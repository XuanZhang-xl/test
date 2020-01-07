package xl.test.javabasic.io.socket;

/**
 * created by XUAN on 2020/1/7
 */
public interface MyServer {

    // 服务默认监听端口
    public int port = 80;

    /**
     * 开启服务
     */
    public void startServer();
}
