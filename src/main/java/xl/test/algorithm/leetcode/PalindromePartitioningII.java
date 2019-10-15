package xl.test.algorithm.leetcode;

import org.junit.Test;

import java.util.Arrays;

/**
 * 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。
 * <p>
 * 返回符合要求的最少分割次数。
 * <p>
 * 示例:
 * <p>
 * 输入: "aab"
 * 输出: 1
 * 解释: 进行一次分割就可将 s 分割成 ["aa","b"] 这样两个回文子串。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/palindrome-partitioning-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * created by XUAN on 2019/10/14
 */
public class PalindromePartitioningII {

    private static final String[] TEST_STRINGS = new String[]{
            "abccb",
            "qwertyyyytrewq",
            "aba",
            "asdsaxssxasss",
            "qwerty",
            "aabbaabbaacdfgfdccfdgssa"
    };

    /**
     *
     * 流程: 以 abccb为例
     *
     * 初始     dp[0] == dp[1] == dp[2] == dp[3] == dp[4] = 4
     *
     * abccb   奇: dp[0] = min(dp[0], 0) = 0    // 此处的min函数中用于比较的0是写死的, 因为只有一个字符, 明显是0
     * ^
     * abccb   偶: S[0] !=S[1] 无
     * ^^
     *
     * abccb   奇: dp[1] = min(dp[1], dp[0] + 1) = 1
     *  ^
     * abccb   偶: S[1] !=S[2] 无
     *  ^^
     *
     * abccb   奇: dp[2] = min(dp[2], dp[1] + 1) = 2
     *   ^
     * abccb   偶: S[2] == S[3] --> dp[3] = min(dp[3], dp[1] + 1) = 2  // S[1]之后的字符都已经匹配了, 所以S[1]之前需要的数量+1
     *   ^^        S[1] == S[4] --> dp[4] = min(dp[4], dp[0] + 1) = 1  // S[0]之后的字符都已经匹配了, 所以S[0]之前需要的数量+1  其实这里已经遍历到最后了, 可以结束遍历了
     *
     *
     * abccb   奇: dp[3] = min(dp[3], dp[2] + 1) = 2
     *    ^
     * abccb   偶: S[3] !=S[4] 无
     *    ^^
     *
     * abccb   奇偶:无
     *     ^
     *
     * 流程总结:
     * 1. 初始为字符串长度-1, 也就是所有字符都切割
     * 2. 选中一个字符或两个字符
     * 3. 以选中的字符为中心向左右扩张, 判断是否为回文
     * 4. 如果为回文, 则前面未匹配到的字符需要切割的数量+1作为dp数组的值(注意: 遍历到哪里, 修改对应的dp数组, 而不是修改中心字符对应的dp数组), 且扩张后继续匹配
     * 5. 如果不为回文, 则跳过遍历下一个字符
     *
     *
     * 中心思想:
     * 找到以每个字符为中心的最长回文串, 得到以这个字符为中心的切割次数, 与前面字符的切割次数作对比, 得到最小值即为答案
     *
     * 状态转移公示:
     * f(j) = min(f(j), f(i - 1) + 1)
     * 其中:
     * i  中心字符可以匹配到的最左边位置
     * j  中心字符可以匹配到的最右边位置
     *
     */
    @Test
    public void palindromePartitioning() {
        for (String s : TEST_STRINGS) {
            System.out.println(minCut(s));
        }
    }

    public int minCut(String s) {
        if (s == null || s.length() <= 1) {
            return 0;
        }
        int len = s.length();
        int dp[] = new int[len];
        // 假设每个字符都要分, 所以一共分 len - 1 次
        Arrays.fill(dp, len - 1);

        for (int i = 0; i < len; i++) {
            // 奇数回文串以1个字符为中心
            minCutHelper(s, i, i, dp);
            // 偶数回文串以2个字符为中心
            minCutHelper(s, i, i + 1, dp);
        }
        return dp[len - 1];
    }

    private void minCutHelper(String s, int i, int j, int[] dp) {
        int len = s.length();
        while (i >= 0 && j < len && s.charAt(i) == s.charAt(j)) {
            dp[j] = Math.min(dp[j], i == 0 ? 0 : dp[i - 1] + 1);
            i--;
            j++;
        }
    }

    public int minCut2(String s) {
        if (s == null || s.length() <= 1) {
            return 0;
        }
        int len = s.length();
        int dp[] = new int[len];
        // 假设每个字符都要分, 所以一共分 len - 1 次
        Arrays.fill(dp, len - 1);

        for (int i = 0; i < len; i++) {
            // 奇数回文串以1个字符为中心
            int tempLeft = i;
            int tempRight = i;
            while (tempLeft >= 0 && tempRight < len && s.charAt(tempLeft) == s.charAt(tempRight)) {
                dp[tempRight] = Math.min(dp[tempRight], tempLeft == 0 ? 0 : dp[tempLeft - 1] + 1);
                tempLeft--;
                tempRight++;
            }
            // 偶数回文串以2个字符为中心
            tempLeft = i;
            tempRight = i + 1;
            while (tempLeft >= 0 && tempRight < len && s.charAt(tempLeft) == s.charAt(tempRight)) {
                dp[tempRight] = Math.min(dp[tempRight], tempLeft == 0 ? 0 : dp[tempLeft - 1] + 1);
                tempLeft--;
                tempRight++;
            }
        }
        return dp[len - 1];
    }

}
