package xl.test.javabasic.spi.dubbospi;

import org.apache.dubbo.common.URL;

/**
 * wrapper类, 装饰者模式
 * 如果ExtensionLoader加载扩展时, 发现扩展类包含其他扩展点作为构造函数的参数, 就会认为这个扩展类是wrapper类
 * 具体判断逻辑:
 * ```
 *     private boolean isWrapperClass(Class<?> clazz) {
 *         try {
 *             clazz.getConstructor(type);
 *             return true;
 *         } catch (NoSuchMethodException e) {
 *             return false;
 *         }
 *     }
 *```
 * 需要在配置文件中添加:
 * wrapper=xl.test.javabasic.spi.dubbospi.PrintHelloXlWrapperService
 * key(这里是wrapper)随便写都可以
 *
 * 注意: Wrapper 类不属于候选的扩展点实现
 *
 * created by XUAN on 2019/9/4
 */
public class PrintHelloXlWrapperService implements PrintService {

    private PrintService printService;

    public PrintHelloXlWrapperService(PrintService printService) {
        if (printService == null) {
            throw new IllegalArgumentException("PrintService can not be null");
        }
        this.printService = printService;
    }

    @Override
    public void printInfo() {
        printService.printInfo();
        System.out.println("I'm lu");
    }

    @Override
    public void printAdaptive(URL url) {
        printService.printInfo();
        System.out.println("I'm lu");
    }
}
