package framework.es;

import com.alibaba.fastjson.JSON;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.client.transport.TransportClient;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * created by XUAN on 2019/9/11
 */
public class ElasticSearchApi {

    TransportClient client;

    @Before
    public void initClient() {
        client = ElasticSearchClient.getClient();
    }

    @Test
    public void getApi() {
        GetResponse getResponse = client.prepareGet("get-togetger", "group", "2").get();
        if (getResponse.isExists()) {
            System.out.println(JSON.toJSONString(getResponse.getSource()));
        } else {
            System.out.println("失败: " + JSON.toJSONString(getResponse));
        }
    }

    @After
    public void close() {
        if (client != null) {
            client.close();
        }
    }
}
