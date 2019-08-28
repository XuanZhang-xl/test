package framework.redis;

import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.RedisSentinelConnection;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 部署在腾讯上的集群及单点redis测试
 * created by zhangxuan9 on 2019/2/13
 */
public class RedisDeployOnTenXun {

    private static String host = "140.143.206.160";

    private static byte[] redisList = "list".getBytes();

    public static void main(String[] args) throws InterruptedException {
        //ValueOperations<String, String> operations = redisClusterTemplate().opsForValue();
        //String hello = operations.get("hello");
        //System.out.println(hello);


        RedisClusterConnection clusterConnection = getRedisClusterFactory().getClusterConnection();
        clusterConnection.set("hello".getBytes(), "world".getBytes());
        byte[] bytes = clusterConnection.get("hello".getBytes());
        System.out.println(new String(bytes));

        List<byte[]> ranges = clusterConnection.lRange(redisList, 0, -1);
        for (byte[] range : ranges) {
            System.out.println(new String(range));
        }



        //Long num = clusterConnection.rPush(redisList, "企鹅老婆".getBytes());
        //num = clusterConnection.rPush(redisList, "企鹅老婆2".getBytes());
        //num = clusterConnection.rPush(redisList, "企鹅老婆232".getBytes());
        //// 最多阻塞两秒, 来拿出list队列中的元素
        //byte[] pop = clusterConnection.rPop(redisList);
        //System.out.println(new String(pop));



        RedisClusterClient redisClient = RedisClusterClient.create("redis://140.143.206.160:7001");
        StatefulRedisClusterConnection<String, String> connection = redisClient.connect();
        System.out.println("Connected to Redis");
        RedisAdvancedClusterCommands<String, String> sync = connection.sync();
        RedisAdvancedClusterAsyncCommands<String, String> async = connection.async();


        System.out.println(sync.get("hello"));
        connection.close();
        redisClient.shutdown();
    }
    public static RedisClusterConnection redisClusterConnection() {
        return getRedisClusterFactory().getClusterConnection();
    }

    /**
     * 获得redis集群
     * @return
     */
    public static RedisTemplate<String, String> redisClusterTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();
        LettuceConnectionFactory factory = getRedisClusterFactory();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    /**
     * 获得单点redis
     * @return
     */
    public static RedisTemplate<String, String> redisTemplate() {
        StringRedisTemplate template = new StringRedisTemplate();

        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration(host, 6379);
        LettuceConnectionFactory factory = new LettuceConnectionFactory(configuration, clientConfig);
        factory.afterPropertiesSet();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    public static LettuceConnectionFactory getRedisClusterFactory() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration();
        List<RedisNode> redisNodes = new ArrayList<RedisNode>();
        redisNodes.add(new RedisNode(host, 7001));
        redisNodes.add(new RedisNode(host, 7002));
        redisNodes.add(new RedisNode(host, 7003));
        redisNodes.add(new RedisNode(host, 7004));
        redisNodes.add(new RedisNode(host, 7005));
        redisNodes.add(new RedisNode(host, 7006));
        redisClusterConfiguration.setClusterNodes(redisNodes);
        redisClusterConfiguration.setMaxRedirects(3);

        // TODO: 使用ssl的配置??
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .commandTimeout(Duration.ofSeconds(2))
                .shutdownTimeout(Duration.ZERO)
                .build();

        LettuceConnectionFactory factory = new LettuceConnectionFactory(redisClusterConfiguration, clientConfig);
        factory.afterPropertiesSet();
        return factory;
    }

}
