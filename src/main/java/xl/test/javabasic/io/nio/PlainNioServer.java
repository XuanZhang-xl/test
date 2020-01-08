package xl.test.javabasic.io.nio;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * created by zhangxuan9 on 2019/1/23
 */
public class PlainNioServer {

    public static void main(String[] args) throws IOException {
        server();

        //System.out.println(SelectionKey.OP_WRITE);
        //System.out.println(SelectionKey.OP_READ);
        //System.out.println(SelectionKey.OP_WRITE | SelectionKey.OP_READ);
    }


    public static void server() throws IOException {
        // 打开服务器通道, 每调用一次open()就新打开一个, 唯一操作是接受入站连接
        ServerSocketChannel serverChannel = ServerSocketChannel.open();
        // 异步
        serverChannel.configureBlocking(false);
        // 从服务器管道获得获得服务器socket, 有且仅有一个ServerSocket
        ServerSocket socket = serverChannel.socket();
        // 绑定监听的端口
        InetSocketAddress address = new InetSocketAddress(8081);
        socket.bind(address);

        // 选选择器, 每次open()都返回新选择器
        Selector selector = Selector.open();
        // 向这个selector中注册服务器管道, 服务器通道只关心一件事, 就是是否有新的连接, 如果有, 则注册这些连接
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        final ByteBuffer msg = ByteBuffer.wrap("Hi!\r\n".getBytes());

        while (true) {
            // 这段代码貌似会阻塞
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()) {
                SelectionKey selectionKey = iterator.next();
                try {
                    if (selectionKey.isAcceptable()) {
                        ServerSocketChannel server = (ServerSocketChannel)selectionKey.channel();
                        SocketChannel client = server.accept();
                        client.configureBlocking(false);
                        // 接受客户端，并将它注册到选择器
                        client.register(selector, SelectionKey.OP_WRITE | SelectionKey.OP_READ, msg.duplicate());
                        System.out.println("Accepted connection from " + client);
                    }
                    if (selectionKey.isWritable()) {
                        SocketChannel client = (SocketChannel)selectionKey.channel();
                        ByteBuffer buffer = (ByteBuffer)selectionKey.attachment();
                        while (buffer.hasRemaining()) {
                            if (client.write(buffer) == 0) {
                                break;
                            }
                        }
                        client.close();
                        continue;
                    }
                    if (selectionKey.isReadable()) {
                        SocketChannel client = (SocketChannel)selectionKey.channel();
                        String result = "";
                        if (client.read(msg) != -1) {
                            result = result + new String(msg.array());
                        }
                        System.out.println("Accepted connection from client, " + "  data: " + result);
                        client.close();
                    }
                } catch (IOException e) {
                    selectionKey.cancel();
                    selectionKey.channel().close();
                }
                iterator.remove();
            }
        }
    }
}
