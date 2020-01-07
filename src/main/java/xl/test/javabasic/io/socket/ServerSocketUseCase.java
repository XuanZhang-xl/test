package xl.test.javabasic.io.socket;

import org.junit.Test;
import xl.test.javabasic.io.socket.httpserver.SimpleHttpServer;
import xl.test.javabasic.io.socket.timeserver.MultiThreadTimeServer;
import xl.test.javabasic.io.socket.timeserver.TimeServer;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * 服务器  ServerSocket 使用示例
 *
 * @see xl.test.javabasic.io.socket.objectserver
 * @see xl.test.javabasic.io.socket.timeserver
 * @see xl.test.javabasic.io.socket.httpserver
 * created by XUAN on 2020/1/7
 */
public class ServerSocketUseCase {

    @Test
    public void startTimeServer() {
        //TimeServer timeServer = new ThreadPooledTimeServer();
        //TimeServer timeServer = new SimpleTimeServer();
        TimeServer timeServer = new MultiThreadTimeServer();
        timeServer.startServer();
    }

    @Test
    public void simpleHttpServer() throws IOException {
        byte[] data = Files.readAllBytes(Paths.get("./src/main/resource/application.properties"));
        MyServer myServer = new SimpleHttpServer(data, Charset.forName("UTF-8"), "application/text", 80);
        myServer.startServer();
    }

}
