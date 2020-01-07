package xl.test.javabasic.io.socket;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketAddress;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 客户端 Socket 使用示例
 * created by XUAN on 2020/1/6
 */
public class SocketUseCase {

    /**
     * 获得RFC 867中定义的时间格式
     * @throws IOException
     */
    @Test
    public void simpleSocket() throws IOException {
        // 美国国家标准与技术研究院, 端口13, 时间格式在RFC 867中定义, 和JSR不一样
        String website = "time.nist.gov";
        try(Socket socket = new Socket(website, 13);) {
            socket.setSoTimeout(15000);
            InputStream inputStream = socket.getInputStream();
            StringBuilder time = new StringBuilder();
            InputStreamReader reader = new InputStreamReader(inputStream, StandardCharsets.US_ASCII);
            int c;
            while ((c = reader.read()) != -1) {
                time.append((char)c);
            }
            System.out.println(time);
        }
    }

    /**
     * 将RFC 868中定义的事件转换为java时间
     * @throws IOException
     */
    @Test
    public void changeRfc867TimeToJavaTime() throws IOException {
        //String website = "time.nist.gov";
        String website = "127.0.0.1";

        // RFC 868中定定义的时间协议,发送的是自格林尼治时间1900年1月1日子夜之后经过的秒数, 而java起始于1970年, 这是这70年间的秒数
        long differenceBetweenEpochs = 2208988800L;


        // 37端口, 对应RFC 868中定定义的时间协议
        try(Socket socket = new Socket(website, 37);) {
            socket.setSoTimeout(15000);
            // 这个url返回的是数字, 所以用InputStream读取
            InputStream raw = socket.getInputStream();
            long secondSince1900 = 0L;
            for (int i = 0; i < 4; i++) {
                // 由于java没有无符号整形, 所以只能每次读取一个字节, 放入long中, 一共4字节
                secondSince1900 = (secondSince1900 << 8) | raw.read();
            }
            long secondSince1970 = secondSince1900 - differenceBetweenEpochs;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            System.out.println(sdf.format(new Date(secondSince1970 * 1000)));
        }
    }

    /**
     * dict是一个简单的双向TCP协议, 由RFC 2229定义
     *
     * 就是查字典
     *
     * 连接不上, 超时
     *
     * @throws IOException
     */
    @Test
    public void dict() {
        try (Socket socket = new Socket("dict.org", 2628)) {
            socket.setSoTimeout(15000);
            // 获得输出流
            OutputStream os = socket.getOutputStream();
            // 变为字符流, 使用UTF_8编码表
            OutputStreamWriter osw = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            // 写入命令, 询问 英语-拉丁语字典 中对gold的定义
            osw.write("DEFINE eng-lat gold\r\n");
            osw.flush();

            // 获得响应
            InputStream is = socket.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(is, StandardCharsets.UTF_8));
            String line = null;
            while ((line = br.readLine()) != null) {
                if (line.startsWith("250")) {
                    // 相应以3位数的状态码开头, 250表示???
                    return;
                } else if (line.startsWith("552")) {
                    System.out.println("No definition found for gold");
                    return;
                } else if (line.matches("\\d\\d\\d .*")) {
                    // 其他状态码
                    continue;
                } else if (line.trim().equals(".")) {
                    // 结束
                    return;
                } else {
                    System.out.println(line);
                }
            }
            osw.write("quit\r\n");
            osw.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获得本地及远程的端口信息
     */
    @Test
    public void getLocalAndRemoteAddress() {


        List<Socket> sockets = new ArrayList<>();
        try {
            sockets.add(new Socket("dict.org", 2628));
            sockets.add(new Socket("www.baidu.com", 80));

            for (Socket socket : sockets) {
                socket.setSoTimeout(15000);
                SocketAddress localSocketAddress = socket.getLocalSocketAddress();
                System.out.println("localAddress: " + localSocketAddress);
                SocketAddress remoteSocketAddress = socket.getRemoteSocketAddress();
                System.out.println("SocketAddress: " + remoteSocketAddress);
                System.out.println("connected to " + socket.getInetAddress() + " on port " + socket.getPort() + " from port " + socket.getLocalPort() + " of" + socket.getLocalAddress());

                System.out.println();
                System.out.println();
            }


        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (!sockets.isEmpty()) {
                for (Socket socket : sockets) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    /**
     * 直接这样读取baidu是读不到的, 为什么?
     *
     *
     * @throws IOException
     */
    @Test
    public void getDataFromSocket() throws IOException {
        Socket socket = null;
        try {
            //socket = new Socket("www.baidu.com", 80);
            socket = new Socket("127.0.0.1", 80);
            socket.setSoTimeout(50000);
            //BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //String data = null;
            //while ((data = br.readLine()) != null) {
            //    System.out.println(data);
            //}

            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write("HTTP/1.1\r\n");
            osw.flush();
            InputStreamReader reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = reader.read()) != -1) {
                sb.append((char)c);
            }
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

    @Test
    public void getHttpClient() throws IOException {
        Socket socket = null;
        try {
            socket = new Socket("127.0.0.1", 80);
            socket.setSoTimeout(50000);

            OutputStreamWriter osw = new OutputStreamWriter(socket.getOutputStream());
            osw.write("GET application.properties HTTP/1.1\r\n");
            osw.flush();
            InputStreamReader reader = new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8);
            int c;
            StringBuilder sb = new StringBuilder();
            while ((c = reader.read()) != -1) {
                sb.append((char)c);
            }
            System.out.println(sb);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (socket != null) {
                socket.close();
            }
        }
    }

}
