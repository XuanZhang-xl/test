package xl.test.javabasic.io.bio.timeserver;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

/**
 * created by XUAN on 2020/1/7
 */
public class TimeTask implements Runnable {
    Socket socket;

    @Override
    public void run() {
        try {
            OutputStream os = socket.getOutputStream();
            os.write(TimeServerUtils.getRfc2229Time());
            os.flush();
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

    public TimeTask(Socket socket) {
        this.socket = socket;
    }
}
