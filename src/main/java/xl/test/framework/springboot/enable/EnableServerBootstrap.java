package xl.test.framework.springboot.enable;

import org.springframework.boot.autoconfigure.AutoConfigurationPackages;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @Enable 模块的应用示例
 *
 * 分别使用了 ImportSelector与ImportBeanDefinitionRegistrar实现
 *
 * @EnableServer(type = Server.ServerType.HTTP) 启动{@link HttpServer}
 * @EnableServer(type = Server.ServerType.FTP)  启动{@link FtpServer}
 *
 * 个人认为这种装配方式在装配大型模块的时候比较有用, 大型模块, 就是要注册很多bean, 每个bean的注册流程还比较复杂的模块, 比如Servlet与WebFlux.
 * 如果仅仅是选择一个bean, 那就是杀鸡用牛刀.
 *
 * created by XUAN on 2019/12/23
 */
@Configuration
@EnableServer(type = Server.ServerType.FTP)
@EnableAutoConfiguration
public class EnableServerBootstrap {

    public static void main(String[] args){
        AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
        // 注册当前引导类
        applicationContext.register(EnableServerBootstrap.class);
        // 启动上下文
        applicationContext.refresh();
        //获得server bean
        Server server = applicationContext.getBean(Server.class);
        // 启动服务器
        server.start();
        //关闭服务器
        server.stop();

        // 获得当前扫描路径
        List<String> basePackages = AutoConfigurationPackages.get(applicationContext);
        basePackages.forEach(System.out::println);
        // 关闭上下文
        applicationContext.close();
    }
}
