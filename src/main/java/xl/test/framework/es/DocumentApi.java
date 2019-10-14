package xl.test.framework.es;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.admin.cluster.node.tasks.list.ListTasksResponse;
import org.elasticsearch.action.bulk.BackoffPolicy;
import org.elasticsearch.action.bulk.BulkProcessor;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetItemResponse;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.unit.ByteSizeUnit;
import org.elasticsearch.common.unit.ByteSizeValue;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentFactory;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.reindex.BulkByScrollResponse;
import org.elasticsearch.index.reindex.BulkByScrollTask;
import org.elasticsearch.index.reindex.DeleteByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryAction;
import org.elasticsearch.index.reindex.UpdateByQueryRequestBuilder;
import org.elasticsearch.script.Script;
import org.elasticsearch.script.ScriptType;
import org.elasticsearch.tasks.TaskId;
import org.elasticsearch.tasks.TaskInfo;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;
import java.util.Date;
import java.util.concurrent.CountDownLatch;

/**
 * es version: 6.8.3
 *
 * https://segmentfault.com/a/1190000015220491
 * https://www.elastic.co/guide/en/elasticsearch/client/java-api/6.8/java-docs.html
 *
 * created by XUAN on 2019/9/11
 */
public class DocumentApi {

    TransportClient client;

    ObjectMapper mapper = new ObjectMapper();

    String index = "twitter";
    String type = "_doc";
    String id = "1";

    @Before
    public void initClient() {
        client = ElasticSearchClient.getClient();
    }

    /**
     * 直接放入文档, 同时创建index和type
     * @throws IOException
     */
    @Test
    public void buildIndex() throws IOException {
        // 方式1, 指定了id, 第一次为创建, 后续为更新这个id
        IndexResponse response1 = client.prepareIndex("twitter", "_doc", "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
                .get();
        sysoIndexResponse(response1);

        // 方式2
        String json = "{" +
                "\"user\":\"kimchy\"," +
                "\"postDate\":\"2013-01-30\"," +
                "\"message\":\"trying out Elasticsearch\"" +
                "}";
        // 自动生成id, 会一直创建新的文档
        IndexResponse response2 = client.prepareIndex("twitter", "_doc")
                .setSource(json, XContentType.JSON)
                .get();
        sysoIndexResponse(response2);
    }

    /**
     * 如果没有index, 报错
     * 直接根据文档id获得文档, 效率最高
     */
    @Test
    public void getApi() throws IOException {

        GetResponse getResponse1 = client.prepareGet("get-togetger", "group", "1").get();
        if (getResponse1.isExists()) {
            System.out.println(mapper.writeValueAsString(getResponse1.getSource()));
        } else {
            System.out.println("失败: " + mapper.writeValueAsString(getResponse1));
        }
        GetResponse getResponse2 = client.prepareGet("twitter", "_doc", "11").get();
        if (getResponse2.isExists()) {
            System.out.println(mapper.writeValueAsString(getResponse2.getSource()));
        } else {
            System.out.println("失败: " + mapper.writeValueAsString(getResponse2));
        }
    }


    @Test
    public void deleteApi() throws IOException {
        DeleteResponse response = client.prepareDelete("twitter", "_doc", "1").get();
        if (response.status().getStatus() == 200) {
            System.out.println(response.toString());
        } else {
            System.out.println("失败: " + response.toString());
        }
    }

    /**
     * The delete by query API allows one to delete a given set of documents based on the result of a query
     * 同步查询删除
     * @throws IOException
     */
    @Test
    public void deleteByQueryApiSync() throws IOException {
        BulkByScrollResponse response =
                DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                        .filter(QueryBuilders.matchQuery("gender", "male"))  //query
                        .source("twitter")                                              //index
                        .get();                                                         //execute the operation
        System.out.println("删除了" + response.getDeleted() + "条记录");                 // number of deleted documents
    }

    /**
     * 异步查询删除
     * @throws IOException
     */
    @Test
    public void deleteByQueryApiAsync() throws Exception {
        CountDownLatch countDownLatch = new CountDownLatch(1);
        DeleteByQueryAction.INSTANCE.newRequestBuilder(client)
                .filter(QueryBuilders.matchQuery("gender", "male"))
                .source("twitter")
                .execute(new ActionListener<BulkByScrollResponse>() {
                    @Override
                    public void onResponse(BulkByScrollResponse response) {
                        System.out.println("删除了" + response.getDeleted() + "条记录");
                        countDownLatch.countDown();
                    }
                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                        countDownLatch.countDown();
                    }
                });
        countDownLatch.await();
    }

    /**
     * 更新, 文档不存在报错, 字段不存在新增
     * @throws Exception
     */
    @Test
    public void updateApi() throws Exception {
        GetResponse getResponse = client.prepareGet(index, type, id).get();
        if (getResponse.isExists()) {
            System.out.println("原数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        } else {
            return;
        }
        UpdateRequest updateRequest = new UpdateRequest();
        updateRequest.index(index);
        updateRequest.type(type);
        updateRequest.id(id);
        updateRequest.doc(XContentFactory.jsonBuilder()
                .startObject()
                .field("user1", "lu")
                .endObject());
        UpdateResponse updateResponse = client.update(updateRequest).get();
        if (updateResponse.status().getStatus() == 200) {
            getResponse = client.prepareGet(index, type, id).get();
            System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        } else {
            System.out.println("更新失败");
            System.out.println(mapper.writeValueAsString(updateResponse));
        }

        // 同下面两种写法
        UpdateResponse updateResponse2 = client.prepareUpdate(index, type, id)
                .setScript(new Script("ctx._source.user = \"xuan2\""))
                .get();
        getResponse = client.prepareGet(index, type, id).get();
        System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));

        UpdateResponse updateResponse3 = client.prepareUpdate(index, type, id)
                .setDoc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "xuan3")
                        .endObject())
                .get();
        getResponse = client.prepareGet(index, type, id).get();
        System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));
    }

    /**
     * 更新
     * 如果文档存在, 则 updateRequest() 会更新, upsert()会被忽略
     * 如果文档不存在, 则会使用upsert()的内容新增文档
     * @throws Exception
     */
    @Test
    public void upsert() throws Exception {
        id = "2";
        GetResponse getResponse = client.prepareGet(index, type, id).get();
        if (getResponse.isExists()) {
            System.out.println("原数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        } else {
            return;
        }
        IndexRequest indexRequest = new IndexRequest(index, type, id)
                .source(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("name", "Joe Smith")
                        .field("gender", "male")
                        .field("ping", "pong")
                        .endObject());
        UpdateRequest updateRequest = new UpdateRequest(index, type, id)
                .doc(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("gender", "female")
                        .field("hello", "world")
                        .endObject())
                .upsert(indexRequest);
        client.update(updateRequest).get();
        getResponse = client.prepareGet(index, type, id).get();
        System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));
    }

    /**
     * 并发更新控制
     * @throws Exception
     */
    @Test
    public void concurrentUpdateApi() throws Exception {
        GetResponse getResponse = client.prepareGet(index, type, id).get();
        if (getResponse.isExists()) {
            System.out.println("原数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        } else {
            return;
        }

        UpdateResponse updateResponse1 = client.prepareUpdate(index, type, id)
                .setScript(new Script( "ctx._source.user = \"xuan1\""))
                .setVersion(getResponse.getVersion())
                .get();
        if (updateResponse1.status().getStatus() == 200) {
            System.out.println("updateResponse1更新成功");
            GetResponse getResponse1 = client.prepareGet(index, type, id).get();
            System.out.println("新数据为 " + mapper.writeValueAsString(getResponse1.getSource()));
        } else {
            System.out.println("updateResponse1更新失败" + updateResponse1.status().getStatus());
        }
        UpdateResponse updateResponse2 = client.prepareUpdate(index, type, id)
                .setScript(new Script( "ctx._source.user = \"xuan2\""))
                .setVersion(getResponse.getVersion())
                .get();
        if (updateResponse2.status().getStatus() == 200) {
            System.out.println("updateResponse2更新成功");
            GetResponse getResponse2 = client.prepareGet(index, type, id).get();
            System.out.println("新数据为 " + mapper.writeValueAsString(getResponse2.getSource()));
        } else {
            System.out.println("updateResponse2更新失败" + updateResponse2.status().getStatus());
        }
    }

    @Test
    public void multiGetApi() throws Exception {
        MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                .add("twitter", "_doc", "1")
                .add("twitter", "_doc", "2", "3", "4")
                .add("another", "_doc", "foo")
                .get();

        for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
            // 如果没有index, 则会失败
            if (itemResponse.isFailed()) {
                GetResponse response = itemResponse.getResponse();
                if (response != null && response.isExists()) {
                    System.out.println(response.getSourceAsString());
                }
            }
        }
    }

    /**
     * The bulk API allows one to index and delete several documents in a single request.
     * 批量执行  增加索引, 新增, 更新, 删除
     * @throws Exception
     */
    @Test
    public void bulkApi() throws Exception {

        BulkRequestBuilder bulkRequest = client.prepareBulk();

        // either use client#prepare, or use Requests# to directly build index/delete requests
        bulkRequest.add(client.prepareIndex(index, type, "1")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "xuan")
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                )
        );

        bulkRequest.add(client.prepareIndex(index, type, "2")
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "kimchy")
                        .field("postDate", new Date())
                        .field("message", "another post")
                        .endObject()
                )
        );

        BulkResponse bulkResponse = bulkRequest.get();
        if (!bulkResponse.hasFailures()) {
            // process failures by iterating through each bulk response item
            MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                    .add(index, type, "1", "2")
                    .get();

            for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
                System.out.println(itemResponse.getResponse().getSourceAsString());
            }
        }
    }

    /**
     * 自动批量执行??
     *
     * By default, BulkProcessor:
     *     sets bulkActions to 1000
     *     sets bulkSize to 5mb
     *     does not set flushInterval
     *     sets concurrentRequests to 1, which means an asynchronous execution of the flush operation.
     *     sets backoffPolicy to an exponential backoff with 8 retries and a start delay of 50ms. The total wait time is roughly 5.1 seconds.
     * @throws Exception
     */
    @Test
    public void bulkProcessorApi() throws Exception {
        String id1 ="10";
        String id2 ="11";
        CountDownLatch countDownLatch = new CountDownLatch(1);

        BulkProcessor bulkProcessor = BulkProcessor.builder(
                client, new BulkProcessor.Listener() {
                    // This method is called just before bulk is executed. You can for example see the numberOfActions with request.numberOfActions()
                    @Override
                    public void beforeBulk(long executionId, BulkRequest request) {
                        System.out.println("beforeBulk executionId:" + executionId);
                    }
                    // This method is called after bulk execution. You can for example check if there was some failing requests with response.hasFailures()
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, BulkResponse response) {
                        System.out.println("afterBulk executionId: " + executionId);
                        if (!response.hasFailures()) {
                            MultiGetResponse multiGetItemResponses = client.prepareMultiGet()
                                    .add(index, type, id1, id2)
                                    .get();
                            for (MultiGetItemResponse itemResponse : multiGetItemResponses) {
                                System.out.println(itemResponse.getResponse().getSourceAsString());
                            }
                        }
                        countDownLatch.countDown();
                    }
                    // This method is called when the bulk failed and raised a Throwable
                    @Override
                    public void afterBulk(long executionId, BulkRequest request, Throwable failure) {
                        System.out.println("error " + failure);
                        countDownLatch.countDown();
                    }
                })
                // We want to execute the bulk every 100 requests
                .setBulkActions(100)
                // We want to flush the bulk every 5mb
                .setBulkSize(new ByteSizeValue(5, ByteSizeUnit.MB))
                // We want to flush the bulk every 5 seconds whatever the number of requests
                .setFlushInterval(TimeValue.timeValueSeconds(5))
                // Set the number of concurrent requests. A value of 0 means that only a single request will be allowed to be executed.
                // A value of 1 means 1 concurrent request is allowed to be executed while accumulating new bulk requests.
                .setConcurrentRequests(1)
                .setBackoffPolicy(
                        // Set a custom backoff policy which will initially wait for 100ms, increase exponentially and retries up to three times.
                        // A retry is attempted whenever one or more bulk item requests have failed with an EsRejectedExecutionException which indicates that there were too little compute resources available for processing the request.
                        // To disable backoff, pass BackoffPolicy.noBackoff().
                        BackoffPolicy.exponentialBackoff(TimeValue.timeValueMillis(100), 3))
                .build();

        bulkProcessor.add(client.prepareIndex(index, type, id1)
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "xuan" + id1)
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                ).request()
        );
        bulkProcessor.add(client.prepareIndex(index, type, id2)
                .setSource(XContentFactory.jsonBuilder()
                        .startObject()
                        .field("user", "xuan" + id2)
                        .field("postDate", new Date())
                        .field("message", "trying out Elasticsearch")
                        .endObject()
                ).request()
        );
        // Flush any remaining requests
        // 不flush的话要等5秒
        //bulkProcessor.flush();

        countDownLatch.await();

        // Or close the bulkProcessor if you don't need it anymore
        bulkProcessor.close();
    }

    /**
     * 根据条件更新
     * @throws Exception
     */
    @Test
    public void updateByQueryApi() throws Exception {
        id = "11";
        UpdateByQueryRequestBuilder updateByQuery =
                UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        updateByQuery.source(index).abortOnVersionConflict(false)
                .filter(QueryBuilders.termQuery("user", "xuan" + id))
                // 限制更新数量, ??? 只更新前1000条吗? TODO
                .size(1000)
                .script(new Script(ScriptType.INLINE,
                        // lang代表使用哪种脚本文件修改, painless是es默认的脚本语言, 还可以是Python, js等
                        "painless",
                        // 指定脚本内容  ctx 代表es上下文, _source 代表文档
                        "ctx._source.message = 'es'",
                        Collections.emptyMap()));;
        BulkByScrollResponse response = updateByQuery.get();
        System.out.println("更新了" + response.getUpdated() + "条数据");
        if (response.getUpdated() > 0) {
            GetResponse getResponse = client.prepareGet(index, type, id).get();
            System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        }

        updateByQuery =
                UpdateByQueryAction.INSTANCE.newRequestBuilder(client);
        updateByQuery.source(index)
                .script(new Script(
                        ScriptType.INLINE,
                        "painless",
                        "if (ctx._source.awesome == 'absolutely') {"
                                + "  ctx.op='noop'"
                                + "} else if (ctx._source.awesome == 'lame') {"
                                + "  ctx.op='delete'"
                                + "} else {"
                                + "ctx._source.awesome = 'absolutely'}",
                        Collections.emptyMap()));
        response = updateByQuery.get();
        if (response.getUpdated() > 0) {
            GetResponse getResponse = client.prepareGet(index, type, id).get();
            System.out.println("新数据为 " + mapper.writeValueAsString(getResponse.getSource()));
        }
    }

    /**
     * 可以使用Task API获取所有正在运行的update-by-query请求的状态:
     * @throws Exception
     */
    @Test
    public void taskApi() throws Exception {
        ListTasksResponse tasksList = client.admin().cluster().prepareListTasks()
                .setActions(UpdateByQueryAction.NAME).setDetailed(true).get();
        for (TaskInfo info: tasksList.getTasks()) {
            TaskId taskId = info.getTaskId();
            BulkByScrollTask.Status status = (BulkByScrollTask.Status) info.getStatus();
            System.out.println(status.toString());
        }
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    private void sysoIndexResponse(IndexResponse response) {
        if (response.status().getStatus() == 200) {
            String _index = response.getIndex();
            System.out.println("_index : " + _index);
            String _type = response.getType();
            System.out.println("_type : " + _type);
            // Document ID (generated or not)
            String _id = response.getId();
            System.out.println("_id : " + _id);
            // Version (if it's the first time you index this document, you will get: 1)
            long _version = response.getVersion();
            System.out.println("_version : " + _version);
        } else {
            System.out.println(response.toString());
        }
    }
}
