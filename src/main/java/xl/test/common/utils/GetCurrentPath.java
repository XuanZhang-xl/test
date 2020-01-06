package xl.test.common.utils;

import java.io.File;

/**
 * 打印当前的根路径
 * created by XUAN on 2020/1/6
 */
public interface GetCurrentPath {

    /**
     * 根路径
     */
    String absolutePath = new File(".").getAbsolutePath();
}
