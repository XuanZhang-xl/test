package algorithm.leetcode.thread;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.LockSupport;

/**
 *
 * 我们提供了一个类：
 *
 * public class Foo {
 *   public void one() { print("one"); }
 *   public void two() { print("two"); }
 *   public void three() { print("three"); }
 * }
 *
 * 三个不同的线程将会共用一个 Foo 实例。
 *
 *     线程 A 将会调用 one() 方法
 *     线程 B 将会调用 two() 方法
 *     线程 C 将会调用 three() 方法
 *
 * 请设计修改程序，以确保 two() 方法在 one() 方法之后被执行，three() 方法在 two() 方法之后被执行。
 *
 *
 *
 * 示例 1:
 *
 * 输入: [1,2,3]
 * 输出: "onetwothree"
 * 解释:
 * 有三个线程会被异步启动。
 * 输入 [1,2,3] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 two() 方法，线程 C 将会调用 three() 方法。
 * 正确的输出是 "onetwothree"。
 *
 * 示例 2:
 *
 * 输入: [1,3,2]
 * 输出: "onetwothree"
 * 解释:
 * 输入 [1,3,2] 表示线程 A 将会调用 one() 方法，线程 B 将会调用 three() 方法，线程 C 将会调用 two() 方法。
 * 正确的输出是 "onetwothree"。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/print-in-order
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * created by XUAN on 2019/9/19
 */
public class PrintSerialization {

    AtomicInteger count = new AtomicInteger(0);

    Thread thread1 = null;

    Thread thread2 = null;

    Thread thread3 = null;

    public void first(Runnable printFirst) throws InterruptedException {
        System.out.println("一");
        thread1 = Thread.currentThread();
        if (count.get() < 2) {
            count.getAndIncrement();
            LockSupport.park();
        }
        printFirst.run();
        LockSupport.unpark(thread2);
    }

    public void second(Runnable printSecond) throws InterruptedException {
        System.out.println("二");
        thread2 = Thread.currentThread();
        if (count.get() < 2) {
            count.getAndIncrement();
        } else {
            LockSupport.unpark(thread1);
        }
        LockSupport.park();
        printSecond.run();
        LockSupport.unpark(thread3);
    }

    public void third(Runnable printThird) throws InterruptedException {
        System.out.println("三");
        thread3 = Thread.currentThread();
        if (count.get() < 2) {
            count.getAndIncrement();
        } else {
            LockSupport.unpark(thread1);
        }
        LockSupport.park();
        printThird.run();
    }


    public static void main(String[] args) throws NoSuchMethodException {
        PrintSerialization printSerialization = new PrintSerialization();
        Method first = PrintSerialization.class.getMethod("first", Runnable.class);
        Method second = PrintSerialization.class.getMethod("second", Runnable.class);
        Method third = PrintSerialization.class.getMethod("third", Runnable.class);
        print(printSerialization, first, 1);
        print(printSerialization, second, 2);
        print(printSerialization, third, 3);
    }

    public static void  print(PrintSerialization printSerialization, Method method, int i) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    method.invoke(printSerialization, new Runnable() {
                        @Override
                        public void run() {
                            System.out.println(i);
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
