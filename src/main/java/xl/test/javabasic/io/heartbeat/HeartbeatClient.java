package xl.test.javabasic.io.heartbeat;

import java.io.IOException;
import java.net.Socket;

/**
 * created by zhangxuan9 on 2019/1/28
 */
public class HeartbeatClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Socket socket = new Socket("127.0.0.1", 8888);

        int count = 0;
        while (true) {
            socket.sendUrgentData(1);
            count++;
            System.out.println("执行了" + count + "次嗅探");
            Thread.sleep(1000);
        }
    }

}
