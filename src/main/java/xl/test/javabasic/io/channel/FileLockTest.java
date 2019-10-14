package xl.test.javabasic.io.channel;

import org.junit.Test;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

/**
 * created by zhangxuan9 on 2019/1/25
 */
public class FileLockTest {

    @Test
    public void asynClose() throws IOException, InterruptedException {
        FileOutputStream fos = new FileOutputStream("C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\resource\\a.txt");
        final FileChannel channel = fos.getChannel();
        Thread a = new Thread() {
            @Override
            public void run() {
                try {
                    channel.lock(1, 2, false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread b = new Thread() {
            @Override
            public void run() {
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };
        a.start();
        Thread.sleep(1);
        b.start();
        Thread.sleep(1000);
        fos.close();
        channel.close();




    }

    public void fileLock() {

    }

}
