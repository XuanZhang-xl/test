package algorithm.heap;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * created by XUAN on 2019/3/11
 */
public class ThreadHolder {
    public static Long[] items = new Long[]{113L, 104L, 6L, 129L, 210L, 9L, 6L, 15L, 1826L, 362L, 31L};
    public static long now = System.currentTimeMillis();
    private Thread thread;

    final Lock lock = new ReentrantLock();
    final Condition condition = lock.newCondition();

    // 待优化
    ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static BinaryHeap<Task>  heap = null;
    static {
        List<Task> tasks = new ArrayList<Task>();
        for (int i = 0; i < items.length; i++) {
            Task task = new Task();
            task.setOrderId(String.valueOf(i));
            task.setTime(now + items[i]);
            tasks.add(task);
        }
        heap = new BinaryHeap<Task>(tasks);
    }


    public void addTask (Task task) {
        if (task != null) {
            lock.lock();
            try {
                heap.insert(task);
                condition.signal();
            } finally {
                lock.unlock();
            }
        }
    }


    @Test
    public void test() throws InterruptedException {

        this.thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    System.out.println("循环开始");
                    lock.lock();
                    try {
                        Task task = null;
                        while ((task = heap.findMin()) == null) {
                            System.out.println(Thread.currentThread().getName() + "永远睡着, 等待唤醒");
                            condition.await();
                        }
                        if (System.currentTimeMillis() >= task.getTime()) {
                            task = heap.deleteMin();
                            System.out.println(task.getOrderId() + " 任务已提交");
                            executorService.submit(new TaskContainer(task));
                        } else {
                            System.out.println("将睡" + (task.getTime() - System.currentTimeMillis()));
                            condition.await(task.getTime() - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
                        }
                    } catch (InterruptedException e) {
                        System.out.println("default-thread被打断");
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }, "default-thread");
        this.thread.start();

        Thread.sleep(10000);
        Task task1 = new Task(System.currentTimeMillis() + 10000L, String.valueOf(items.length + 1));
        Task task2 = new Task(System.currentTimeMillis() + 18742L, String.valueOf(items.length + 2));
        Task task3 = new Task(System.currentTimeMillis() + 12343L, String.valueOf(items.length + 3));
        Task task4 = new Task(System.currentTimeMillis() + 10000L, String.valueOf(items.length + 4));
        addTask(task1);
        addTask(task3);
        Thread.sleep(20000);
        addTask(task2);
        addTask(task4);

        this.thread.join();
        System.out.println("测试结束");
    }
}
