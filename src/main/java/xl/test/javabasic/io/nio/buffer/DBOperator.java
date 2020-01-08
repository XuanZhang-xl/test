package xl.test.javabasic.io.nio.buffer;

/**
 * created by zhangxuan9 on 2018/12/29
 */
public class DBOperator implements AutoCloseable {

    @Override
    public void close() throws Exception {
        System.out.println("AutoCloseable的close方法已被调用");
    }
}
