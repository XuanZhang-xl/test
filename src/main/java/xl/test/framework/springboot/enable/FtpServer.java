package xl.test.framework.springboot.enable;

/**
 * created by XUAN on 2019/12/23
 */
public class FtpServer implements Server {
    @Override
    public void start() {
        System.out.println("ftp服务启动");
    }

    @Override
    public void stop() {
        System.out.println("ftp服务关闭");
    }
}
