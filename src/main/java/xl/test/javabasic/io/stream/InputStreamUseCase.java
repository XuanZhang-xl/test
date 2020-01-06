package xl.test.javabasic.io.stream;


import org.junit.Test;
import xl.test.common.utils.GetCurrentPath;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * 输入流用例
 * created by XUAN on 2020/1/6
 */
public class InputStreamUseCase implements GetCurrentPath {

    static {
        System.out.println("当前根路径: " + absolutePath);
    }

    /**
     * BufferedInputStream extends FilterInputStream 过滤器流
     * 也就是有一个
     *  protected volatile InputStream in;
     * 这样的成员变量, 底层操作都是这个in变量在做, 自己只做自己的功能, 如Buffer, 加解密, 压缩解压缩等
     *
     * 也就是传说中的装饰器模式
     *
     *
     * @throws FileNotFoundException
     */
    @Test
    public void fileInputStream() throws IOException {
        FileInputStream fis = new FileInputStream("./src/main/resource/application.properties");
        BufferedInputStream bin = new BufferedInputStream(fis);
        byte[] data = new byte[bin.available()];
        int readCount = bin.read(data, 0, bin.available());
        System.out.println("读取到了" + readCount + "个字符");
        System.out.println("数据为: \r\n" + new String(data));
    }

    @Test
    public void dataInputStream() throws IOException {
        FileInputStream fis = new FileInputStream("./src/main/resource/b.txt");
        BufferedInputStream bin = new BufferedInputStream(fis);
        DataInputStream dis = new DataInputStream(bin);
        System.out.println(dis.readInt());

    }

}
