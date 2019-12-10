package xl.test.algorithm;

import org.junit.Test;

/**
 * 给定一个整数 n，求以 1 ... n 为节点组成的二叉搜索树有多少种？
 *
 * 示例:
 *
 * 输入: 3
 * 输出: 5
 * 解释:
 * 给定 n = 3, 一共有 5 种不同结构的二叉搜索树:
 *
 *    1         3     3      2      1
 *     \       /     /      / \      \
 *      3     2     1      1   3      2
 *     /     /       \                 \
 *    2     1         2                 3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/unique-binary-search-trees
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 *
 * 1 -> 1
 *     大  小
 *      \ /
 *       1
 *      / \
 *     小  大
 *
 * 2 -> 2
 *      一        二
 *      2         1
 *     /           \
 *    1             2
 *
 * 3 -> 5
 *  1开头: 2种 2*1 , 2开头, 1种 1*1    3开头2种 2*1
 *    1         3     3      2      1
 *     \       /     /      / \      \
 *      3     2     1      1   3      2
 *     /     /       \                 \
 *    2     1         2                 3
 *
 * 4 -> 14
 * 1开头: 5种 1*5  2开头2种 1*2  3开头2种 2*1  4开头5种 5*1
 *
 *
 *                4
 *               /
 *  1           3     3      2      1              3            1          1        1
 *   \         /     / \    / \      \            / \            \          \        \
 *    3       2     1   4  1   3      2          2   4            4          4        3
 *   / \     /       \          \      \        /                /          /        / \
 *  2   4   1         2          4      3      1                3          2        2   4
 *                                       \                     /           \
 *                                        4                   2             3
 *
 * 5 -> 14 * 2 + 5* 2 + 2 = 40
 *
 * 3开头
 *
 *             3               3
 *            / \             / \
 *           2   4           1   4
 *          /     \          \    \
 *         1       5          2    5
 *
 *
 * 卡特兰数
 *
 * created by XUAN on 2019/12/9
 */
public class UniqueBinarySearchTrees {

    @Test
    public void numTrees() {
        System.out.println(numTrees(3));
        System.out.println(numTrees(4));
        System.out.println(numTrees(5));
    }

    public int numTrees(int n) {
        if (n <= 0) {
            return 1;
        }
        if (n == 1) {
            return 1;
        }

        int[] dp = new int[n + 1];
        // 当左/右子树是空时, 也可以是一棵树, 所以当根节点是空时, 也应该有一颗树, 空树
        dp[0] = 1;
        // 只有根节点
        dp[1] = 1;

        for (int i = 2; i <= n; ++i) {
            // 假设第j个节点为根节点, 则可能性为  一共有j-1个节点的数量 乘以 一共有i-j个节点的数量, 遍历所有的j, 相加, 则为一共有i个节点的二叉树数量
            // 还是很好理解的
            for (int j = 1; j <= i; ++j) {
                dp[i] += dp[j - 1] * dp[i - j];
            }
        }
        return dp[n];
    }

}
