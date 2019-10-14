package xl.test.javabasic.io.channel;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.channels.FileChannel;

/**
 * created by zhangxuan9 on 2019/1/25
 */
public class RandomAccessTest {

    public static void main(String[] args) throws IOException {
        try (RandomAccessFile file1 = new RandomAccessFile("C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\resource\\a.txt", "rw");
             RandomAccessFile file2 = new RandomAccessFile("C:\\Users\\zhangxuan9\\IdeaProjects\\test\\src\\main\\resource\\b.txt", "rw");


             FileChannel channel1 = file1.getChannel();
             FileChannel channel2 = file2.getChannel();
             ) {
            channel2.position(3);

            // 从channel2 的第4位开始数3位传输到channel1的第3位开始数3位的地方
            long l = channel1.transferFrom(channel2, 2, 30);
            System.out.println(l);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
