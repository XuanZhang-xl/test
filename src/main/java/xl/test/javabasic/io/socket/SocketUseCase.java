package xl.test.javabasic.io.socket;

import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
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
        String website = "time.nist.gov";

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

}
