package algorithm.leetcode;

import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * 给定一个字符串 s，将 s 分割成一些子串，使每个子串都是回文串。
 *
 * 返回 s 所有可能的分割方案。
 *
 * 示例:
 *
 * 输入: "aab"
 * 输出:
 * [
 *   ["aa","b"],
 *   ["a","a","b"]
 * ]
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/palindrome-partitioning
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/12
 */
public class PalindromePartitioning {

    @Test
    public void partition () {
        System.out.println(partition("aaaa"));
        System.out.println(partition("abccb"));
        System.out.println(partition("qwertyyyytrewq"));
        System.out.println(partition("aba"));
        System.out.println(partition("asdsaxssxasss"));
        System.out.println(partition("qwerty"));
    }



    public List<List<String>> partition(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        List<List<String>> result = new ArrayList<>();
        // 从第一个字符开始遍历, 获得第一个字符就是 subString(0, 1), 所以传入0,1
        partition(result, new ArrayList<>(), s, 0, 1);
        return result;
    }
    public void partition(List<List<String>> total, List<String> result, String s, int formerIndex,  int rearIndex) {
        if (s.length() == rearIndex) {
            List<String> copy = new ArrayList<>();
            copy.addAll(result);
            total.add(copy);
            return;
        }
        for (int i = rearIndex; i < s.length(); i++) {
            String former = s.substring(formerIndex, i);
            if (isPalindrome(former)) {
                result.add(former);
                partition(total, result, s, rearIndex, i + 1);
                result.remove(former);
            }
        }
    }



    public boolean isPalindrome (String s) {
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
}
