package javabasic.spi.javaspi;

import java.util.ServiceLoader;

/**
 * API：API(Application Programming Interface)表示应用程序编程接口。SPI：SPI(Service Provider Interface)表示服务提供商接口。
 *
 * API与SPI的关系
 * 框架提供API及其实现，框架在实现过程中提供SPI回调机制。SPI是框架的扩展点。如果使用框架方要扩展框架，可以自己实现SPI并注入框架，于是框架使用方其实也是一个服务提供商。
 *
 * java spi的具体约定如下 ：
 *
 * 当服务的提供者，提供了服务接口的一种实现之后，在jar包的META-INF/services/目录里同时创建一个以服务接口命名的文件。
 * 该文件里就是实现该服务接口的具体实现类。而当外部程序装配这个模块的时候，就能通过该jar包META-INF/services/里的配置文件找到具体的实现类名，并装载实例化，完成模块的注入。
 * 基于这样一个约定就能很好的找到服务接口的实现类，而不需要再代码里指定。jdk提供服务实现查找的一个工具类：java.util.ServiceLoader。
 * created by XUAN on 2019/9/4
 */
public class JavaSpiTest {

    public static void main(String[] args){
        ServiceLoader<PrintService> printServices = ServiceLoader.load(PrintService.class);
        for (PrintService printService : printServices) {
            printService.printInfo();
        }
    }
}
