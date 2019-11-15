package xl.test.algorithm.leetcode;

import org.junit.Test;

/**
 * 给你一个字符串 s 和一个字符规律 p，请你来实现一个支持 '.' 和 '*' 的正则表达式匹配。
 *
 * '.' 匹配任意单个字符
 * '*' 匹配零个或多个前面的那一个元素
 * 所谓匹配，是要涵盖 整个 字符串 s的，而不是部分字符串。
 *
 * 说明:
 *
 * s 可能为空，且只包含从 a-z 的小写字母。
 * p 可能为空，且只包含从 a-z 的小写字母，以及字符 . 和 *。
 * 示例 1:
 *
 * 输入:
 * s = "aa"
 * p = "a"
 * 输出: false
 * 解释: "a" 无法匹配 "aa" 整个字符串。
 * 示例 2:
 *
 * 输入:
 * s = "aa"
 * p = "a*"
 * 输出: true
 * 解释: 因为 '*' 代表可以匹配零个或多个前面的那一个元素, 在这里前面的元素就是 'a'。因此，字符串 "aa" 可被视为 'a' 重复了一次。
 * 示例 3:
 *
 * 输入:
 * s = "ab"
 * p = ".*"
 * 输出: true
 * 解释: ".*" 表示可匹配零个或多个（'*'）任意字符（'.'）。
 * 示例 4:
 *
 * 输入:
 * s = "aab"
 * p = "c*a*b"
 * 输出: true
 * 解释: 因为 '*' 表示零个或多个，这里 'c' 为 0 个, 'a' 被重复一次。因此可以匹配字符串 "aab"。
 * 示例 5:
 *
 * 输入:
 * s = "mississippi"
 * p = "mis*is*p*."
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/regular-expression-matching
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 *
 * .*的含义是 : .表示任意字符，*表示它前面的字符重复0到多次，在这里也就是.重复多次，.*可以匹配所有的字符串
 * 那为什么 .** 语法错误? *不可以代表重复多次*么?
 *
 * created by XUAN on 2019/11/5
 */
public class PatternLeetcode {

    @Test
    public void isMatch(){
        //String[] s = new String[]{"aa","aa","ab","aab","mississippi"};
        //String[] p = new String[]{"a","a*",".*","c*a*b","mis*is*p*."};
        String[] s = new String[]{"aab"};
        String[] p = new String[]{"c*a*b"};
        for (int i = 0; i < s.length; i++) {
            System.out.println(isMatch(s[i], p[i]));
        }
    }

    public boolean isMatch(String s,String p){
        if (s == null || p == null) {
            return false;
        }
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];
        dp[0][0] = true;//dp[i][j] 表示 s 的前 i 个是否能被 p 的前 j 个匹配
        for (int i = 0; i < p.length(); i++) { // here's the p's length, not s's
            if (p.charAt(i) == '*' && dp[0][i - 1]) {
                dp[0][i + 1] = true; // here's y axis should be i+1
            }
        }
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) {
                //如果是任意元素 或者是对于元素匹配
                if (p.charAt(j) == '.' || p.charAt(j) == s.charAt(i)) {
                    dp[i + 1][j + 1] = dp[i][j];
                }
                if (p.charAt(j) == '*') {
                    //如果前一个元素不匹配 且不为任意元素
                    if (p.charAt(j - 1) != s.charAt(i) && p.charAt(j - 1) != '.') {
                        // 只能忽略 第j+1个元素及之后的*(也就是当*代表重复0次的情况), 看忽略之后是否匹配
                        dp[i + 1][j + 1] = dp[i + 1][j - 1];
                    } else {
                        // 以下三种情况符合一种即可
                        // dp[i][j] = dp[i-1][j]   忽略s的第i个元素看是否匹配, 如果匹配, 则由于*的存在, 不妨多一个与第i-1个元素一样的第i个元素, 也就是当*代表重复多次的情况
                        // dp[i][j] = dp[i][j-1]   忽略p的第j个元素看是否匹配, 也就是忽略*, 也就是当*代表一次的情况
                        // dp[i][j] = dp[i][j-2]   忽略p的第j及j-1个元素看是否匹配, 也就是当*代表零次的情况
                        dp[i + 1][j + 1] = dp[i + 1][j] || dp[i][j + 1] || dp[i + 1][j - 1];
                    }
                }
            }
        }
        return dp[s.length()][p.length()];
    }

}
