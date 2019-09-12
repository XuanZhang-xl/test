package javabasic.spi.javaspi;

/**
 * created by XUAN on 2019/9/4
 */
public class PrintHelloXlService implements PrintService {

    @Override
    public void printInfo() {
        System.out.println("hello, xl");
    }
}
