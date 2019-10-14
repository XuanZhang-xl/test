package xl.test.javabasic.spi.dubbospi;

import org.apache.dubbo.common.URL;

/**
 * created by XUAN on 2019/9/4
 */
public class PrintHelloWorldService implements PrintService {

    @Override
    public void printInfo() {
        System.out.println("hello, world");
    }

    @Override
    public void printAdaptive(URL url) {
        System.out.println("hello, world");
    }
}
