package xl.test.javabasic.io.nio;

import xl.test.javabasic.io.bio.stream.OutputStreamUseCase;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.SocketChannel;
import java.nio.channels.WritableByteChannel;

/**
 * 打印字符的nio版本
 *
 * @see OutputStreamUseCase#simpleOutputStreamUseCase()
 *
 * created by XUAN on 2020/1/8
 */
public class ChargenClient {

    public static int port = 19;

    public static void main(String[] args){
        //InetSocketAddress address = new InetSocketAddress("rama.poly.edu", port);
        InetSocketAddress address = new InetSocketAddress("127.0.0.1", 8081);
        try {
            SocketChannel channel = SocketChannel.open(address);
            ByteBuffer buffer = ByteBuffer.allocate(74);
            WritableByteChannel out = Channels.newChannel(System.out);
            // 写书至buffer
            while (channel.read(buffer) != -1) {
                // 写完此buffer后, 转换为读模式
                buffer.flip();
                // 读数据至buffer
                out.write(buffer);
                // 为写作准备
                buffer.clear();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
