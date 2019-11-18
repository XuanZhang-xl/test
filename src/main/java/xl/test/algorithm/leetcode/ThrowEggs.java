package xl.test.algorithm.leetcode;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

/**
 * 你将获得 K 个鸡蛋，并可以使用一栋从 1 到 N  共有 N 层楼的建筑。
 *
 * 每个蛋的功能都是一样的，如果一个蛋碎了，你就不能再把它掉下去。
 *
 * 你知道存在楼层 F ，满足 0 <= F <= N 任何从高于 F 的楼层落下的鸡蛋都会碎，从 F 楼层或比它低的楼层落下的鸡蛋都不会破。
 *
 * 每次移动，你可以取一个鸡蛋（如果你有完整的鸡蛋）并把它从任一楼层 X 扔下（满足 1 <= X <= N）。
 *
 * 你的目标是确切地知道 F 的值是多少。
 *
 * 无论 F 的初始值如何，你确定 F 的值的最小移动次数是多少？
 *
 *  
 *
 * 示例 1：
 *
 * 输入：K = 1, N = 2
 * 输出：2
 * 解释：
 * 鸡蛋从 1 楼掉落。如果它碎了，我们肯定知道 F = 0 。
 * 否则，鸡蛋从 2 楼掉落。如果它碎了，我们肯定知道 F = 1 。
 * 如果它没碎，那么我们肯定知道 F = 2 。
 * 因此，在最坏的情况下我们需要移动 2 次以确定 F 是多少。
 * 示例 2：
 *
 * 输入：K = 2, N = 6
 * 输出：3
 * 示例 3：
 *
 * 输入：K = 3, N = 14
 * 输出：4
 *  
 *
 * 提示：
 *
 * 1 <= K <= 100
 * 1 <= N <= 10000
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/super-egg-drop
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * PS: 如果鸡蛋没碎, 则可以重复使用,所以最笨但有效的方法是直接从一楼开始扔到N楼, 在第i楼时碎了, 则F=i-1, 必然可以得到答案.
 *
 * https://mp.weixin.qq.com/s/vMks8ule7S5HHI54Be6v3A
 *
 * created by XUAN on 2019/11/18
 */
public class ThrowEggs {

    @Test
    public void throwEggs() {
        int k = 2;
        int n = 10;
        System.out.println(throwEggs(k, n));
        System.out.println(superEggDrop(k, n));
        System.out.println(superEggDrop2(k, n));
    }

    Map<String, Integer> memo = new HashMap<>();

    public int throwEggs(int k, int n) {
        if (k == 1) return n;
        if (n == 0) return 0;
        throwEggsRecursion(k, n);
        return memo.get(k + "_" + n);
    }

    public int throwEggsRecursion(int k, int n) {
        // base case
        if (k == 1) return n;
        if (n == 0) return 0;
        // 避免重复计算
        String key = k + "_" + n;
        Integer value = memo.get(key);
        if (value != null) {
            return value;
        }

        // 初始值次数, 找个最大的
        value = Integer.MAX_VALUE;

        // 穷举所有可能的选择
        for (int i = 1; i < n + 1; i++) {
            // 计算在第i层扔下的最小次数, 选择最小的一个值
            value = Math.min(value,
                    Math.max(
                            // 在第i层扔下时, 有两种可能, 碎了, 没碎
                            // 第i层没碎, 则鸡蛋仍有k个, 剩余需要测试的楼层有n-i层
                            throwEggsRecursion(k, n - i),
                            // 第i层碎了, 则鸡蛋仍有k个, 剩余需要测试的楼层有i-1层
                            throwEggsRecursion(k - 1, i - 1)
                    ) + 1 // 扔鸡蛋次数+1
            );
        }
        // 记入备忘录
        memo.put(key, value);
        return value;
    }



    public int superEggDrop(int K, int N) {
        // Right now, dp[i] represents dp(1, i)
        int[] dp = new int[N+1];
        for (int i = 0; i <= N; ++i)
            // 保底, 每层最大次数即为层数
            dp[i] = i;

        for (int k = 2; k <= K; ++k) {
            // Now, we will develop dp2[i] = dp(k, i)
            int[] dp2 = new int[N+1];
            int x = 1;
            for (int n = 1; n <= N; ++n) {
                // Let's find dp2[n] = dp(k, n)
                // Increase our optimal x while we can make our answer better.
                // Notice max(dp[x-1], dp2[n-x]) > max(dp[x], dp2[n-x-1])
                // is simply max(T1(x-1), T2(x-1)) > max(T1(x), T2(x)).
                while (x < n && Math.max(dp[x-1], dp2[n-x]) > Math.max(dp[x], dp2[n-x-1]))
                    x++;

                // The final answer happens at this x.
                dp2[n] = 1 + Math.max(dp[x-1], dp2[n-x]);
            }
            dp = dp2;
        }
        return dp[N];
    }

    public int superEggDrop2(int K, int N) {
        int[][] dp = new int[K + 1][N + 1];
        for (int m = 1; m <= N; m++) {
            dp[0][m] = 0; // zero egg
            for (int k = 1; k <= K; k++) {
                dp[k][m] = dp[k][m - 1] + dp[k - 1][m - 1] + 1;
                if (dp[k][m] >= N) {
                    return m;
                }
            }
        }
        return N;
    }

}


