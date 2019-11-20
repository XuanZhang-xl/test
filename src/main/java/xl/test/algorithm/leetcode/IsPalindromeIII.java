package xl.test.algorithm.leetcode;

import org.junit.Test;
import xl.test.algorithm.utils.StringUtil;

/**
 *
 *  给出一个字符串 s 和一个整数 k，请你帮忙判断这个字符串是不是一个「K 回文」。
 *
 *  所谓「K 回文」：如果可以通过从字符串中删去最多 k 个字符将其转换为回文，那么这个字符串就是一个「K 回文」。
 *
 *  示例：
 *
 *  输入：s = "abcdeca", k = 2
 *  输出：true
 *  解释：删除字符 “b” 和 “e”。
 *
 *  提示：
 *
 *  1 <= s.length <= 1000
 *  s 中只含有小写英文字母
 *  1 <= k <= s.length
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-palindrome-iii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/17
 */
public class IsPalindromeIII {

    private static final String[] TEST_STRINGS = new String[]{
            "aaaa",
            "abccb",
            "qwertyyyytrewq",
            "aba",
            "asdsaxssxasss",
            "qwerty",
            "cbbbcc",
            "abcdabcdabcdabcdabcdabcdabcdabcddcbadcbadcbadcbadcbadcbadcbadcba"
    };

    @Test
    public void isPalindrome () {
        int k = 4;
        for (String s : TEST_STRINGS) {
            System.out.println(isPalindrome(s, k, 0));
        }
    }


    public boolean isPalindrome (String s, int k, int currentK) {
        if (k < currentK) {
            return false;
        }
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) {
                return isPalindrome(s.substring(i, j), k, currentK + 1) || isPalindrome(s.substring(i + 1, j + 1), k, currentK + 1);
            }
            i++;
            j--;
        }
        return true;
    }

    /**
     * 思路2: 求s和反转s的最长公共子序列即可
     */
    @Test
    public void isPalindrome2 () {
        int k = 4;
        for (String s : TEST_STRINGS) {
            System.out.println(isPalindrome2(s, k));
        }
    }

    private boolean isPalindrome2(String s, int k) {
        StringBuilder sb = new StringBuilder();
        for (int i = s.length() - 1; i >= 0 ; i--) {
            sb.append(s.charAt(i));
        }
        return s.length() <= k + StringUtil.longestSubString(s, sb.toString());
    }

}
