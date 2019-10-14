package xl.test.algorithm.hyperloglog;

/**
 * created by XUAN on 2019/8/23
 */
public class BitKeeper {

    // 最大低位0个数
    private int maxBits;

    public void random (long value) {
        int bits = lowZeros(value);
        if (bits > maxBits) {
            maxBits = bits;
        }
    }

    private int lowZeros(long value) {
        int i = 1;
        // Long型最长64位
        while (i < 64) {
            if (value >> i << i != value) {
                break;
            }
            i++;
        }
        return i - 1;
    }

    public int getMaxBits() {
        return maxBits;
    }
}
