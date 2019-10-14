package xl.test.javabasic.concurrent;

import org.junit.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * TODO: 为啥非公平锁也是顺序获得锁???
 * created by XUAN on 2019/9/19
 */
public class ReentrantReadWriteLockTest {

    ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    CountDownLatch latch = new CountDownLatch(1);

    @Test
    public void testReentrantReadWriteLock() throws InterruptedException {

        new Thread(new WriteLockRunnable(4)).start();
        for (int i = 0; i < 3; i++) {
            new Thread(new ReadLockRunnable(i)).start();
        }
        for (int i = 3; i < 6; i++) {
        }
        latch.countDown();
        Thread.sleep(200000);
    }


    class ReadLockRunnable implements Runnable {
        int i = 0;
        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.readLock().lock();
            System.out.println("第" + i + "个线程获取了读锁");
            System.out.println("读锁数量: " + lock.getReadLockCount() + "写锁数量: " + lock.getWriteHoldCount());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.readLock().unlock();
            System.out.println("第" + i + "个线程释放了读锁");
        }

        public ReadLockRunnable(int i) {
            this.i = i;
        }
    }

    class WriteLockRunnable implements Runnable {

        int i = 0;
        @Override
        public void run() {
            try {
                latch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            lock.writeLock().lock();
            System.out.println("第" + i + "个线程获取了写锁");
            System.out.println("读锁数量: " + lock.getReadLockCount() + "写锁数量: " + lock.getWriteHoldCount());
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            lock.writeLock().unlock();
            System.out.println("第" + i + "个线程释放了写锁");
        }

        public WriteLockRunnable(int i) {
            this.i = i;
        }
    }
}
