package xl.test.algorithm.leetcode;

import org.junit.Test;
import xl.test.algorithm.leetcode.str.Trie;

import java.util.Arrays;
import java.util.List;

/**
 * 给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，判定 s 是否可以被空格拆分为一个或多个在字典中出现的单词。
 *
 * 说明：
 *
 *     拆分时可以重复使用字典中的单词。
 *     你可以假设字典中没有重复的单词。
 *
 * 示例 1：
 *
 * 输入: s = "leetcode", wordDict = ["leet", "code"]
 * 输出: true
 * 解释: 返回 true 因为 "leetcode" 可以被拆分成 "leet code"。
 *
 * 示例 2：
 *
 * 输入: s = "applepenapple", wordDict = ["apple", "pen"]
 * 输出: true
 * 解释: 返回 true 因为 "applepenapple" 可以被拆分成 "apple pen apple"。
 *      注意你可以重复使用字典中的单词。
 *
 * 示例 3：
 *
 * 输入: s = "catsandog", wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/word-break
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/21
 */
public class WordBreak {

    @Test
    public void wordBreak() {
        System.out.println(wordBreak("aaaaaaa", Arrays.asList("aaaa", "aaa")));
        System.out.println(wordBreak("aaaaaaa", Arrays.asList("aaaa", "aa")));
        System.out.println(wordBreak("leetcode", Arrays.asList("leet", "code")));
        System.out.println(wordBreak("applepenapple", Arrays.asList("apple", "pen")));
        System.out.println(wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
        System.out.println(wordBreak("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab", Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa")));

    }

    /**
     * 时间复杂度太高
     * @param s
     * @param wordDict
     * @return
     */
    public boolean wordBreak(String s, List<String> wordDict) {
        return Trie.buildTrie(wordDict).wordBreak(s);
    }


    /**
     * dp[i] 代表s[0] ~ s[i] 可否被完全匹配
     *
     * 0:false 1:true
     *
     *  aaaaaaa 匹配 ["aaaa", "aaa"]
     * 10011011
     *
     *  aaaaaaa 匹配 ["aaaa", "aa"]
     * 10101010
     *
     *  leetcode 匹配 ["leet", "code"]
     * 100010001
     *
     *  catsandog 匹配 ["cats", "dog", "sand", "and", "cat"]
     * 1001100100
     *
     * dp[i] = dp[j] && s.substring(j, i) is matched
     *
     */
    @Test
    public void wordBreak2() {
        System.out.println(wordBreak2("aaaaaaa", Arrays.asList("aaaa", "aaa")));
        System.out.println(wordBreak2("aaaaaaa", Arrays.asList("aaaa", "aa")));
        System.out.println(wordBreak2("leetcode", Arrays.asList("leet", "code")));
        System.out.println(wordBreak2("applepenapple", Arrays.asList("apple", "pen")));
        System.out.println(wordBreak2("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
        System.out.println(wordBreak2("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaab", Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa")));

    }
    public boolean wordBreak2(String s, List<String> wordDict) {
        // 可以类比于背包问题
        int n = s.length();
        // memo[i] 表示 s 中以 i - 1 结尾的字符串是否可被 wordDict 拆分
        boolean[] memo = new boolean[n + 1];
        memo[0] = true;
        for (int i = 1; i <= n; i++) {
            for (int j = 0; j < i; j++) {
                if (memo[j] && wordDict.contains(s.substring(j, i))) {
                    memo[i] = true;
                    break;
                }
            }
        }
        return memo[n];
    }

}
