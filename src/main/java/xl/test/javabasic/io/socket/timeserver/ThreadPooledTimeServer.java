package xl.test.javabasic.io.socket.timeserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * created by XUAN on 2020/1/7
 */
public class ThreadPooledTimeServer implements TimeServer {

    // 固定数量线程池
    private ExecutorService pool = Executors.newFixedThreadPool(50);

    @Override
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println(socket);
                    // 新线程交给线程池处理
                    pool.submit(new TimeTask(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
