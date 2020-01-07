package xl.test.javabasic.io.socket.timeserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * created by XUAN on 2020/1/7
 */
public class SimpleTimeServer implements TimeServer {

    @Override
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (true) {
                // 阻塞, 直到接受到链接
                // 坏处是如果有一个连接阻塞, 将阻塞整个服务器
                try (Socket socket = serverSocket.accept()) {
                    System.out.println(socket);
                    OutputStream os = socket.getOutputStream();

                    os.write(TimeServerUtils.getRfc2229Time());
                    os.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
