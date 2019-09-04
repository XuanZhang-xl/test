package javabasic.spi.dubbospi;

import javabasic.spi.Contants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.ExtensionFactory;
import org.apache.dubbo.common.extension.ExtensionLoader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * dubbo spi是java spi的改进, 同时又兼容java spi
 *
 * 改进:
 * 1.java spi会一次性实例化扩展点所有实现, 浪费资源. dubbo spi只是加载配置文件中的类, 并分成不同种类缓存在内存中, 不会全部立即初始化.
 * 2. 如果扩展加载失败, 则连扩展的名称都获取不到.... 总之, 就是获取不到失败的真正原因.
 * 3. 增加了对IoC和AOP的支持, 一个扩展可以添加其他扩展.
 *
 * created by XUAN on 2019/9/4
 */
public class DubboSpiTest {

    public static void main(String[] args){
        ExtensionLoader<PrintService> extensionLoader = ExtensionLoader.getExtensionLoader(PrintService.class);
        PrintService printService = extensionLoader.getDefaultExtension();
        printService.printInfo();

        // 所有扩展类的key
        Set<String> supportedExtensions = extensionLoader.getSupportedExtensions();
        for (String supportedExtension : supportedExtensions) {
            System.out.println(supportedExtension);
        }

        System.out.println();
        System.out.println();
        System.out.println();
        // 换了一个扩展类, 如果有包装类, 那么这个扩展类还是被包装的
        URL url = URL.valueOf("test://localhost/test").addParameter(Contants.SERVICE_KEY, "printServiceImpl2");
        PrintService adaptivePrintService = ExtensionLoader.getExtensionLoader(PrintService.class).getAdaptiveExtension();
        adaptivePrintService.printAdaptive(url);


        System.out.println();
        System.out.println();
        System.out.println();
        ExtensionLoader<ExtensionFactory> loader = ExtensionLoader.getExtensionLoader(ExtensionFactory.class);
        for (String name : loader.getSupportedExtensions()) {
            // 这里不会出现adaptive,因为 getSupportedExtensions 是从 cachedClasses 里取得值, 而AdaptiveExtensionFactory有@Adaptive 不会在cachedClasses里
            System.out.println(name);
        }

    }
}
