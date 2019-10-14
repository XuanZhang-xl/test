package xl.test.algorithm.leetcode.thread;

import java.lang.reflect.Method;
import java.util.concurrent.locks.LockSupport;

/**
 * 我们提供一个类：
 *
 * class FooBar {
 *   public void foo() {
 *     for (int i = 0; i < n; i++) {
 *       print("foo");
 *     }
 *   }
 *
 *   public void bar() {
 *     for (int i = 0; i < n; i++) {
 *       print("bar");
 *     }
 *   }
 * }
 *
 * 两个不同的线程将会共用一个 FooBar 实例。其中一个线程将会调用 foo() 方法，另一个线程将会调用 bar() 方法。
 *
 * 请设计修改程序，以确保 "foobar" 被输出 n 次。
 *
 *
 *
 * 示例 1:
 *
 * 输入: n = 1
 * 输出: "foobar"
 * 解释: 这里有两个线程被异步启动。其中一个调用 foo() 方法, 另一个调用 bar() 方法，"foobar" 将被输出一次。
 *
 * 示例 2:
 *
 * 输入: n = 2
 * 输出: "foobarfoobar"
 * 解释: "foobar" 将被输出两次。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-foobar-alternately
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * created by XUAN on 2019/9/20
 */
public class AlternativelyPrint {

    private int n;

    Thread thread1 = null;

    Thread thread2 = null;

    public void foo(Runnable printFoo) throws InterruptedException {
        thread1 = Thread.currentThread();
        for (int i = 0; i < n; i++) {
            printFoo.run();
            LockSupport.unpark(thread2);
            LockSupport.park();
        }
        LockSupport.unpark(thread2);
    }

    public void bar(Runnable printBar) throws InterruptedException {
        thread2 = Thread.currentThread();
        if (thread1 == null) {
            LockSupport.park();
        }
        for (int i = 0; i < n; i++) {
            printBar.run();
            LockSupport.unpark(thread1);
            LockSupport.park();
        }
    }

    public AlternativelyPrint(int n) {
        this.n = n;
    }

    public static void main(String[] args) throws Exception {
        AlternativelyPrint alternativelyPrint = new AlternativelyPrint(10);
        Method first = AlternativelyPrint.class.getMethod("foo", Runnable.class);
        Method second = AlternativelyPrint.class.getMethod("bar", Runnable.class);
        print(alternativelyPrint, second, "bar");
        Thread.sleep(1000);
        print(alternativelyPrint, first, "foo");
    }

    private static void print(AlternativelyPrint alternativelyPrint, Method method, String data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(alternativelyPrint, new Runnable() {
                        @Override
                        public void run() {
                            System.out.print(data);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
