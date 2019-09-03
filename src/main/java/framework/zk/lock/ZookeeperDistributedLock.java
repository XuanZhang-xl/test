package framework.zk.lock;

import framework.zk.CuratorOperator;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.junit.Test;

import javax.security.auth.login.Configuration;
import java.util.concurrent.CountDownLatch;

/**
 * zk分布式锁
 * created by XUAN on 2019/9/3
 */
public class ZookeeperDistributedLock {

    // 所有锁都放在这个路径下
    private static final String ORDER_LOCK = "ORDER_LOCK";

    // 获得锁重试次数
    private int retryTime = 5;

    private CuratorFramework operator = null;
    private void init() {
        operator = new CuratorOperator("distributed-lock").curatorFramework;
        operator.start();
        System.out.println("当前客户端状态: " + operator.getState().name());
    }

    public void initLock() throws Exception {
        if (operator == null) {
            init();
        }
        if (operator.checkExists().forPath("/" + ORDER_LOCK) == null) {
            operator.create()
                    .creatingParentsIfNeeded()
                    .withMode(CreateMode.PERSISTENT)
                    .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                    .forPath("/" + ORDER_LOCK);
        }
        addWatcherToLock("/" + ORDER_LOCK);
    }

    public boolean getLock(String lockId) {
        int i = 0;
        while(i < retryTime) {
            try {
                operator.create()
                        .creatingParentsIfNeeded()
                        // 临时节点, 挂了锁就消失
                        .withMode(CreateMode.EPHEMERAL)
                        .withACL(ZooDefs.Ids.OPEN_ACL_UNSAFE)
                        .forPath("/" + ORDER_LOCK + "/" + lockId);
                System.out.println("获得分布式锁" + lockId + "成功");
                return true;
            } catch (Exception e) {
                System.out.println("获得分布式锁失败, 第" + (++i) + "次重试");
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
            }
        }
        System.out.println("获得分布式锁" + lockId + "失败");
        return false;
    }

    public boolean releaseLock(String lockId) {
        try {
            if (operator.checkExists().forPath("/" + ORDER_LOCK + "/" + lockId) != null) {
                operator.delete().forPath("/" + ORDER_LOCK + "/" + lockId);
            }
            System.out.println("分布式锁" + lockId + "已释放");
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }



    private void addWatcherToLock(String nodePath) throws Exception {
        if (operator == null) {
            init();
        }
        PathChildrenCache cache = new PathChildrenCache(operator, nodePath, true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(new PathChildrenCacheListener() {
            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event) throws Exception {
                if (event == null) {
                    System.out.println("event为空");
                    return;
                }
                if (event.getData() == null) {
                    System.out.println("data为空");
                    System.out.println("监控到分布式锁 发生事件: " + event.getType().name());
                    return;
                }
                if (event.getType() == null) {
                    System.out.println("type为空");
                    System.out.println("监控到分布式锁节点" + event.getData().getPath() + "发生事件");
                    return;
                }
                System.out.println("监控到分布式锁节点" + event.getData().getPath() + "发生事件: " + event.getType().name()) ;
                //if (event.getType().equals(PathChildrenCacheEvent.Type.CHILD_REMOVED)) {
                //    String path = event.getData().getPath();
                //    System.out.println("上一个会话已释放锁或会话已断开, 节点路径为: " + path);
                //    if (path.contains(ORDER_ID)) {
                //        System.out.println("释放计数器");
                //        countDownLatch.countDown();
                //    }
                //}
            }
        });
    }
}
