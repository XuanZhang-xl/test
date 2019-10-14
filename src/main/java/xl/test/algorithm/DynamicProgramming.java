package xl.test.algorithm;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * https://blog.csdn.net/zw6161080123/article/details/80639932
 * 动态规划
 * created by XUAN on 2019/8/28
 */
public class DynamicProgramming {

    /**
     * 有一座高度是10级台阶的楼梯, 从下往上走, 每跨一步只能向上1级或者2级台阶。要求用程序来求出一共有多少种走法。
     * <p>
     * 比如, 每次走1级台阶, 一共走10步, 这是其中一种走法。我们可以简写成 1,1,1,1,1,1,1,1,1,1。
     * <p>
     * 再比如, 每次走2级台阶, 一共走5步, 这是另一种走法。我们可以简写成 2,2,2,2,2。
     * <p>
     * 当然, 除此之外, 还有很多很多种走法。
     * <p>
     * 这里就要用到了动态规划的思想了：动态规划（Dynamic Programming）是一种分阶段求解决策问题的数学思想。总结起来就是一句话, 大事化小, 小事化了。
     * <p>
     * 我们用动态规划问题来看看上述的问题吧：
     * 问题建模：
     * <p>
     * 假如只差一步就能走完整个楼梯, 要分为几种情况？因为每一步能走一级或者两级台阶, 所以有如下两种情况：
     * <p>
     * 1.最后一步走2级台阶, 也就是从8级到10级
     * <p>
     * 2.最后一步走1级台阶, 也就是从9级到10级
     * <p>
     * 那么在上面的基础上假设1级到8级有X种走法, 1级到9级有Y种走法, 那么1级到10级有几种走法？
     * <p>
     * 实际上, 10级台阶的所有走法可以根据最后一步的不同分为两个部分。
     * <p>
     * 第一部分：最后一步从9级到10级, 这种走法的数量和1级到9级的数量一致, 也就是Y种。
     * <p>
     * 第二部分：最后一步从8级到10级, 这种走法的数量和1级到8级的数量一致, 也就是X种。
     * <p>
     * 总的走法就是两种走法的总和, 也就是SUM=X+Y种。
     * <p>
     * 我们把10级台阶的走法表达为F(10)此时:
     * <p>
     * F(10) = F(9)+F(8)
     * <p>
     * F(9)   = F(8)+F(7)
     * <p>
     * F(8)   = F(7)+F(6)
     * <p>
     * ...
     * <p>
     * F(3)   = F(2)+F(1)
     * <p>
     * 看到没, 我们把一个复杂的问题分阶段分步的简化, 简化成简单的问题, 这就是动态规划的思想。
     * <p>
     * 当只有1级台阶和2级台阶时走法很明显, 即F(1)=1、F(2)=2, 可以归纳出如下公式：
     * <p>
     * F(n) = F(n-1) + F(n-2)(n >= 3);
     * <p>
     * F(2) = 2;
     * <p>
     * F(1) = 1;
     * <p>
     * 动态规划中包含三个重要的概念, 最优子结构、边界、状态转移公式。
     * <p>
     * 上面我们分析出F(10)=F(9)+F(8),  其中, F(9)和F(8)是F(10)的最优子结构。
     * <p>
     * 当只有1级和2级台阶时, 我们可以直接得出结果, 而无需再次简化。我们称F(2)和F(1)是问题的"边界", 如果一个问题没有边界, 那么这个问题就没有有限解。
     * <p>
     * F(n) = F(n-1) + F(n-2)是阶段之间的状态转移公式, 它是动态规划的核心, 决定了问题的每个阶段和下阶段之间的关系。
     * <p>
     * 至此, 动态规划的“问题建模就完成了”。
     */
    @Test
    public void oneHundredStep() {
        // 100级太大了, Long型也不够, 会溢出

        long begin1 = System.currentTimeMillis();
        System.out.println(oneHundredStep0(1000L));
        long end1 = System.currentTimeMillis();
        System.out.println("用时" + (end1 - begin1));

        long begin2 = System.currentTimeMillis();
        System.out.println(oneHundredStepDynamicProgramming(1000));
        long end2 = System.currentTimeMillis();
        System.out.println("用时" + (end2 - begin2));
    }

    // 缓存, 这里可以极大提高执行效率, 免去重复计算
    Map<Long, Long> cache = new HashMap<>();

    /**
     * 使用递归实现
     * @param step
     * @return
     */
    public Long oneHundredStep0(Long step) {
        // 边界, 1级台阶, 有一种解法
        if (step == 1) {
            return 1L;
        }
        // 边界, 2级台阶, 有二种解法
        if (step == 2) {
            return 2L;
        }
        Long step_1 = step - 1;
        Long temp1 = cache.get(step_1);
        if (temp1 == null) {
            temp1 = oneHundredStep0(step_1);
            cache.put(step_1, temp1);
        }
        Long step_2 = step - 2;
        Long temp2 = cache.get(step_2);
        if (temp2 == null) {
            temp2 = oneHundredStep0(step_2);
            cache.put(step_2, temp2);
        }
        return temp1 + temp2;
    }

    public Long oneHundredStepDynamicProgramming(int step) {
        long[] dp = new long[step];
        // 边界条件
        dp[0] = 1L;
        dp[1] = 2L;
        // 从第三位开始循环
        int i = 2;
        while (i < step) {
            dp[i] = dp[i - 1] + dp[i - 2];
            i++;
        }
        return dp[step - 1];
    }


    /**
     * 0-1背包问题
     * <p>
     * 给定n个重量为w1
     * w1​, w2​,w3​,…,wn​, 价值为v1​,v2​,v3​,…,vn​的物品和容量为C
     * <p>
     * C的背包, 求这个物品中一个最有价值的子集, 使得在满足背包的容量的前提下, 包内的总价值最大
     * <p>
     * 0-1背包问题指的是每个物品只能使用一次
     * <p>
     * 递归方法
     * <p>
     * 首先我们用递归的方式来尝试解决这个问题
     * 我们用F(n,C)
     * F(n,C)表示将前nn个物品放进容量为CC的背包里, 得到的最大的价值。
     * 我们用自顶向下的角度来看, 假如我们已经进行到了最后一步（即求解将n
     * <p>
     * n个物品放到背包里获得的最大价值）, 此时我们便有两种选择
     * <p>
     * 不放第n
     * <p>
     * n个物品, 此时总价值为F(n−1,C)
     * <p>
     * 放置第n
     * n个物品, 此时总价值为vn+F(n−1,C−wn)
     * 其中:
     * vn              当前放置物品的第n个物品的价值
     * F(n−1,C−wn)     之前放置n-1个物品的最大价值, 因为第n个物品用去了wn的容量, 所以计算时容量需要减去wn, 也就是说, 第n个物品已经放了的前提下, 计算前n-1个物品在 C-wn容量下的最大价值
     * 由此, 把问题由大化小
     *
     * <p>
     * 两种选择中总价值最大的方案就是我们的最终方案, 递推式（有时也称之为状态转移方程）如下
     * F(i,C)=max(F(i−1,C),v(i)+F(i−1,C−w(i)))
     */
    @Test
    public void knapsack() {
        // 重量
        int[] w = {2, 1, 3, 2};
        // 价值
        int[] v = {12, 10, 20, 15};
        // 背包容量
        int capacity = 5;
        System.out.println(solveKnapsack(w, v, w.length - 1, capacity));
    }

    /**
     * 解决背包问题的递归函数
     *
     * @param w        物品的重量数组
     * @param v        物品的价值数组
     * @param index    当前待选择的物品索引
     * @param capacity 当前背包有效容量
     * @return 最大价值
     */
    private int solveKnapsack(int[] w, int[] v, int index, int capacity) {
        //基准条件：如果什么都不放或者容量不足, 直接返回当前价值0
        if (index < 0 || capacity <= 0) {
            return 0;
        }
        //不放第index个物品所得价值
        int res = solveKnapsack(w, v, index - 1, capacity);
        //放第index个物品所得价值（前提是：第index个物品可以放得下）
        if (w[index] <= capacity) {
            res = Math.max(res,
                    v[index] + solveKnapsack(w, v, index - 1, capacity - w[index]));
        }
        return res;
    }


    /**
     * 使用动态规划求解 0-1背包问题
     *
     * 行: 背包容量
     * 列: 物品数量
     *
     * 当前物品不放 和 当前物品价值+之前物品最大价值 取最大值
     *
     *
     *    -  0  1  2  3
     * 0  0  0  0  0  0
     * 1  0  0  10 10 10
     * 2  0  12 12 12 15
     * 3  0  12 22 22 25
     * 4  0  12 22 30 30
     * 5  0  12 22 32 37
     *
     * 这个题目不能用一维数组实现, 因为不知道会使用之前的哪一个数据, 比如
     * dp[3][5] = max(dp[2][5], v(3) + dp[2][5 - w[3]])  = max(32, 15 + dp[2][3]) = max(32, 37) = 37
     * 这里用到dp[2][3], 而w[3]一变, dp[2][?]也跟着变, 根本不确定 ? 到底是几
     *
     */
    @Test
    public void knapsackDynamicProgramming() {
        // 物品占用容量
        int[] w = {2, 1, 3, 2};
        // 物品价值
        int[] v = {12, 10, 20, 15};
        // 背包容量
        int C = 5;
        // 增加一列, 作为一个物品也不放时的价值存储, 全部为0, 无需初始化
        int size = w.length + 1;
        // 初始化时C增加一行, 作为背包容量为0时的最大价值, 全部为0, 无需初始化
        int[][] dp = new int[size][C + 1];

        // 尝试放入第i个物品
        for (int i = 1; i < size; i++) {
            // 尝试将背包容量扩张至j
            for (int j = 1; j <= C; j++) {
                // 因为增加了一空列, 所以这里i都要 -1, 故增加一个变量
                int tempI = i - 1;
                // 获得当前物品不放入时的最大价值
                int value = dp[tempI][j];
                // 如果当前物品占用的容量小于背包容量, 则需要与 [当前物品放入背包后的最大价值] 比较
                if (w[tempI] <= j) {
                    // [当前物品不放入时的最大价值] 与 [当前物品放入背包后的最大价值] 取最大值
                    value = Math.max(value, v[tempI] + dp[tempI][j - w[tempI]]);
                }
                // 放入结果集
                dp[i][j] = value;
            }
        }
        System.out.println(dp[size - 1][C]);
    }


    /**
     * 最长子串
     * 这里应该是只要字符顺序一致即可， 匹配的字符中间可以夹别的字符
     *
     * 字符串s: BDCABA,  字符串t: ABCBDAB
     *
     *    -  B  D  C  A  B  A
     * -  0  0  0  0  0  0  0
     * A  0  0  0  0  1  1  1
     * B  0  1  1  1  1  2  2
     * C  0  1  1  2  2  2  2
     * B  0  1  1  2  2  3  3
     * D  0  1  2  2  2  3  3
     * A  0  1  2  2  3  3  4
     * B  0  1  2  2  3  4  4
     *
     *
     * 设 X=(x1,x2,.....xn)和 Y={y1,y2,.....ym} 是两个序列, 将 X 和 Y 的最长公共子序列记为LCS(X,Y), 
     * 如果 xn=ym, 即X的最后一个元素与Y的最后一个元素相同, 这说明该元素一定位于公共子序列中。
     * 因此, 现在只需要找：LCS(Xn-1, Ym-1)就好, LCS(X,Y)=LCS(Xn-1, Ym-1)+1, 
     * 如果xn != ym, 这下要麻烦一点, 因为它产生了两个子问题：LCS(Xn-1, Ym) 和 LCS(Xn, Ym-1)。
     *
     * TODO: 怎么找出 BCBA 和 BCAB 这两个最长子串
     */
    @Test
    public void LongestSubString(){
        String s = "BDCABA";
        String t = "ABCBDAB";

        int[][] dp = new int[t.length() + 1][s.length() + 1];

        for (int j = 1; j < s.length() + 1; j++) {
            for (int i = 1; i < t.length() + 1; i++) {
                if (s.charAt(j - 1) == t.charAt(i - 1)) {
                    dp[i][j] = dp[i - 1][j - 1] + 1;
                } else {
                    dp[i][j] = Math.max(dp[i][j - 1], dp[i - 1][j]);
                }
            }
        }
        System.out.println(dp[t.length()][s.length()]);
    }

}
