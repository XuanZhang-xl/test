package xl.test.javabasic.io.channel;

import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * created by zhangxuan9 on 2018/12/29
 */
public class FileChannelTest {


    public static FileInputStream fis;
    public static FileChannel fc;


    public static void main(String[] args){
        try {
            fis = new FileInputStream(new File("./src\\main\\resource\\a.txt"));
            fc = fis.getChannel();
            fc.position(2);
            ByteBuffer byteBuffer = ByteBuffer.allocate(5);
            byteBuffer.position(1);
            fc.read(byteBuffer);

            byte[] array = byteBuffer.array();
            for (byte b : array) {
                System.out.print(((char)b) + " " + b);
                System.out.println();
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            System.out.println("finally");
        }
    }

    @Test
    public void testRead1() throws Exception {





    }


    /**
     * 测试批量写入
     * @throws Exception
     */
    @Test
    public void testBatchWrite1() throws Exception {
        FileOutputStream fos = new FileOutputStream(new File("./src\\main\\resource\\a.txt"));
        FileChannel fileChannel = fos.getChannel();
        ByteBuffer[] byteBuffers = {ByteBuffer.wrap("abcde".getBytes()), ByteBuffer.wrap("12345".getBytes())};
        fileChannel.write(ByteBuffer.wrap("qqqqq".getBytes()));
        fileChannel.position(1);
        fileChannel.write(byteBuffers, 1 ,1);
        fileChannel.close();
    }


    /**
     * 测试批量读取
     * @throws Exception
     */
    @Test
    public void testBatchRead1() throws Exception {
        FileInputStream fis = new FileInputStream(new File("./src\\main\\resource\\b.txt"));
        FileChannel channel = fis.getChannel();
        ByteBuffer[] byteBuffers = {ByteBuffer.wrap("abcde".getBytes()), ByteBuffer.wrap("12345".getBytes())};

        long read = channel.read(byteBuffers, 0, 2);
        System.out.println(read);
        for (ByteBuffer byteBuffer : byteBuffers) {
            byte[] array = byteBuffer.array();
            for (byte b : array) {
                System.out.print((char)b);
            }
            System.out.println();
            byteBuffer.clear();
        }
        read = channel.read(byteBuffers, 0, 2);
        System.out.println(read);
        for (ByteBuffer byteBuffer : byteBuffers) {
            byte[] array = byteBuffer.array();
            for (byte b : array) {
                System.out.print((char)b);
            }
            System.out.println();
            byteBuffer.clear();
        }
        read = channel.read(byteBuffers, 0, 2);
        System.out.println(read);
        for (ByteBuffer byteBuffer : byteBuffers) {
            byte[] array = byteBuffer.array();
            for (byte b : array) {
                System.out.print((char)b);
            }
            System.out.println();
            byteBuffer.clear();
        }
    }
}
