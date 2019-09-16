package framework.es;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.ClearScrollRequestBuilder;
import org.elasticsearch.action.search.ClearScrollResponse;
import org.elasticsearch.action.search.MultiSearchResponse;
import org.elasticsearch.action.search.SearchRequestBuilder;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregator;
import org.elasticsearch.search.aggregations.bucket.histogram.DateHistogramInterval;
import org.elasticsearch.search.aggregations.bucket.histogram.Histogram;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.sort.FieldSortBuilder;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * https://www.elastic.co/guide/en/elasticsearch/reference/6.8/search.html
 * created by XUAN on 2019/9/12
 */
public class SearchApi {

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
     * 查出所有数据
     * @throws Exception
     */
    @Test
    public void basicSearch() throws Exception {
        SearchResponse response = client.prepareSearch().get();
        printResult(response);
    }

    /**
     * 根据key value查询
     * @throws Exception
     */
    @Test
    public void searchByParams() throws Exception {
        SearchResponse response = client.prepareSearch("twitter", "get-togetger")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                // Query
                .setQuery(QueryBuilders.termQuery("awesome", "absolutely"))
                // Filter  TODO: from()gt()    to()lt() 的区别?  还有时间怎么查啊
                .setPostFilter(QueryBuilders.rangeQuery("postDate").gt(getPastDate(10)))
                // 限制数量, from 起始位置, size 位移
                .setFrom(0).setSize(60).setExplain(true)
                .get();
        printResult(response);
    }


    /**
     * 获得mapping
     * @throws Exception
     */
    @Test
    public void listMappingInfo() throws Exception {
        ImmutableOpenMap<String, MappingMetaData> mappings = client.admin()
                .cluster()
                .prepareState()
                .execute()
                .actionGet()
                .getState()
                .getMetaData()
                .getIndices()
                .get(index)
                .getMappings();
        Iterator<ObjectObjectCursor<String, MappingMetaData>> iterator = mappings.iterator();
        while(iterator.hasNext()) {
            ObjectObjectCursor<String, MappingMetaData> next = iterator.next();
            int index = next.index;
            String key = next.key;
            Map<String, Object> sourceAsMap = next.value.getSourceAsMap();
            for (Map.Entry<String, Object> entry : sourceAsMap.entrySet()) {
                System.out.println("index:" + index + " key:" + key + " entryKey:" + entry.getKey() + "  entryValue:" + entry.getValue());
            }
        }
    }

    /**
     * 滚动输出, 当输出过大时, 可以按条数批量输出
     * @throws Exception
     */
    @Test
    public void scroll() throws Exception {
        // TODO: 明明有name=kimchy 的记录, 但是加了这个条件后就再也查不到了
        QueryBuilder qb = QueryBuilders.termQuery("name", "kimchy");

        SearchResponse scrollResp = client.prepareSearch("twitter").setTypes("_doc")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                //.setQuery(qb)
                //max of 100 hits will be returned for each scroll
                .setSize(1).get();
        //Scroll until no hits are returned
        do {
            for (SearchHit hit : scrollResp.getHits().getHits()) {
                System.out.println("当前ScrollId: " + scrollResp.getScrollId());
                System.out.println(hit.getSourceAsString());
            }
            scrollResp = client.prepareSearchScroll(scrollResp.getScrollId()).setScroll(new TimeValue(60000)).execute().actionGet();
        // Zero hits mark the end of the scroll and the while loop.
        } while(scrollResp.getHits().getHits().length != 0);



    }

    @Test
    public void clearScroll() throws Exception {
        SearchResponse scrollResp = client.prepareSearch("twitter").setTypes("_doc")
                .addSort(FieldSortBuilder.DOC_FIELD_NAME, SortOrder.ASC)
                .setScroll(new TimeValue(60000))
                .setSize(1).get();

        List<String> scrollIdList = new ArrayList<>();
        scrollIdList.add(scrollResp.getScrollId());
        // 清除 Scroll
        ClearScrollRequestBuilder clearScrollRequestBuilder = client.prepareClearScroll();
        clearScrollRequestBuilder.setScrollIds(scrollIdList);
        ClearScrollResponse response = clearScrollRequestBuilder.get();
        System.out.println("取消scroll结果: " + response.isSucceeded());
    }

    /**
     * 简单多条件或查询
     * @throws Exception
     */
    @Test
    public void multiSearch() throws Exception {
        SearchRequestBuilder srb1 = client
                .prepareSearch().setQuery(QueryBuilders.queryStringQuery("elasticsearch")).setSize(10);
        SearchRequestBuilder srb2 = client
                .prepareSearch().setQuery(QueryBuilders.matchQuery("user", "kimchy")).setSize(10);

        MultiSearchResponse sr = client.prepareMultiSearch()
                .add(srb1)
                .add(srb2)
                .get();

        // You will get all individual responses from MultiSearchResponse#getResponses()
        for (MultiSearchResponse.Item item : sr.getResponses()) {
            SearchResponse response = item.getResponse();
            System.out.println("命中:" + response.getHits().getTotalHits() + "条记录");
            if (response.getHits().getTotalHits() > 0) {
                for (SearchHit hit : response.getHits().getHits()) {
                    System.out.println(hit.getSourceAsString());
                }
            }
        }
    }

    /**
     * 使用QueryBuilder
     * termQuery("key", obj) 完全匹配
     * termsQuery("key", obj1, obj2..)   一次匹配多个值
     * matchQuery("key", Obj) 单个匹配, field不支持通配符, 前缀具高级特性
     * multiMatchQuery("text", "field1", "field2"..);  匹配多个字段, field有通配符忒行
     * matchAllQuery();         匹配所有文件
     */
    @Test
    public void termsAndMatchQueryBuilder() throws Exception {
        //QueryBuilder queryBuilder = QueryBuilders.termsQuery("message", "out", "xuan10");
        //QueryBuilder queryBuilder = QueryBuilders.matchQuery("message", "out");
        //QueryBuilder queryBuilder = QueryBuilders.multiMatchQuery("kimchy", "user", "message", "gender");
        QueryBuilder queryBuilder = QueryBuilders.matchAllQuery();

        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .execute()
                .actionGet(Long.valueOf(10000));
        printResult(searchResponse);

    }

    @Test
    public void boolQueryBuilder() throws Exception {

        BoolQueryBuilder queryBuilder = QueryBuilders.boolQuery();
        queryBuilder
                .must(QueryBuilders.matchQuery("message", "es"))
                //.must(QueryBuilders.termsQuery("user", "kimchy"))
                .should(QueryBuilders.termsQuery("awesome", "absolutely"))
        ;

        BoolQueryBuilder subQueryBuilder = QueryBuilders.boolQuery();
        subQueryBuilder.should(QueryBuilders.termsQuery("awesome", "relative"));
        //queryBuilder.must(subQueryBuilder);


        SearchResponse searchResponse = client.prepareSearch(index)
                .setTypes(type)
                .setSearchType(SearchType.QUERY_THEN_FETCH)
                .setQuery(queryBuilder)
                .execute()
                .actionGet(Long.valueOf(10000));
        printResult(searchResponse);

    }

    /**
     * 查询遍历抽取
     * @param queryBuilder
     */
    private void searchFunction(QueryBuilder queryBuilder) {
        SearchResponse response = client.prepareSearch("twitter")
                .setSearchType(SearchType.DFS_QUERY_THEN_FETCH)
                .setScroll(new TimeValue(60000))
                .setQuery(queryBuilder)
                .setSize(100).execute().actionGet();
    }

    @Test
    public void aggregation() throws Exception {
        Map<String, Object> metaData = new HashMap<>();
        metaData.put("fielddata", true);
        SearchResponse sr = client.prepareSearch(index).setTypes(type)
                .setQuery(QueryBuilders.matchAllQuery())
                // TODO: java.lang.IllegalArgumentException: Fielddata is disabled on text fields by default. Set fielddata=true on [user] in order to load fielddata in memory by uninverting the inverted index. Note that this can however use significant memory. Alternatively use a keyword field instead.
                //.addAggregation(
                //        AggregationBuilders.terms("groupByUser").setMetaData(metaData).field("user")
                //)
                .addAggregation(
                        AggregationBuilders.dateHistogram("groupByPostDate")
                                .field("postDate")
                                .dateHistogramInterval(DateHistogramInterval.YEAR)
                )
                .get();

        Terms groupByUser = sr.getAggregations().get("groupByUser");
        if (groupByUser != null) {
            for (Terms.Bucket bucket : groupByUser.getBuckets()) {
                System.out.println(bucket.getKeyAsString() + "     " + bucket.getDocCount());
            }
        }

        Histogram groupByPostDate = sr.getAggregations().get("groupByPostDate");
        if (groupByPostDate != null) {
            for (Histogram.Bucket bucket : groupByPostDate.getBuckets()) {
                System.out.println(bucket.getKeyAsString() + "     " + bucket.getDocCount());
            }
        }

    }


    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }

    private void printResult(SearchResponse response) throws JsonProcessingException {
        if (response.status().getStatus() == 200) {
            System.out.println("成功");
            for (SearchHit hit : response.getHits().getHits()) {
                System.out.println("index: " + hit.getIndex() + "  id: " + hit.getId() + "   source: " + hit.getSourceAsString());
            }
        } else {
            System.out.println("错误: " + mapper.writeValueAsString(response));
        }
    }

    private Date getPastDate(int past) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DATE, calendar.get(Calendar.DATE) - past);
        Date date = calendar.getTime();
        new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
        return date;
    }
}
