package xl.test.javabasic.io.buffer;

import java.nio.ByteBuffer;

/**
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
