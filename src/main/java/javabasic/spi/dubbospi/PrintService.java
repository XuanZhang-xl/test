package javabasic.spi.dubbospi;

import javabasic.spi.Contants;
import org.apache.dubbo.common.URL;
import org.apache.dubbo.common.extension.Adaptive;
import org.apache.dubbo.common.extension.SPI;

/**
 * created by XUAN on 2019/9/4
 */
@SPI("printServiceImpl")
public interface PrintService {

    void printInfo();

    @Adaptive({Contants.SERVICE_KEY})
    void printAdaptive(URL url);
}
