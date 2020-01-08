package xl.test.javabasic.io.bio.httpserver;

import xl.test.javabasic.io.bio.MyServer;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

/**
 * 支持Get方法的http服务器
 *
 * 可以获得某个目录下的文件
 * 比如 访问 127.0.0.1 可以直接获得 application.properties  因为默认返回这个文件
 * 访问 127.0.0.1/a.txt  返回 a.txt 以此类推
 *
 * @author XUAN
 * @since 2020/01/07
 */
public class GetHttpServer implements MyServer {

    private Logger logger = Logger.getLogger("GetHttpServer");

    // 固定数量线程池
    private ExecutorService pool = Executors.newFixedThreadPool(50);

    private String indexFile = "application.properties";

    private File rootDirectory;

    @Override
    public void startServer() {
        try (ServerSocket server = new ServerSocket(port);) {
            logger.info("Accepting connection on port " + server.getLocalPort());
            while (true) {
                // 每个连接一个线程
                Socket socket = server.accept();
                pool.submit(new RequestProcessor(socket));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public GetHttpServer(File rootDirectory) {
        this.rootDirectory = rootDirectory;
    }

    class RequestProcessor implements Runnable {

        Socket socket;

        private File rootDirectory;

        @Override
        public void run() {
            String root = rootDirectory.getPath();
            try (BufferedOutputStream bos = new BufferedOutputStream(socket.getOutputStream());) {
                OutputStreamWriter osw = new OutputStreamWriter(bos);
                InputStream rawInput = socket.getInputStream();
                BufferedReader br = new BufferedReader(new InputStreamReader(new BufferedInputStream(rawInput), StandardCharsets.US_ASCII));
                StringBuilder inputData = new StringBuilder();
                // 每次读取1024个字符
                String line = null;
                boolean firstLineNum = true;
                String firstLine = null;
                // 读到空行, 结束 但是这样判断结束的话POST请求怎么办??? TODO
                while ((line = br.readLine()) != null && !"".equals(line)) {
                    inputData.append(line);
                    // 获得第一行内容
                    if (firstLineNum) {
                        firstLine = line;
                        firstLineNum = false;
                    }
                }
                String data = inputData.toString();

                logger.info(socket.getRemoteSocketAddress() + "   " + data);

                String[] tokens = firstLine.split("\\s+");
                String method = tokens[0];
                String version = "";
                if ("GET".equals(method)) {
                    String fileName = tokens[1];
                    if (fileName.endsWith("/")) {
                        fileName += indexFile;
                    }
                    if (!fileName.startsWith("/")) {
                        fileName = "/" + fileName;
                    }
                    String contentType = URLConnection.getFileNameMap().getContentTypeFor(fileName);
                    if (tokens.length > 2) {
                        version = tokens[2];
                    }

                    File theFile = new File(rootDirectory, fileName.substring(1, fileName.length()));
                    // 可以读取且没有在根文档之外
                    if (theFile.canRead() && theFile.getCanonicalPath().startsWith(root)) {
                        byte[] outputData = Files.readAllBytes(theFile.toPath());
                        if (version.startsWith("HTTP/")) {
                            // HTTP连接, 发送首部
                            sendHeader(osw, "HTTP/1.0 200 OK", contentType, outputData.length);
                        }
                        // 发送文件, 可能是图像, 所以使用字节流
                        bos.write(outputData);
                        bos.flush();
                    } else {
                        // 无法找到文件
                        String body = new StringBuilder("<HTML>\r\n")
                                .append("<HEAD><TITLE>File Not Found</TITLE>\r\n")
                                .append("</HEAD>\r\n")
                                .append("<BODY>")
                                .append("<H1>HTTP Error 404: File Not Found</H1>\r\n")
                                .append("</BODY><HTML>\r\n").toString();
                        if (version.startsWith("HTTP/")) {
                            // HTTP连接, 发送首部
                            sendHeader(osw, "HTTP/1.0 404 Not Found", "text/html; Charset-utf-8", body.length());
                        }
                        osw.write(body);
                        osw.flush();
                    }
                } else {
                    // 方法不是get
                    String body = new StringBuilder("<HTML>\r\n")
                            .append("<HEAD><TITLE>Not Implemented</TITLE>\r\n")
                            .append("</HEAD>\r\n")
                            .append("<BODY>")
                            .append("<H1>HTTP Error 501: Not Implemented</H1>\r\n")
                            .append("</BODY><HTML>\r\n").toString();
                    if (version.startsWith("HTTP/")) {
                        // HTTP连接, 发送首部
                        sendHeader(osw, "HTTP/1.0 501 Not Implemented", "text/html; Charset-utf-8", body.length());
                    }
                    osw.write(body);
                    osw.flush();
                }
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

        private void sendHeader(OutputStreamWriter osw, String responseCode, String contentType, int length) throws IOException {
            String rn = "\r\n";
            osw.write(responseCode + rn);
            Date now = new Date();
            osw.write("Date: " + now + rn);
            osw.write("Server: GetServer 2.0" + rn);
            osw.write("Content-length: " + length + rn);
            osw.write("Content-type: " + contentType + rn + rn);
        }

        public RequestProcessor(Socket socket) {
            this.socket = socket;
            if (GetHttpServer.this.rootDirectory.isFile()) {
                throw new IllegalArgumentException("rootDirectory must be a directory, not a file");
            }
            try {
                rootDirectory = GetHttpServer.this.rootDirectory.getCanonicalFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
