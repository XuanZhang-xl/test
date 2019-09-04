package javabasic.spi.dubbospi;

import javabasic.spi.dubbospi.PrintService;
import org.apache.dubbo.common.URL;

/**
 * created by XUAN on 2019/9/4
 */
public class PrintHelloXlService implements PrintService {

    @Override
    public void printInfo() {
        System.out.println("hello, xl");
    }

    @Override
    public void printAdaptive(URL url) {
        System.out.println("hello, xl");
    }
}
