package xl.test.algorithm.leetcode;

import xl.test.algorithm.leetcode.utils.StringUtil;
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

    private static final String[] TEST_STRINGS = new String[]{
            //"aaaa",
            //"abccb",
            //"qwertyyyytrewq",
            //"aba",
            //"asdsaxssxasss",
            //"qwerty",
            "cbbbcc"
    };


    /**
     * 回溯算法
     */
    @Test
    public void partitionBacktracking () {
        for (String s : TEST_STRINGS) {
            System.out.println(partitionBacktracking(s));
        }
    }

    public List<List<String>> partitionBacktracking(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        List<List<String>> result = new ArrayList<>();
        // 从第一个字符开始遍历, 获得第一个字符就是 subString(0, 1), 所以传入0,1
        partitionBacktracking0(result, new ArrayList<>(), s, 0, 1);
        return result;
    }
    private void partitionBacktracking0(List<List<String>> total, List<String> result, String s, int formerIndex, int rearIndex) {
        if (s.length() + 1 == rearIndex) {
            List<String> copy = new ArrayList<>();
            result.forEach(sub -> copy.add(sub));
            total.add(copy);
            return;
        }
        for (int i = rearIndex; i < s.length() + 1; i++) {
            String former = s.substring(formerIndex, i);
            if (StringUtil.isPalindrome(former)) {
                result.add(former);
                partitionBacktracking0(total, result, s, i, i + 1);
                result.remove(result.size() - 1);
            }
        }
    }
}
