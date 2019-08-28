package algorithm;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 动态规划
 * created by XUAN on 2019/8/28
 */
public class DynamicProgramming {

    /**
     * 有一座高度是10级台阶的楼梯，从下往上走，每跨一步只能向上1级或者2级台阶。要求用程序来求出一共有多少种走法。
     * <p>
     * 比如，每次走1级台阶，一共走10步，这是其中一种走法。我们可以简写成 1,1,1,1,1,1,1,1,1,1。
     * <p>
     * 再比如，每次走2级台阶，一共走5步，这是另一种走法。我们可以简写成 2,2,2,2,2。
     * <p>
     * 当然，除此之外，还有很多很多种走法。
     * <p>
     * 这里就要用到了动态规划的思想了：动态规划（Dynamic Programming）是一种分阶段求解决策问题的数学思想。总结起来就是一句话，大事化小，小事化了。
     * <p>
     * 我们用动态规划问题来看看上述的问题吧：
     * 问题建模：
     * <p>
     * 假如只差一步就能走完整个楼梯，要分为几种情况？因为每一步能走一级或者两级台阶，所以有如下两种情况：
     * <p>
     * 1.最后一步走2级台阶，也就是从8级到10级
     * <p>
     * 2.最后一步走1级台阶，也就是从9级到10级
     * <p>
     * 那么在上面的基础上假设1级到8级有X种走法，1级到9级有Y种走法，那么1级到10级有几种走法？
     * <p>
     * 实际上，10级台阶的所有走法可以根据最后一步的不同分为两个部分。
     * <p>
     * 第一部分：最后一步从9级到10级，这种走法的数量和1级到9级的数量一致，也就是Y种。
     * <p>
     * 第二部分：最后一步从8级到10级，这种走法的数量和1级到8级的数量一致，也就是X种。
     * <p>
     * 总的走法就是两种走法的总和，也就是SUM=X+Y种。
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
     * 看到没，我们把一个复杂的问题分阶段分步的简化，简化成简单的问题，这就是动态规划的思想。
     * <p>
     * 当只有1级台阶和2级台阶时走法很明显，即F(1)=1、F(2)=2，可以归纳出如下公式：
     * <p>
     * F(n) = F(n-1) + F(n-2)(n >= 3);
     * <p>
     * F(2) = 2;
     * <p>
     * F(1) = 1;
     * <p>
     * 动态规划中包含三个重要的概念，最优子结构、边界、状态转移公式。
     * <p>
     * 上面我们分析出F(10)=F(9)+F(8)， 其中，F(9)和F(8)是F(10)的最优子结构。
     * <p>
     * 当只有1级和2级台阶时，我们可以直接得出结果，而无需再次简化。我们称F(2)和F(1)是问题的"边界"，如果一个问题没有边界，那么这个问题就没有有限解。
     * <p>
     * F(n) = F(n-1) + F(n-2)是阶段之间的状态转移公式，它是动态规划的核心，决定了问题的每个阶段和下阶段之间的关系。
     * <p>
     * 至此，动态规划的“问题建模就完成了”。
     */
    @Test
    public void oneHundredStep() {
        // 100级太大了, Long型也不够, 会溢出
        System.out.println(oneHundredStep0(100L));
    }

    // 缓存, 这里可以极大提高执行效率, 免去重复计算
    Map<Long, Long> cache = new HashMap<>();

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


    /**
     * 0-1背包问题
     * <p>
     * 给定n个重量为w1
     * w1​，w2​,w3​,…,wn​，价值为v1​,v2​,v3​,…,vn​的物品和容量为C
     * <p>
     * C的背包，求这个物品中一个最有价值的子集，使得在满足背包的容量的前提下，包内的总价值最大
     * <p>
     * 0-1背包问题指的是每个物品只能使用一次
     * <p>
     * 递归方法
     * <p>
     * 首先我们用递归的方式来尝试解决这个问题
     * 我们用F(n,C)
     * F(n,C)表示将前nn个物品放进容量为CC的背包里，得到的最大的价值。
     * 我们用自顶向下的角度来看，假如我们已经进行到了最后一步（即求解将n
     * <p>
     * n个物品放到背包里获得的最大价值），此时我们便有两种选择
     * <p>
     * 不放第n
     * <p>
     * n个物品，此时总价值为F(n−1,C)
     * <p>
     * 放置第n
     * n个物品，此时总价值为vn+F(n−1,C−wn)
     * <p>
     * 两种选择中总价值最大的方案就是我们的最终方案，递推式（有时也称之为状态转移方程）如下
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
        //基准条件：如果索引无效或者容量不足，直接返回当前价值0
        if (index < 0 || capacity <= 0) {
            return 0;
        }
        //不放第index个物品所得价值
        int res = solveKnapsack(w, v, index - 1, capacity);
        //放第index个物品所得价值（前提是：第index个物品可以放得下）
        if (w[index] <= capacity) {
            res = Math.max(res, v[index] + solveKnapsack(w, v, index - 1, capacity - w[index]));
        }
        return res;
    }


    /**
     * 使用动态规划求解 0-1背包问题
     * TODO:尼玛到底怎么理解?
     */
    @Test
    public void knapsackDynamicProgramming() {
        int[] w = {2, 1, 3, 2};
        int[] v = {12, 10, 20, 15};
        int C = 5;
        int size = w.length;
        if (size == 0) {
            System.out.println(0);
        }
        int[][] dp = new int[size][C + 1];
        // 初始化第一行
        // 仅考虑容量为C的背包放第0个物品的情况
        for (int i = 0; i <= C; i++) {
            dp[0][i] = w[0] <= i ? v[0] : 0;
        }
        //填充其他行和列
        for (int i = 1; i < size; i++) {
            for (int j = 0; j <= C; j++) {
                dp[i][j] = dp[i - 1][j];
                if (w[i] <= j) {
                    dp[i][j] = Math.max(dp[i][j], v[i] + dp[i - 1][j - w[i]]);
                }
            }
        }
        System.out.println(dp[size - 1][C]);
    }

}
