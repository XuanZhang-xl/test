package xl.test.framework.redis;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * created by zhangxuan9 on 2019/2/13
 */
public class EasyRedisConnection {

    private static String host = "140.143.206.160";
    private static String key = "hello";

    public static void main(String[] args){
        JedisPool pool = new JedisPool(host, 7001);
        Jedis jedis = pool.getResource();
        jedis.set(key, "world");
        String hello = pool.getResource().get(key);
        System.out.println(hello);

        //RedisClient client = RedisClient.create("redis://" + host + ":7001");
        //StatefulRedisConnection<String, String> connection = client.connect();
        //RedisStringCommands sync = connection.sync();
        //System.out.println(sync.get(key));
        //
        //List<RedisURI> list = new ArrayList<>();
        //list.add(RedisURI.create("redis://" + host + ":7001"));
        //list.add(RedisURI.create("redis://" + host + ":7002"));
        //list.add(RedisURI.create("redis://" + host + ":7003"));
        //list.add(RedisURI.create("redis://" + host + ":7004"));
        //list.add(RedisURI.create("redis://" + host + ":7005"));
        //list.add(RedisURI.create("redis://" + host + ":7006"));
        //RedisClusterClient clusterClient = RedisClusterClient.create(list);
        //String s = clusterClient.connect().sync().get(key);
        //System.out.println(s);

    }
}
