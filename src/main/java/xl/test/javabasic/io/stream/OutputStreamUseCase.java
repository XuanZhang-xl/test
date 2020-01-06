package xl.test.javabasic.io.stream;

import org.junit.Test;

import java.io.IOException;
import java.io.OutputStream;

/**
 * 输出流用例
 * created by XUAN on 2020/1/6
 */
public class OutputStreamUseCase {


    @Test
    public void simpleOutputStreamUseCase() throws Exception {
        generateCharacters(System.out, true);
    }

    /**
     * OutputStream#write(), 接受一个int类型输入, 也就是写入一个字符, 范围是0~255, 请避免超过这个范围
     *
     * 这里每次向OutputStream写入一字节, 字节从33-126之间循环
     *
     * @param out
     * @throws IOException
     */
    public static void generateCharacters(OutputStream out) throws Exception {
        generateCharacters(out, false);
    }

    public static void generateCharacters(OutputStream out, boolean useCache) throws Exception {
        // 其实字符
        int firstPrintableCharacter = 33;
        // 可显示的ASCII字符是33~126之间, 一共94个字符
        int numberOfPrintableCharacters = 94;
        // 字符串长度
        int numberOfCharacterPerLine = 72;

        byte[] cache = null;
        if (useCache) {
            // +2 为回车和换行
            cache = new byte[numberOfCharacterPerLine + 2];
        }

        int start = firstPrintableCharacter;

        while (true) {
            if (useCache) {
                for (int i = start; i < start + numberOfCharacterPerLine; i++) {
                    // 取余确保能一直循环
                    cache[i - start] = (byte) ((i - firstPrintableCharacter ) % numberOfPrintableCharacters + firstPrintableCharacter);
                }
                cache[numberOfCharacterPerLine] = '\r';
                cache[numberOfCharacterPerLine + 1] = '\n';
                out.write(cache);
            } else {
                for (int i = start; i < start + numberOfCharacterPerLine; i++) {
                    // 取余确保能一直循环
                    out.write((i - firstPrintableCharacter ) % numberOfPrintableCharacters + firstPrintableCharacter);
                }
                // 回车
                out.write('\r');
                // 换行
                out.write('\n');
            }
            Thread.sleep(1000);
            // 每次偏移一个字符
            start = (start + 1 - firstPrintableCharacter) % numberOfPrintableCharacters + firstPrintableCharacter;
        }
    }

}
