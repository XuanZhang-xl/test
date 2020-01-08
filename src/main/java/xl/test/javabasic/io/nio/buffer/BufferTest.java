package xl.test.javabasic.io.nio.buffer;

import java.nio.ByteBuffer;

/**
 * capacity：在读/写模式下都是固定的，就是我们分配的缓冲大小（容量）。
 *
 * position：类似于读/写指针，表示当前读(写)到什么位置。
 *
 * limit：在写模式下表示最多能写入多少数据，此时和capacity相同。在读模式下表示最多能读多少数据，此时和缓存中的实际数据大小相同。
 *
 * flip() 写完此buffer后, 转换为读模式
 *
 * created by zhangxuan9 on 2018/12/24
 */
public class BufferTest {

    static byte[] bytes = {1, 2, 3, 4, 5, 6, 7, 8};


    public static void main(String[] args){
        try {
            // 间接缓冲区
            ByteBuffer byteBuffer = ByteBuffer.allocate(1);
            System.out.println(byteBuffer.isDirect());
            // 直接缓冲区
            ByteBuffer byteBufferDirect = ByteBuffer.allocateDirect(1);
            System.out.println(byteBufferDirect.isDirect());

            ByteBuffer wrapBuffer = ByteBuffer.wrap(bytes);
            System.out.println(wrapBuffer.isDirect());
            System.out.println(wrapBuffer.get());
            System.out.println(wrapBuffer.get());
            System.out.println(wrapBuffer.get());

            //byte[] newBytes = {45, 46, 47, 48};
            //wrapBuffer.put(newBytes, 1, 3);

            wrapBuffer.putInt(0, Integer.MIN_VALUE - 1);

            for (int i = 0; i < bytes.length; i++) {
                System.out.print(bytes[i] + " ");
            }
            ByteBuffer slice = wrapBuffer.slice();
            System.out.println(slice.arrayOffset());
            //wrapBuffer.position(0);
            System.out.println(wrapBuffer.position());



        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
