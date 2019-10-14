package xl.test.javabasic.io.objectserver;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.Socket;

/**
 * created by zhangxuan9 on 2019/1/28
 */
public class ObjectClient {

    public static void main(String[] args) throws IOException {


        try (
             Socket socket = new Socket("127.0.0.1", 8888);
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        ) {
            for (int i = 0; i < 5; i++) {
                User newUser = new User(i, "username" + i, "password" + i);
                objectOutputStream.writeObject(newUser);
                Thread.sleep(1000);
                User user = (User) objectInputStream.readObject();
                System.out.println(System.currentTimeMillis() + "     客户端接受到: " + JSONObject.toJSONString(user));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
