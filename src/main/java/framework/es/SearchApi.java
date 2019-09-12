package framework.es;

import com.carrotsearch.hppc.cursors.ObjectObjectCursor;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.search.SearchType;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.cluster.metadata.IndexMetaData;
import org.elasticsearch.cluster.metadata.MappingMetaData;
import org.elasticsearch.common.collect.ImmutableOpenMap;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
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
                System.out.println("index: " + hit.getIndex() + "   source: " + hit.getSourceAsString());
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
