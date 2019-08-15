package algorithm;

import org.junit.Test;

/**
 * 八皇后问题
 *
 * 由于每一排最多只有一个皇后, 所以可以用byte类型
 *
 * 满足条件:
 * 1. 下面这些数字必然每个有且只有一次, 如果没有对角线限制, 那么答案就是这些数字的排列
 * 2. 由于对角线限制, 下面一排的数字不能是当前排的1/2或2倍
 * 3. 由于byte类型是有符号的, java又没有无符号类型, 所以还是short方便点, short也方便于扩展到15皇后
 * 4. short不知怎么乘, 还是int吧, 反正机器都64位了, long也行
 * 00000001   1
 * 00000010   2
 * 00000100   4
 * 00001000   8
 * 00010000   16
 * 00100000   32
 * 01000000   64
 * 10000000   byte:-127 short: 128
 *
 * created by XUAN on 2019/8/15
 */
public class EightQueens {

    /**
     * 皇后数量
     */
    private final int queenNumber = 15;

    /**
     * 结果数量
     */
    private int resultAmount = 0;

    /**
     * 所用的数字的最大值
     */
    private int max;

    /**
     * 基础值
     */
    private int[] baseArray = null;

    {
        baseArray = new int[queenNumber];
        int init = 1;
        for (int i = 0; i < queenNumber; i++) {
            if (i != 0) {
                init = init * 2;
            }
            if (i == queenNumber - 1) {
                max = init;
            }
            baseArray[i] = init;
        }
    }

    @Test
    public void solution() {
        long begin = System.currentTimeMillis();
        int[] chess = new int[queenNumber];
        solution(chess, 0);
        long end = System.currentTimeMillis();
        System.out.println("一共有" + resultAmount + "种解法， 共用时" + (end - begin) + "毫秒");
    }

    private void solution(int[] chess, int currentRow) {
        if (currentRow == queenNumber) {
            resultAmount++;
            //printChess(chess);
            return;
        }

        for (int base = 0; base < queenNumber; base++) {
            // 先尝试着放
            chess[currentRow] = baseArray[base];
            if (checkSafety(chess, currentRow)) {
                solution(chess, currentRow + 1);
            }
            chess[currentRow] = 0;
        }
    }

    /**
     * 棋盘
     * base: 尝试的下一列的index
     * currentRow: 当前循环到的列的index
     * @param chess
     * @param base
     * @param currentRow
     * @return
     */
    private boolean checkSafety(int[] chess, int currentRow) {
        int position = chess[currentRow];
        int tempMin = position, tempMax = position;
        // 每一行都要通过检查
        for (int i = currentRow - 1; i >= 0; i--) {
            if (chess[i] == position) {
                return false;
            }
            // 右斜线
            if (tempMin > 1) {
                if (chess[i] == (tempMin = tempMin >> 1)) {
                    return false;
                }
            }
            // 左斜线
            if (tempMax < max) {
                if (chess[i] == (tempMax = tempMax << 1)) {
                    return false;
                }
            }
        }
        return true;
    }

    private void printChess(int[] chess) {
        System.out.println("第" + resultAmount + "种解法: ");
        for (int i = 0; i < chess.length; i++) {
            System.out.println(chess[i]);
        }
    }
}
