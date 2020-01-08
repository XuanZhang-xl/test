package xl.test.javabasic.io.nio.selector;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;

/**
 * created by zhangxuan9 on 2019/1/29
 */
public class SelectorTest {

    public static void main(String[] args) throws IOException {
        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        // 非阻塞
        serverSocketChannel.configureBlocking(false);
        //serverSocketChannel.bind(new InetSocketAddress("127.0.0.1", 8888));
        ServerSocket serverSocket = serverSocketChannel.socket();
        serverSocket.bind(new InetSocketAddress(8888));

        Selector selector = Selector.open();
        // 注册
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("select=" + selector);
        System.out.println("isRegistered: " + serverSocketChannel.isRegistered());
        serverSocket.close();
        serverSocketChannel.close();


    }
}
