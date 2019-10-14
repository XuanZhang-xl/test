package xl.test.javabasic.spi.javaspi;

/**
 * created by XUAN on 2019/9/4
 */
public class PrintHelloWorldService implements PrintService {

    @Override
    public void printInfo() {
        System.out.println("hello, world");
    }
}
