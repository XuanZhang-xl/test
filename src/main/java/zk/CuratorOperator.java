package zk;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryForever;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.retry.RetryOneTime;
import org.apache.curator.retry.RetryUntilElapsed;

/**
 * created by XUAN on 2019/8/14
 */
public class CuratorOperator {

    //public static final String zkServer = "140.143.206.160:2181";
    public static final String zkServer = "140.143.206.160:2181,140.143.206.160:2182,140.143.206.160:2183";

    public static final Integer timeout = 5000;

    public CuratorFramework curatorFramework = null;

    public CuratorOperator() {

        // baseSleepTimeMs初始重试时间, 重试次数越多, 重试间隔事件越长, 这里最多5次重试
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 5);

        // 固定间隔重试, 推荐
        //RetryPolicy retryPolicy = new RetryNTimes(1000, 5);

        // 只重试一次
        //RetryPolicy retryPolicy = new RetryOneTime(1000);

        // 永远重试...
        //RetryPolicy retryPolicy = new RetryForever(1000);

        // 有最大重试时间
        //RetryPolicy retryPolicy = new RetryUntilElapsed(5000, 1000);

        curatorFramework = CuratorFrameworkFactory
                .builder()
                .connectString(zkServer)
                .sessionTimeoutMs(timeout)
                .retryPolicy(retryPolicy)
                // 根节点下会创建workspace节点
                .namespace("workspace")
                .build();
    }
}
