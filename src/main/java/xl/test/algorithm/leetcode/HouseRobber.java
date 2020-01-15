package xl.test.algorithm.leetcode;

import org.junit.Test;

/**
 * 你是一个专业的小偷，计划偷窃沿街的房屋。每间房内都藏有一定的现金，影响你偷窃的唯一制约因素就是相邻的房屋装有相互连通的防盗系统，如果两间相邻的房屋在同一晚上被小偷闯入，系统会自动报警。
 *
 * 给定一个代表每个房屋存放金额的非负整数数组，计算你在不触动警报装置的情况下，能够偷窃到的最高金额。
 *
 * 示例 1:
 *
 * 输入: [1,2,3,1]
 * 输出: 4
 * 解释: 偷窃 1 号房屋 (金额 = 1) ，然后偷窃 3 号房屋 (金额 = 3)。
 *      偷窃到的最高金额 = 1 + 3 = 4 。
 *
 * 示例 2:
 *
 * 输入: [2,7,9,3,1]
 * 输出: 12
 * 解释: 偷窃 1 号房屋 (金额 = 2), 偷窃 3 号房屋 (金额 = 9)，接着偷窃 5 号房屋 (金额 = 1)。
 *      偷窃到的最高金额 = 2 + 9 + 1 = 12 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/house-robber
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2020/1/15
 */
public class HouseRobber {

    @Test
    public void houseRobber () {
        int[] houses = new int[]{2,7,9,3,1};
        System.out.println(houseRobber0(houses));
    }

    /**
     * 解决动态规划问题就是找「状态」和「选择」，仅此而已。
     * 假想你就是这个专业强盗，从左到右走过这一排房子，在每间房子前都有两种选择：抢或者不抢。
     * 如果你抢了这间房子，那么你肯定不能抢相邻的下一间房子了，只能从下下间房子开始做选择。
     * 如果你不抢这间房子，那么你可以走到下一间房子前，继续做选择。
     * 当你走过了最后一间房子后，你就没得抢了，能抢到的钱显然是 0（base case）。
     * 以上的逻辑很简单吧，其实已经明确了「状态」和「选择」：你面前房子的索引就是状态，抢和不抢就是选择。
     * https://mp.weixin.qq.com/s/z44hk0MW14_mAQd7988mfw
     *
     * @param houses
     * @return
     */
    private int houseRobber0(int[] houses) {
        return dp(houses, 0);
    }
    // 返回 nums[start..] 能抢到的最大值
    private int dp(int[] nums, int start) {
        if (start >= nums.length) {
            return 0;
        }

        int res = Math.max(
                // 不抢，去下家
                dp(nums, start + 1),
                // 抢，去下下家
                nums[start] + dp(nums, start + 2)
        );
        return res;
    }
    private int houseRobber1(int[] nums) {
        if (nums == null || nums.length == 0) {
            return 0;
        }
        if (nums.length == 1) {
            return nums[0];
        }

        int[] dp = new int[nums.length];
        dp[0] = nums[0];
        for (int i = 1; i < nums.length; i++) {
            //dp[i] = Math.max(dp[i - 1], )
        }


        return dp[0];

    }
}
