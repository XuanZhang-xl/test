package algorithm.leetcode;

import algorithm.leetcode.utils.StringUtil;
import org.junit.Test;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * created by XUAN on 2019/04/26
 */
public class FindAllSymmetryString {


    // 找出字字符串中所有的对称子字符串
    public static void main(String[] args) {
        /**
         * 结果应该有:
         * aa
         * bb
         * aa
         * bb
         * aa
         *
         * abba
         * aabbaa
         * cdfgfdc
         * dfgfd
         * fgf
         * cc
         * ss
         */
        String data = "aabbaabbaacdfgfdccfdgssa";

        /**
         * 方法1:
         * 最直观的方法:
         * 找出所有子字符串, 遍历判断是否为对称字符串
         */


        /**
         * 方法2:
         * 使用正则表达式
         */

        /**
         * 方法3:
         * 找到规律: 由于是对称, 则必然出现相同字符
         */
        Map<String, Integer> subDatas = new HashMap<String, Integer>();
        for (int i = 1; i == data.length(); i++) {
            String subData = data.substring(0, i);

        }

    }



    /******************2019/10/14 终于可以做出来了， 回溯算法或动态规划*****************/


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
    public void allSymmetry () {
        for (String s : TEST_STRINGS) {
            System.out.println(allSymmetryBacktracking(s));
        }
    }

    /**
     * 回溯算法
     */
    public List<String> allSymmetryBacktracking (String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        Set<String> result = new HashSet<>();
        // 从第一个字符开始遍历, 获得第一个字符就是 subString(0, 1), 所以传入0,1
        allSymmetryBacktracking0(result, new ArrayList<>(), s, 0, 1);
        return new ArrayList<>(result);
    }
    public void allSymmetryBacktracking0(Set<String> result, List<String> temp, String s, int formerIndex, int rearIndex) {
        if (s.length() + 1 == rearIndex) {
            return;
        }
        for (int i = rearIndex; i < s.length() + 1; i++) {
            String former = s.substring(formerIndex, i);
            if (StringUtil.isPalindrome(former)) {
                temp.add(former);
                result.add(former);
                allSymmetryBacktracking0(result, temp, s, i, i + 1);
                temp.remove(temp.size() - 1);
            }
        }
    }

    /**
     * 动态规划
     */
    @Test
    public void allSymmetryDynamicProgramming () {
        for (String s : TEST_STRINGS) {
            System.out.println(allSymmetryDynamicProgramming(s));
        }
    }

    /**
     * 动态规划的重点是找到前后关系, 根据前面的结果简化后面的计算 以降低复杂度
     *
     * 这里, 需要找到一个回文串和另一个回文串之间的关系
     *
     * 回文串之间可以是这样的关系
     *
     * 1. 字符串(S)中的回文串以某个字符为中心(回文串长度为奇数), 假设中心字符在第n位, 则可以说, S[n - i] = S[n + i], 也就是n前i位必然等于n的后i位
     * 或以某两个字符为中心(这两个字符(n, n+1)必然一样, 回文串长度为偶数), 则  S[n - i] = S[n + 1 + i]
     *
     * 2. TODO: 还可以找到别的回文串的规律吗? 待高手补充
     *
     * 根据关系1, 可以构建 dp
     * 假设dp[i][j] 表示字符串从第i位到j位是否为回文串, 则dp为上三角矩阵
     * 如果 dp[i][j]为回文串, 则计算dp[i - 1][j + 1]时只需要判断 S[i - 1]是否等于S[j + 1]即可
     *
     * @param s
     * @return
     */
    private Set<String> allSymmetryDynamicProgramming(String s) {
        if (StringUtils.isEmpty(s)) {
            return null;
        }
        Set<String> result = new HashSet<>();
        int length = s.length();
        boolean[][] dp = new boolean[length][length];
        for (int i = 0; i < length; i++) {
            boolean[] line = dp[i];
            // 回文串长度为奇数
            // 单个字符必然是回文
            line[i] = true;
            result.add(s.substring(i, i + 1));
            int offset = 1;
            while (i - offset >= 0 && i + offset < length && dp[i - offset + 1][i + offset - 1]) {
                if (s.charAt(i - offset) == s.charAt(i + offset)) {
                    // 包头包尾, 所以后面+1
                    result.add(s.substring(i - offset, i + offset + 1));
                    dp[i - offset][i + offset] = true;
                }
                offset++;
            }

            // 回文串长度为偶数
            // 最后一个字符, 没有偶数串了
            if (i == length - 1) {
                continue;
            }
            if (s.charAt(i) == s.charAt(i + 1)) {
                line[i + 1] = true;
                result.add(s.substring(i, i + 2));
            }
            offset = 1;
            while (i - offset >= 0 && i + offset + 1 < length && dp[i - offset + 1][i + offset]) {
                if (s.charAt(i - offset) == s.charAt(i + offset + 1)) {
                    // 包头包尾, 所以后面+1
                    result.add(s.substring(i - offset, i + offset + 2));
                    dp[i - offset][i + offset + 1] = true;
                }
                offset++;
            }
        }
        return result;
    }

    @Test
    public void test () {
        for (String s : TEST_STRINGS) {
            System.out.print("回溯算法 ");
            System.out.println(allSymmetryBacktracking(s));
            System.out.print("动态规划 ");
            System.out.println(allSymmetryDynamicProgramming(s));
            System.out.println();
        }
    }
}
