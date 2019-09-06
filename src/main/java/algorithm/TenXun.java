package algorithm;

import org.junit.Test;

/**
 * 腾讯算法题
 * created by XUAN on 2019/9/5
 */
public class TenXun {

    /**
     * 带有权重的排序
     * 有n个顾客在排队
     * 每个顾客有两属性 a, b
     * 顾客的不满意度算法:   ai(i - 1) + bi(n - i)
     *
     * 也就是 a 乘以排在他前面的人数 加上 b 乘以排在他后面的人数
     *
     * 求: 所有顾客不满意度最小的排序方法
     *
     * ai(i - 1) + bi(n - i)  --->   (ai - bi)i - ai nbi
     * 交换的时候, 只有i会变, 也就是说, 只看ai - bi的大小就可以了
     *
     * 实现就不写了
     */
    @Test
    public void WeightedQueue() {

    }

    /**
     * 给出一组整数 2,3,6,1,8,3
     * 找出其中一段连续整数, 把这段整数中的最小值 乘以这些整数之和
     *
     * 求这个乘法的最大值
     * 如上例, 则为  8 * 8 = 64
     *
     * 思路: 找出最小数, 数组分为前后两段, 在分别找出两段的最小数, 循环...知道找到最大的乘积
     *
     */
    @Test
    public void test1() {
        //int[] nums = new int[]{2,3,6,1,8,3};
        int[] nums = new int[]{1,1,3,1,1,3,3,1,1};
        int maxRegion = findMaxRegion(nums, 0, nums.length, Integer.MIN_VALUE);
        System.out.println(maxRegion);
    }

    private int findMaxRegion(int[] nums, int begin, int end, int max) {
        if (begin >= end) {
            return nums[begin] * nums[begin];
        }

        // 当前最小值
        int min = Integer.MAX_VALUE;
        // 最小值在数组中位置
        int minIndex = 0;
        // 临时结果
        int sum = 0;
        // 当前的值
        for (int i = begin; i < end; i++) {
            if (nums[i] < min){
                min = nums[i];
                minIndex = i;
            }
            sum = sum + nums[i];
        }
        sum = sum * min;
        // 和之前的最大结果比较
        max = max > sum ? max : sum;
        // 获得左边最大值
        int maxLeft = findMaxRegion(nums, begin, minIndex, max);

        max = max > maxLeft ? max : maxLeft;
        // 获得右边最大值
        int maxRight = 0;
        if (minIndex + 1 < nums.length) {
            maxRight = findMaxRegion(nums, minIndex + 1, end, max);
        }
        // 返回 当前, 左边, 右边 最大值
        return max > maxRight ? max : maxRight;
    }
}
