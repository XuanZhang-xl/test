package redis;

import org.junit.Test;
import org.springframework.data.geo.Circle;
import org.springframework.data.geo.Distance;
import org.springframework.data.geo.GeoResult;
import org.springframework.data.geo.GeoResults;
import org.springframework.data.geo.Metrics;
import org.springframework.data.geo.Point;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * redis操作
 * created by XUAN on 2019/08/25
 */
public class RedisOperations {

    private static RedisTemplate<String, String> redisTemplate = RedisClusterTenXun.redisCacheTemplate();


    /**
     * 存储经纬度, 可用来计算距离以及查找附近的经纬度点
     * @throws Exception
     */
    @Test
    public void geoHash() throws Exception {
        // 存储地址的键
        String companyLocations = "companyLocations";
        GeoOperations<String, String> geoOperation = redisTemplate.opsForGeo();
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

}
