package framework.zk.lock;

import org.junit.Before;
import org.junit.Test;

/**
 * 分布式锁测试
 * 与redis锁相比
 * 优势:
 *      1. 将节点设置为临时节点, 可避免宕机锁无法释放问题, jedis到目前都没支持 setNx设置失效时间
 *      2. redis一般都是单机部署才能用分布式锁, 而zookeeper可以分布式部署使用分布式锁, 可用性高了一点
 *      3. zk分布式锁，获取不到锁，注册个监听器即可，不需要不断主动尝试获取锁，有些场景性能开销较小
 * 劣势: 也没有办法避免锁定时间超过超时时间的问题
 * created by XUAN on 2019/9/3
 */
public class DistributedLockTest {

    public static String orderId = "6347898732343117";

    ZookeeperDistributedLock lock = new ZookeeperDistributedLock();

    @Before
    public void init() throws Exception {
        lock.initLock();
    }

    /**
     * 1. 获得锁
     * 2. 释放锁
     * @throws InterruptedException
     */
    @Test
    public void test1() throws InterruptedException {
        lock.getLock(orderId);
        Thread.sleep(5000);
        lock.releaseLock(orderId);
        Thread.sleep(5000);
    }

    /**
     * 1. 获得锁
     * 2. 获得锁失败
     * @throws InterruptedException
     */
    @Test
    public void test2() throws InterruptedException {
        lock.getLock(orderId);
        lock.getLock(orderId);
        Thread.sleep(5000);
    }

    /**
     * 1. 获得锁
     * 2. 重试获得锁
     * 3. 释放锁
     * 4. 重试获得锁成功
     * @throws InterruptedException
     */
    @Test
    public void test3() throws InterruptedException {
        lock.getLock(orderId);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lock.releaseLock(orderId);
            }
        }).start();
        lock.getLock(orderId);
        Thread.sleep(5000);
    }
}
