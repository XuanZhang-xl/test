package xl.test.javabasic.concurrent;

import org.junit.Test;

import java.util.Arrays;
import java.util.Random;

/**
 * created by XUAN on 2020/1/6
 */
public class ThreadUseCase {

    /**
     * Thread#join  等待被调用线程结束后,继续当前线程的运行
     */
    @Test
    public void join() {
        Random random = new Random();
        int[] array = new int[10000];
        for (int i = 0; i < array.length; i++) {
            array[i] = random.nextInt(array.length);
        }
        SortThread t = new SortThread(array);
        t.start();

        try {
            t.join();
            System.out.println("min: " + array[0]);
            System.out.println("max: " + array[array.length - 1]);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


    static class SortThread extends Thread {

        int[] array = null;

        public SortThread(int[] array) {
            this.array = array;
        }

        @Override
        public void run() {
            Arrays.sort(array);
        }
    }


}
