package xl.test.javabasic.io.socket.httpserver;

import xl.test.javabasic.io.socket.MyServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * 最简单的http服务器, 固定传送一个文件内容
 *
 * created by XUAN on 2020/1/7
 */
public class SimpleHttpServer implements MyServer {

    Logger logger = Logger.getLogger("SimpleHttpServer");

    private byte[] content = null;
    private byte[] header = null;
    private int port = 0;
    private Charset encoding = null;

    // 固定数量线程池
    private ExecutorService pool = Executors.newFixedThreadPool(50);

    public SimpleHttpServer(String data, String encoding, String mimeType, int port) {
        this(data.getBytes(), Charset.forName(encoding), mimeType, port);
    }

    public SimpleHttpServer(byte[] data, Charset encoding, String mimeType, int port) {
        this.content = data;
        this.port = port;
        this.encoding = encoding;
        String header = "HTTP/1.0 200 OK\r\n" +
                "Server: OneFile 2.0\r\n" +
                "Content-length: " + this.content.length + "\r\n" +
                // 这里有两个换行符, 代表head结束, body开始
                "Content-type: " + mimeType + "; charset=" + encoding + "\r\n\r\n";
        this.header = header.getBytes(StandardCharsets.US_ASCII);
    }

    @Override
    public void startServer() {
        try (ServerSocket serverSocket = new ServerSocket(port)) {
            logger.info("Accepting connection on port " + serverSocket.getLocalPort());
            logger.info("Data to be sent: " + new String(content, encoding));
            while (true) {
                try {
                    Socket socket = serverSocket.accept();
                    System.out.println("accept: " + socket);
                    // 新线程交给线程池处理
                    pool.submit(new HttpHandler(socket));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class HttpHandler implements Runnable {

        private Socket socket;

        @Override
        public void run() {
            try {
                BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());
                BufferedInputStream bis = new BufferedInputStream(socket.getInputStream());
                StringBuilder request = new StringBuilder(80);
                while (true) {
                    int c = bis.read();
                    // 只读取第一行
                    // 如果没有\r\n 且传的不是文件, 则在这里卡死, 永远等待下一个字符
                    if (c == '\r' || c== '\n' || c == -1) {
                        break;
                    }
                    request.append((char)c);
                }

                System.out.println("received: " + request);
                // HTTP/1.0或之后的版本, 则发送一个MIME首部
                if (request.toString().indexOf("HTTP/") != -1) {
                    bos.write(header);
                }
                bos.write(content);
                bos.flush();
                System.out.println("HttpHandler response");
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public HttpHandler(Socket socket) {
            this.socket = socket;
        }
    }
}
