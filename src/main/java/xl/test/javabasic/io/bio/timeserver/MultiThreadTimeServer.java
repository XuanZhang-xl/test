package xl.test.javabasic.io.bio.timeserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * created by XUAN on 2020/1/7
 */
public class MultiThreadTimeServer implements TimeServer{

    @Override
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // 非阻塞, 但是新开线程消耗性能
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println(socket);

                    Thread task = new Thread(new TimeTask(socket));
                    task.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
