package algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 八皇后问题
 * created by XUAN on 2019/8/15
 */
public class EightQueensUsingArrayList {

    /**
     * 皇后数量
     */
    private final int queenNumber = 11;

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
    private List<Integer> baseArray = null;

    {
        baseArray = new ArrayList<>(queenNumber);
        int init = 1;
        for (int i = 0; i < queenNumber; i++) {
            if (i != 0) {
                init = init * 2;
            }
            if (i == queenNumber - 1) {
                max = init;
            }
            baseArray.add(i, init);
        }
    }

    @Test
    public void solution() {
        long begin = System.currentTimeMillis();
        List<Integer> chess = new ArrayList<>(queenNumber);
        solution(chess, 0);
        long end = System.currentTimeMillis();
        System.out.println("一共有" + resultAmount + "种解法， 共用时" + (end - begin) + "毫秒");
    }

    private void solution(List<Integer> chess, int currentRow) {
        if (currentRow == queenNumber) {
            resultAmount++;
            //printChess(chess);
            return;
        }

        for (int base = 0; base < queenNumber; base++) {
            Integer tryInteger = baseArray.get(base);
            if (!chess.contains(tryInteger)) {
                // 先尝试着放
                chess.add(currentRow, tryInteger);
                if (checkSafety(chess, currentRow)) {
                    solution(chess, currentRow + 1);
                }
                chess.remove(currentRow);
            }
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
    private boolean checkSafety(List<Integer> chess, int currentRow) {
        int position = chess.get(currentRow);
        int tempMin = position, tempMax = position;
        // 每一行都要通过检查
        for (int i = currentRow - 1; i >= 0; i--) {
            Integer checkPosition = chess.get(i);
            if (checkPosition == position) {
                return false;
            }
            // 右斜线
            if (tempMin > 1) {
                if (checkPosition == (tempMin = tempMin >> 1)) {
                    return false;
                }
            }
            // 左斜线
            if (tempMax < max) {
                if (checkPosition == (tempMax = tempMax << 1)) {
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
