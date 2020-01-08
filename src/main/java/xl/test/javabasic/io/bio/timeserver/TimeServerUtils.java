package xl.test.javabasic.io.bio.timeserver;

/**
 * created by XUAN on 2020/1/7
 */
public class TimeServerUtils {

    // 1900-1970秒数
    private static long differenceBetweenEpochs = 2208988800l;

    /**
     * 获得 Rfc2229 定义的时间, 就是 1900年到现在的秒数
     * @return 二进制形式的1900年到现在的秒数
     */
    public static byte[] getRfc2229Time() {
        long msSince1970 = System.currentTimeMillis();
        long secondSince1970 = msSince1970 / 1000;
        long secondSince1900 = secondSince1970 + differenceBetweenEpochs;
        byte[] time = new byte[4];
        time[0] = (byte)((secondSince1900 & 0x00000000FF000000) >> 24);
        time[1] = (byte)((secondSince1900 & 0x0000000000FF0000) >> 16);
        time[2] = (byte)((secondSince1900 & 0x000000000000FF00) >> 8);
        time[3] = (byte)((secondSince1900 & 0x00000000000000FF));
        return time;
    }


}
