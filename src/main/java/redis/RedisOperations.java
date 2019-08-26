package redis;

import org.junit.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.ConvertingCursor;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * redis操作
 * created by XUAN on 2019/08/25
 */
public class RedisOperations {

    // redis集群
    private static RedisTemplate<String, String> redisCluster = RedisDeployOnTenXun.redisClusterTemplate();

    // 单点redis
    private static RedisTemplate<String, String> redisSingle = RedisDeployOnTenXun.redisTemplate();

    /**
     * 存储经纬度, 可用来计算距离以及查找附近的经纬度点
     * @throws Exception
     */
    @Test
    public void geoHash() throws Exception {
        // 存储地址的键
        String companyLocations = "companyLocations";
        GeoOperations<String, String> geoOperation = redisCluster.opsForGeo();
        List<RedisGeoCommands.GeoLocation> geoLocations = new ArrayList<>();
        // 掘金公司经纬度
        geoLocations.add(new RedisGeoCommands.GeoLocation("juejin", new Point(116.48105d, 39.996794d)));
        // 掌阅公司经纬度
        geoLocations.add(new RedisGeoCommands.GeoLocation("ireader", new Point(116.514203d, 39.905409d)));
        // 美团公司经纬度
        geoLocations.add(new RedisGeoCommands.GeoLocation("meituan", new Point(116.489033d, 40.007669d)));
        // 京东公司经纬度
        geoLocations.add(new RedisGeoCommands.GeoLocation("jd", new Point(116.562108d, 39.787602d)));
        // 小米
        geoLocations.add(new RedisGeoCommands.GeoLocation("jd", new Point(116.334255d, 40.027400d)));

        // 添加经纬度
        for (RedisGeoCommands.GeoLocation geoLocation : geoLocations) {
            geoOperation.add(companyLocations, geoLocation);
        }

        // 获得经纬度
        List<Point> jdLocations = geoOperation.position(companyLocations, "jd");
        if (jdLocations != null && jdLocations.size() > 0) {
            System.out.println("jd X: " + jdLocations.get(0).getX() + "  Y: " + jdLocations.get(0).getY());
        } else {
            throw new Exception("jd坐标不存在");
        }

        // 计算距离
        Distance juejinAndjdDistance = geoOperation.distance(companyLocations, "juejin", "jd");
        System.out.println("juejin和jd的距离为" + juejinAndjdDistance.getValue() + juejinAndjdDistance.getUnit());
        System.out.println();

        // 查找附近的点
        Point targetPoint = new Point(116.5d, 40.0d);
        Distance targetDistance = new Distance(20, Metrics.KILOMETERS);
        Circle circle = new Circle(targetPoint, targetDistance);
        // 可以通过这个参数获得经纬度, 距离, 限制获得个数, 排序
        RedisGeoCommands.GeoRadiusCommandArgs geoRadiusCommandArgs = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs().includeCoordinates();
        // 增加获得参数: 距离
        Set<RedisGeoCommands.GeoRadiusCommandArgs.Flag> flags = geoRadiusCommandArgs.getFlags();
        flags.add(RedisGeoCommands.GeoRadiusCommandArgs.Flag.WITHDIST);
        GeoResults<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResults = geoOperation.radius(companyLocations, circle, geoRadiusCommandArgs);
        String summary = "在经度" + targetPoint.getX() + " 纬度" + targetPoint.getY() + " 的" + targetDistance.getValue() + targetDistance.getUnit() + "距离内, 共有";
        if (geoLocationGeoResults != null && geoLocationGeoResults.getContent() != null && geoLocationGeoResults.getContent().size() > 0) {
            System.out.println(summary + geoLocationGeoResults.getContent().size() + "个点, 如下: ");
            for (GeoResult<RedisGeoCommands.GeoLocation<String>> geoLocationGeoResult : geoLocationGeoResults) {
                RedisGeoCommands.GeoLocation<String> geoLocation = geoLocationGeoResult.getContent();
                String pointSummary = geoLocation.getName();
                if (geoLocation.getPoint() != null) {
                    pointSummary = pointSummary + " 经度: " + geoLocation.getPoint().getX() + "  纬度: " + geoLocation.getPoint().getY();
                }
                Distance distance = geoLocationGeoResult.getDistance();
                if (distance != null) {
                    pointSummary = pointSummary  + " 距离: " + distance.getValue() + distance.getUnit();
                }
                System.out.println(pointSummary);
            }
        } else {
            System.out.println(summary + "0个点");
        }
    }

    /**
     * 找出所有符合正则表达式条件的key
     * 由于会遍历所有的key, 时间复杂度为 O(n), 所以,redis提供了游标及遍历个数 这两个参数, 以控制时间防止阻塞其他请求
     *
     * redisTemplate没有提供scan方法, 但是可以自己实现
     */
    @Test
    public void scan() throws IOException {
        String scanKey = "scan_filed";
        // 放入数据
        HashOperations<String, String, String> hashOperations = redisCluster.<String, String>opsForHash();
        if (!redisCluster.hasKey(scanKey)) {
            for (int i = 0; i < 10000; i++) {
                hashOperations.put(scanKey, scanKey + i, String.valueOf(i));
            }
        }
        int count = 0;
        Cursor<Map.Entry<String, String>> cursor = hashOperations.scan(scanKey,
                // 这里的100是指遍历redis的100个孔, 实际结果有多少个是不确定的
                ScanOptions.scanOptions().match("scan_filed8*").count(100).build());
        try {
            while (cursor.hasNext()) {
                Map.Entry<String, String> next = cursor.next();
                String key = next.getKey();
                String value = next.getValue();
                System.out.println("key: " + key + "  value: " + value);
                count++;
            }
            System.out.println("一共遍历了" + count + "个数据");
        } finally {
            if (!cursor.isClosed()) {
                cursor.close();
            }

        }
    }

    // 自己实现 scan
    @SuppressWarnings("unchecked")
    public static Cursor<String> scan(RedisTemplate redisTemplate, String pattern, int limit) {
        ScanOptions options = ScanOptions.scanOptions().match(pattern).count(limit).build();
        RedisSerializer<String> redisSerializer = (RedisSerializer<String>) redisTemplate.getKeySerializer();
        return (Cursor) redisTemplate.executeWithStickyConnection(new RedisCallback<Cursor<String>>() {
            @Override
            public Cursor<String> doInRedis(RedisConnection redisConnection) throws DataAccessException {
                return new ConvertingCursor<>(redisConnection.scan(options), redisSerializer::deserialize);
            }
        });
    }


    /**
     * redis事务操作
     * 事务只保证隔离性, 不保证原子性, 也就是抛出异常后, 正确的指令还是会被执行
     *
     * 另外, 也可以在事务中使用watch, 乐观锁机制
     * 擦 MULTI is currently not supported in cluster mode.
     */
    @Test
    public void redisTransaction() {
        String key = "book";
        Duration duration = Duration.ofSeconds(20);
        // 这句一定要加, 不然 redisSingle.multi(); 无效
        redisSingle.setEnableTransactionSupport(true);
        ValueOperations<String, String> valueOperation = redisSingle.opsForValue();
        try {
            // 开启事务
            redisSingle.multi();
            valueOperation.set(key, "thinking java", duration);
            valueOperation.increment(key);
            String value = valueOperation.get(key);
            System.out.println(value);
            // 执行所有命令
            List<Object> exec = redisSingle.exec();
            // 上面一行执行会报错
            if (exec == null) {
                System.out.println(key + " 事务执行失败");
            } else {
                System.out.println(key + " 事务执行成功, 返回为" + exec);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        String value = valueOperation.get(key);
        System.out.println(value);

        // watch测试
        key ="account_xuan";
        valueOperation.set(key, "0");
        List<Object> execResult = null;
        do {
            // 将key上乐观锁
            redisSingle.watch(key);
            Integer count = Integer.valueOf(valueOperation.get(key));
            count = count + count;
            redisSingle.multi();
            valueOperation.set(key, String.valueOf(count), duration);
            // 抛弃之前的所有命令
            //redisSingle.discard();
            // 返回为null表示事务执行成功
            execResult = redisSingle.exec();
        } while (execResult == null);
        System.out.println(key + " 事务执行成功, 返回为" + execResult);
    }


    /**
     * 使用redis实现 生产者消费者模式
     *
     * 消息多播, 每一个坚监听者都可以收到消息, 无需绑定, 只要生产者与消费者的channel匹配即可收到消息, 如果没有消费者, 会被丢弃
     * 如果消费者挂了又重连, 则挂了期间的消息永久丢失
     * 这功能暂无使用场景, 将被废弃, 请关注 redis 5.0 中的 stream
     *
     * 这里是生产者, 消费者在start项目中
     */
    @Test
    public void pubSub() {
        String data = "这是测试数据";
        redisSingle.convertAndSend("testsubpub", data);

        redisSingle.convertAndSend("codehole.text", data + ".text");
        redisSingle.convertAndSend("codehole.html", "https://www.jianshu.com/p/0840c56a3c20");
        redisSingle.convertAndSend("codehole.json", "{\"type\":\"text\", \"subscribeName\":\"codehole.json\"}");
    }

    /**stream: redis 5.0新增的支持多播的可持久化消息队列
     */
    @Test
    public void redisStream() {

    }

}
