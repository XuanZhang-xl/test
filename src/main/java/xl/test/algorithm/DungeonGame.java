package xl.test.algorithm;

import org.junit.Test;

/**
 * 一些恶魔抓住了公主（P）并将她关在了地下城的右下角。地下城是由 M x N 个房间组成的二维网格。我们英勇的骑士（K）最初被安置在左上角的房间里，他必须穿过地下城并通过对抗恶魔来拯救公主。
 *
 * 骑士的初始健康点数为一个正整数。如果他的健康点数在某一时刻降至 0 或以下，他会立即死亡。
 *
 * 有些房间由恶魔守卫，因此骑士在进入这些房间时会失去健康点数（若房间里的值为负整数，则表示骑士将损失健康点数）；其他房间要么是空的（房间里的值为 0），要么包含增加骑士健康点数的魔法球（若房间里的值为正整数，则表示骑士将增加健康点数）。
 *
 * 为了尽快到达公主，骑士决定每次只向右或向下移动一步。
 *
 *
 *
 * 编写一个函数来计算确保骑士能够拯救到公主所需的最低初始健康点数。
 *
 * 例如，考虑到如下布局的地下城，如果骑士遵循最佳路径 右 -> 右 -> 下 -> 下，则骑士的初始健康点数至少为 7。
 * -2 (K) 	-3 	3
 * -5 	-10 	1
 * 10 	30 	-5 (P)
 *
 *
 *
 * 说明:
 *
 *     骑士的健康点数没有上限。
 *     任何房间都可能对骑士的健康点数造成威胁，也可能增加骑士的健康点数，包括骑士进入的左上角房间以及公主被监禁的右下角房间。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/dungeon-game
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/11/28
 */
public class DungeonGame {

    @Test
    public void calculateMinimumHP() {
        int[] a = {-2,-3,3};
        int[] b = {-5,-10,1};
        int[] c = {10,30,-5};
        int[][] dungeon = {a,b,c};
        System.out.println(calculateMinimumHPBest(dungeon));
    }

    public int calculateMinimumHPBest(int[][] dungeon) {
        if (dungeon == null || dungeon.length == 0 || dungeon[0].length == 0) {
            return 0;
        }
        int rowSize = dungeon.length;
        int colSize = dungeon[0].length;
        int[][] dp = new int[rowSize][colSize];
        // 设置最后一个值。
        dp[rowSize - 1][colSize -1] = Math.max(0, -dungeon[rowSize - 1][colSize - 1]);

        // 设置最后一列的值
        for (int i = rowSize - 2; i >= 0; --i) {
            int needMin = dp[i + 1][colSize - 1] - dungeon[i][colSize - 1];
            dp[i][colSize -1] = Math.max(0, needMin);
        }

        // 设置最后一行的值
        for (int i = colSize - 2; i >= 0; --i) {
            int needMin = dp[rowSize - 1][i + 1] - dungeon[rowSize - 1][i];
            dp[rowSize - 1][i] = Math.max(0, needMin);
        }

        for (int i = rowSize - 2; i >= 0; --i) {
            for (int j = colSize - 2; j >= 0; --j) {
                // 从右边和下边选择一个最小值，然后减去当前的 dungeon 值
                int needMin = Math.min(dp[i + 1][j], dp[i][j + 1]) - dungeon[i][j];
                dp[i][j] = Math.max(0, needMin);
            }
        }
        return dp[0][0] + 1;
    }


    /**
     * 从前往后看的失败作, 保留
     *
     * 从前往后看失败的关键就是有冗余的健康点数概念, 需要计算上面与左边的冗余的健康点数
     *
     * 当 上面冗余的健康点数>下面冗余的健康点数 && dp[上面]<dp[下面]  或反过来时, 就不知道怎么判断了
     *
     *
     * @param dungeon
     * @return
     */
    public int calculateMinimumHP(int[][] dungeon) {
        if (dungeon == null) {
            return 0;
        }
        int row = dungeon.length;
        if (row == 0) {
            return 0;
        }
        int length = dungeon[0].length;
        int[][] dp = new int[row][length];

        // 初始化
        dp[0][0] = needStamina(dungeon[0][0]);
        for (int i = 1; i < row; i++) {
            if (dungeon[i][0] >= 0) {
                dp[i][0] = dp[i - 1][0];
            } else {
                if (dungeon[i - 1][0] < -dungeon[i][0]) {
                    dp[i][0] = dp[i - 1][0] - dungeon[i][0];
                } else {
                    dp[i][0] = dp[i - 1][0];
                }
            }
            dungeon[i][0] = dungeon[i - 1][0] + dungeon[i][0];
        }
        for (int i = 1; i < length; i++) {
            if (dungeon[0][i] >= 0) {
                dp[0][i] = dp[0][i - 1];
            } else {
                if (dungeon[0][i - 1] < -dungeon[0][i]) {
                    dp[0][i] = dp[0][i - 1] - dungeon[0][i];
                } else {
                    dp[0][i] = dp[0][i - 1];
                }
            }
            dungeon[0][i] = dungeon[0][i - 1] + dungeon[0][i];
        }
        for (int i = 1; i < row; i++) {
            for (int j = 1; j < length; j++) {

                // 取冗余健康点数多的
                if (dungeon[i][j] < 0) {
                    int maxTemp = Math.max(dungeon[i - 1][j], dungeon[i][j - 1]);
                    // 需要健康点数
                    if (maxTemp < 0) {
                        // 当前没有多余的健康点数, 则直接加需要的健康点数
                        dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]) + Math.abs(dungeon[i][j]);
                    } else {
                        // 当前有多余健康点数, 则应耗尽当前可用健康点数后再加所需健康点数
                        if (maxTemp < -dungeon[i][j]) {
                            dp[i][j] = dp[i][j] - maxTemp - dungeon[i][j];
                        } else {
                            // 只扣多余的健康点数
                            dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]);
                        }
                    }
                } else {
                    // 不需要健康点数
                    dp[i][j] = Math.min(dp[i - 1][j], dp[i][j - 1]);
                }
                dungeon[i][j] = Math.max(dungeon[i - 1][j], dungeon[i][j - 1]) + dungeon[i][j];
            }
        }
        return dp[row - 1][length - 1];
    }

    private int needStamina (int stamina) {
        if (stamina > 0) {
            return 0;
        } else {
            return Math.abs(stamina) + 1;
        }
    }
}
