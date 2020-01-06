package xl.test.encryption;

import javax.xml.bind.DatatypeConverter;
import java.io.FileInputStream;
import java.security.DigestInputStream;
import java.security.MessageDigest;

/**
 * 加密线程
 * created by XUAN on 2020/1/6
 */
public class DigestThread extends Thread {

    private String fileName;

    public DigestThread(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            // 读取文件
            FileInputStream fis = new FileInputStream(fileName);
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            // 使用SHA-256加密, 获得摘要
            DigestInputStream din = new DigestInputStream(fis, sha);
            // 读取摘要
            while (din.read() != -1);
            din.close();
            byte[] digest = sha.digest();

            StringBuilder sb = new StringBuilder(fileName);
            sb.append(": ");
            // 已16进制方式显示摘要
            sb.append(DatatypeConverter.printHexBinary(digest));
            System.out.println(sb);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws InterruptedException {
        DigestThread digestThread = new DigestThread("./src/main/resource/a.txt");
        digestThread.start();
        Thread.sleep(1000);
    }
}
