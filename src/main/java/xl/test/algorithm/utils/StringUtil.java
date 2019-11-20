package xl.test.algorithm.utils;

import org.springframework.util.StringUtils;

/**
 * created by XUAN on 2019/10/14
 */
public class StringUtil {

    /**
     * 判断字符串是否是回文
     * @param s
     * @return
     */
    public static boolean isPalindrome (String s) {
        if (StringUtils.isEmpty(s)) {
            return false;
        }
        if (s.length() == 1) {
            return true;
        }
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) {
                return false;
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * 获得最长回文公共子串的长度
     * 详见{link xl.test.algorithm.DynamicProgramming}
     * @param s
     * @param t
     * @return
     */
    public static int longestSubString(String s, String t){
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
        return dp[t.length()][s.length()];
    }


    private StringUtil() {
    }
}
