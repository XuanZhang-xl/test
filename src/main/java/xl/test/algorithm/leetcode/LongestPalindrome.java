package xl.test.algorithm.leetcode;

import org.junit.Test;
import xl.test.algorithm.utils.PairInt;

/**
 *
 * 给定一个字符串 s，找到 s 中最长的回文子串。你可以假设 s 的最大长度为 1000。
 *
 * 示例 1：
 *
 * 输入: "babad"
 * 输出: "bab"
 * 注意: "aba" 也是一个有效答案。
 *
 * 示例 2：
 *
 * 输入: "cbbd"
 * 输出: "bb"
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/longest-palindromic-substring
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/18
 */
public class LongestPalindrome {


    private static final String[] TEST_STRINGS = new String[]{
            "aaaa",
            "abccb",
            "qwertyyyytrewq",
            "aba",
            "asdsaxssxasss",
            "qwerty",
            "aabbaabbaacdfgfdccfdgssa"
    };

    @Test
    public void longestPalindrome() {
        for (String s : TEST_STRINGS) {
            System.out.println(longestPalindrome(s));
        }
    }


    public String longestPalindrome(String s) {
        if (s == null) {
            return null;
        }
        int len = s.length();
        if (len == 0 || len == 1) {
            return s;
        }
        PairInt pairInt = new PairInt(0, 0);
        for (int i = 0; i < len; i++) {
            // 奇数的情况
            subLongestPalindrome(pairInt, s, i, i);
            // 偶数的情况
            subLongestPalindrome(pairInt, s, i, i + 1);
        }
        return s.substring(pairInt.getFirst(), pairInt.getSecond() + 1);
    }

    private void subLongestPalindrome(PairInt pairInt, String s, int i, int j) {
        int len = s.length();
        while (i >= 0 && j < len && s.charAt(i) == s.charAt(j)) {
            if (pairInt.getSecond() - pairInt.getFirst() <= j - i) {
                pairInt.setFirst(i);
                pairInt.setSecond(j);
            }
            i--;
            j++;
        }
    }

}
