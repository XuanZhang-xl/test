package algorithm.hyperloglog;

import java.util.concurrent.ThreadLocalRandom;

/**
 * created by XUAN on 2019/8/23
 */
public class Experiment {

    // 实际个数, 结果越靠近这个值越准确
    private int n;

    // 桶的个数
    private int k;

    // 桶
    private BitKeeper[] bitKeepers;

    public Experiment(int n) {
        this(n, 1024);
    }

    public Experiment(int n, int k) {
        this.n = n;
        this.k = k;
        this.bitKeepers = new BitKeeper[k];
        for (int i = 0; i < k; i++) {
            this.bitKeepers[i] = new BitKeeper();
        }
    }

    public void work () {
        for (int i = 0; i < this.n; i++) {
            // 生成一个最大为 Integer.MAX - 1的数字
            long m = ThreadLocalRandom.current().nextLong(1L << 32);
            // 0xfff0000 -> 1111111111110000000000000000    12个1, 16个0
            // 这是根据随机数随机选择一个桶? 为什么不直接取余?
            BitKeeper keeper = bitKeepers[(int) (((m & 0xfff0000) >> 16) % bitKeepers.length)];
            keeper.random(m);
        }
    }

    public double estimate () {
        double sumbitsInverse = 0.0;

        for (BitKeeper bitKeeper : bitKeepers) {
            // TODO: bitKeeper.getMaxBits()可能为0, 需要改进
            sumbitsInverse += 1.0 /(float) bitKeeper.getMaxBits();
        }
        // 调和平均数
        double avgBits = (float) bitKeepers.length / sumbitsInverse;
        return Math.pow(2, avgBits) * this.k;
    }

    public static void main(String[] args){
        System.out.println(0xffffffff0000L);
    }
}
