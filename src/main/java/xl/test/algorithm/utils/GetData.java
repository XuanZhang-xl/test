package xl.test.algorithm.utils;

import java.util.Random;

/**
 * created by XUAN on 2019/11/20
 */
public class GetData {

    /**
     * 随机获得二维char数组
     * @param length
     * @param row
     * @return
     */
    public static char[][] getRandomZeroOneCharArray(int length, int row) {
        char[][] outer = new char[row][length];
        Random random = new Random();
        for (int i = 0; i < row; i++) {
            char[] inner = new char[length];
            for (int j = 0; j < length; j++) {
                if (random.nextBoolean()) {
                    inner[j] = '1';
                } else {
                    inner[j] = '0';
                }
            }
            outer[i] = inner;
        }
        return outer;
    }

    // 获得固定char数组
    static char[] one = new char[]{'1','1','0','0','0'};
    static char[] two = new char[]{'0','0','1','0','0'};
    static char[] three = new char[]{'0','0','0','1','1'};
    static char[] four = new char[]{'1','1','1','1','0'};
    static char[] five = new char[]{'1','1','0','1','0'};
    static char[] six = new char[]{'0','0','0','0','0'};

    static char[] a = new char[]{'1','1','1','1','1','1'};
    static char[] b = new char[]{'1','0','0','0','0','1'};
    static char[] c = new char[]{'1','0','1','1','0','1'};

    public static char[][] getCharArraySample1() {
        return new char[][]{four.clone(), five.clone(), one.clone(), six.clone()};
    }
    public static char[][] getCharArraySample2() {
        return new char[][]{one.clone(), one.clone(), two.clone(), three.clone()};
    }
    public static char[][] getCharArraySample3() {
        return new char[][]{a.clone(), b.clone(), c.clone(), b.clone(), a.clone()};
    }

    public static void main(String[] args){
        char[][] chars = getRandomZeroOneCharArray(10, 10);
        System.out.println(chars);
    }
}
