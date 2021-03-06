package xl.test.javabasic.io.heartbeat;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * created by zhangxuan9 on 2019/1/28
 */
public class HeartbeatServer {

    public static void main(String[] args) throws Exception {
        ServerSocket serverSocket = new ServerSocket(8888);

        Socket socket = serverSocket.accept();
        System.out.println("建立连接");
        Thread.sleep(Integer.MAX_VALUE);
        socket.close();
        serverSocket.close();
    }

}
