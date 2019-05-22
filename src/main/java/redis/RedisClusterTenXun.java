package redis;

import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisClusterConnection;
import org.springframework.data.redis.connection.RedisNode;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

/**
 * 部署在腾讯上的redis集群测试
 * created by zhangxuan9 on 2019/2/13
 */
public class RedisClusterTenXun {

    private static String host = "140.143.206.160";

    private static byte[] redisList = "list".getBytes();

    public static void main(String[] args){
        //ValueOperations<String, String> operations = redisCacheTemplate().opsForValue();
        //String hello = operations.get("hello");
        //System.out.println(hello);
        RedisClusterConnection clusterConnection = getRedisFactory().getClusterConnection();
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
    }

    public static RedisTemplate<String, String> redisCacheTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        LettuceConnectionFactory factory = getRedisFactory();
        template.setConnectionFactory(factory);
        template.afterPropertiesSet();
        return template;
    }

    public static LettuceConnectionFactory getRedisFactory () {
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
