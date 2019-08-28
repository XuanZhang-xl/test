package javabasic.io.objectserver;

import com.alibaba.fastjson.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * created by zhangxuan9 on 2019/1/28
 */
public class ObjectServer {


    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(8888);
        try (
             Socket socket = serverSocket.accept();
             InputStream inputStream = socket.getInputStream();
             OutputStream outputStream = socket.getOutputStream();
             ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
             ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
                     ) {
            for (int i = 0; i < 5; i++) {
                User user = (User) objectInputStream.readObject();
                System.out.println(System.currentTimeMillis() + "   服务端接受到: " + JSONObject.toJSONString(user));

                Thread.sleep(1000);
                User newUser = new User(i, "username" + i, "password" + i);
                objectOutputStream.writeObject(newUser);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
