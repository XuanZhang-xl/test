package xl.test.javabasic.io.socket;

import org.junit.Test;
import xl.test.javabasic.io.socket.httpserver.GetHttpServer;
import xl.test.javabasic.io.socket.httpserver.SimpleHttpServer;
import xl.test.javabasic.io.socket.timeserver.ThreadPooledTimeServer;
import xl.test.javabasic.io.socket.timeserver.TimeServer;

import java.io.File;
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

    /**
     * 启动时间服务器
     */
    @Test
    public void startTimeServer() {
        TimeServer timeServer = new ThreadPooledTimeServer();
        //TimeServer timeServer = new SimpleTimeServer();
        //TimeServer timeServer = new MultiThreadTimeServer();
        timeServer.startServer();
    }

    /**
     * 启动简单HTTP服务器
     * @throws IOException
     */
    @Test
    public void simpleHttpServer() throws IOException {
        byte[] data = Files.readAllBytes(Paths.get("./src/main/resource/application.properties"));
        MyServer myServer = new SimpleHttpServer(data, Charset.forName("UTF-8"), "application/text", 80);
        myServer.startServer();
    }

    /**
     * 启动http get服务器
     * @throws IOException
     */
    @Test
    public void getHttpServer() throws IOException {
        MyServer myServer = new GetHttpServer(new File("./src/main/resource"));
        myServer.startServer();
    }

}
