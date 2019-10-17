package xl.test.algorithm.leetcode;

import org.junit.Test;

/**
 * 如果一个字符序列与它反转后的字符序列一致，那么它是回文字符序列。
 * <p>
 * 如果对于某个  i，A_i != B_i，那么 A_1, A_2, ... 和 B_1, B_2, ... 这两个字符序列是不同的。
 * <p>
 * <p>
 * <p>
 * 示例 1：
 * <p>
 * 输入：
 * S = 'bccb'
 * 输出：6
 * 解释：
 * 6 个不同的非空回文子字符序列分别为：'b', 'c', 'bb', 'cc', 'bcb', 'bccb'。
 * 注意：'bcb' 虽然出现两次但仅计数一次。
 * <p>
 * 示例 2：
 * <p>
 * 输入：
 * S = 'abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba'
 * 输出：104860361
 * 解释：
 * 共有 3104860382 个不同的非空回文子字符序列，对 10^9 + 7 取模为 104860361。
 * <p>
 * <p>
 * <p>
 * 提示：
 * <p>
 * 字符串 S 的长度将在[1, 1000]范围内。
 * 每个字符 S[i] 将会是集合 {'a', 'b', 'c', 'd'} 中的某一个。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/count-different-palindromic-subsequences
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * created by XUAN on 2019/10/15
 */
public class CountDifferentPalindromicSubsequence {

    private static final String[] TEST_STRINGS = new String[]{
            //"aaaa",
            //"abccb",
            //"qwertyyyytrewq",
            //"aba",
            //"asdsaxssxasss",
            //"qwerty",
            "cbbbcc",
            "abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba"
    };

    @Test
    public void test() {
        for (String s : TEST_STRINGS) {
            System.out.println(countPalindromicSubsequences(s));
        }
    }

    /**
     * 这里的状态转移公式不再是一个单一的等式
     * ...太复杂,,不知怎么总结
     *
     *
     * @param s
     * @return
     */
    public int countPalindromicSubsequences(String s) {
        int strSize = s.length(), M = 1000000000 + 7;
        //dp[i][j]表示的S[i, j]这段字符串中不同的回文子序列个数
        int[][] dp = new int[strSize][strSize];
        //初始化，当个长度的字符串也是一个结果
        for (int i = 0; i < strSize; ++i) {
            dp[i][i] = 1;
        }
        //开始动态规划
        for (int i = strSize - 2; i >= 0; i--) {
            for (int j = i + 1; j < strSize; j++) {
                //上面的两层for循环用于穷举区间[i, j]，i用于确定区间的起点，j确定区间的尾端，并且区间的长度都是由2逐渐增大
                if (s.charAt(i) == s.charAt(j)) {
                    //left用于寻找与S[i]相同的左端第一个下标，right用于寻找与S[i]相同的右端第一个下标
                    int left = i + 1, right = j - 1;
                    while (left <= right && s.charAt(left) != s.charAt(i)) {
                        ++left;
                    }
                    while (left <= right && s.charAt(right) != s.charAt(i)) {
                        --right;
                    }
                    if (left > right) {
                        //中间没有和S[i]相同的字母，例如"aba"这种情况
                        //其中dp[i + 1][j - 1]是中间部分的回文子序列个数，因为中间的所有子序列可以单独存在，也可以再外面包裹上字母a，所以是成对出现的，要乘2。
                        //加2的原因是外层的"a"和"aa"也要统计上
                        dp[i][j] = dp[i + 1][j - 1] * 2 + 2;
                    } else if (left == right) {
                        //中间只有一个和S[i]相同的字母，就是"aaa"这种情况，
                        //其中乘2的部分跟上面的原因相同，
                        //加1的原因是单个字母"a"的情况已经在中间部分算过了，外层就只能再加上个"aa"了。
                        dp[i][j] = dp[i + 1][j - 1] * 2 + 1;
                    } else {
                        //中间至少有两个和S[i]相同的字母，就是"aabaa"这种情况，
                        //其中乘2的部分跟上面的原因相同，要减去left和right中间部分的子序列个数的原因是其被计算了两遍，要将多余的减掉。
                        dp[i][j] = dp[i + 1][j - 1] * 2 - dp[left + 1][right - 1];
                    }
                } else {
                    //dp[i][j - 1] + dp[i + 1][j]这里计算了dp[i + 1][j - 1]两遍
                    dp[i][j] = dp[i][j - 1] + dp[i + 1][j] - dp[i + 1][j - 1];
                }
                dp[i][j] = dp[i][j] < 0 ? dp[i][j] + M : dp[i][j] % M;
            }
        }
        return dp[0][strSize - 1];
    }


}
