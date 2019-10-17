package xl.test.algorithm.leetcode;

import org.junit.Test;
import xl.test.algorithm.leetcode.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 给定一个非空字符串 s，最多删除一个字符。判断是否能成为回文字符串。
 *
 * 示例 1:
 *
 * 输入: "aba"
 * 输出: True
 *
 * 示例 2:
 *
 * 输入: "abca"
 * 输出: True
 * 解释: 你可以删除c字符。
 *
 * 注意:
 *
 *     字符串只包含从 a-z 的小写字母。字符串的最大长度是50000。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/valid-palindrome-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/17
 */
public class IsPalindromeII {

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
        for (String s : TEST_STRINGS) {
            System.out.println(isPalindrome(s));
        }
    }

    public boolean isPalindrome (String s) {
        int i = 0;
        int j = s.length() - 1;
        while (i < j) {
            if (s.charAt(i) != s.charAt(j)) {
                return StringUtil.isPalindrome(s.substring(i, j)) || StringUtil.isPalindrome(s.substring(i + 1, j + 1));
            }
            i++;
            j--;
        }
        return true;
    }

}
