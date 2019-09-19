package javabasic.concurrent;

import org.junit.Test;

import java.util.concurrent.locks.LockSupport;

/**
 * 使用@Test后, 就算有非守护线程还在运行, 只要主线程退出, jvm还是会退出, 目测是因为junit的调用代码中有System.exit(0)导致的
 * 所以这里必须用main方法
 *
 * LockSupport jvm中每个线程维护一个Parker类型对象, 其中有一个count变量
 *
 * 调用park(),   如果看到count大于0(并原子性得将count设为0), 则说明之前调用过unpark方法，所以park方法直接返回。这也是unpark能先于park调用的原因
 *              如果看到count等于0, 则阻塞
 * 调用unpark()  将count设为1
 *              如果看到原count小于1  则 唤醒被阻塞的线程
 *              如果看到原count等于1  则, 啥也不干
 *
 * 参考: https://www.cnblogs.com/qingquanzi/p/8228422.html
 *
 * created by XUAN on 2019/9/19
 */
public class LockSupportTest {

    public static void main(String[] args)throws Exception {
    //@Test
    //public void testLockSupport() throws Exception {
        System.out.println(Thread.currentThread().isDaemon());
        System.out.println(Thread.currentThread().getName());

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                System.out.println("新建thread:" + Thread.currentThread().isDaemon());
                LockSupport.park();
                try {
                    System.out.println("something1");
                    Thread.sleep(3000);
                    System.out.println("something2");
                    Thread.sleep(3000);
                    System.out.println("something3");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

        thread.start();
        Thread.sleep(1000);
        LockSupport.unpark(null);
        System.out.println("主线程end");
    }


}
