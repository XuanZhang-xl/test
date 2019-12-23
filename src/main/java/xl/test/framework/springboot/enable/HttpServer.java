package xl.test.framework.springboot.enable;

/**
 * created by XUAN on 2019/12/23
 */
public class HttpServer implements Server {
    @Override
    public void start() {
        System.out.println("http服务启动");
    }

    @Override
    public void stop() {
        System.out.println("http服务关闭");
    }
}
