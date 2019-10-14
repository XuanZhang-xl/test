package xl.test.framework.es;

import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.TransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * es客户端
 * created by XUAN on 2019/9/11
 */
public class ElasticSearchClient {

    public static TransportClient getClient() {
        Settings.Builder builder = Settings.builder();
        builder.put("cluster.name", "docker-cluster");
        // 不主动探知其他节点
        builder.put("client.transport.sniff", false);
        builder.put("thread_pool.search.size", 5);
        Settings settings = builder.build();

        TransportClient client = new PreBuiltTransportClient(settings);
        try {
            client.addTransportAddress(new TransportAddress(InetAddress.getByName("140.143.206.160"), 9300));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        return client;
    }
}
