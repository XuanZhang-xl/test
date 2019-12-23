package xl.test.framework.springboot.enable;

/**
 * 定义服务的操作
 * created by XUAN on 2019/12/23
 */
public interface Server {

    /**
     * 服务启动
     */
    void start();

    /**
     * 服务关闭
     */
    void stop();

    /**
     * 服务类型
     */
    enum ServerType {
        // HTTP服务
        HTTP,
        // FTP服务
        FTP;
    }
}
