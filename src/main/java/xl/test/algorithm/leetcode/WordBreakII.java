package xl.test.algorithm.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 给定一个非空字符串 s 和一个包含非空单词列表的字典 wordDict，在字符串中增加空格来构建一个句子，使得句子中所有的单词都在词典中。返回所有这些可能的句子。
 *
 * 说明：
 *
 *     分隔时可以重复使用字典中的单词。
 *     你可以假设字典中没有重复的单词。
 *
 * 示例 1：
 *
 * 输入:
 * s = "catsanddog"
 * wordDict = ["cat", "cats", "and", "sand", "dog"]
 * 输出:
 * [
 *   "cats and dog",
 *   "cat sand dog"
 * ]
 *
 * 示例 2：
 *
 * 输入:
 * s = "pineapplepenapple"
 * wordDict = ["apple", "pen", "applepen", "pine", "pineapple"]
 * 输出:
 * [
 *   "pine apple pen apple",
 *   "pineapple pen apple",
 *   "pine applepen apple"
 * ]
 * 解释: 注意你可以重复使用字典中的单词。
 *
 * 示例 3：
 *
 * 输入:
 * s = "catsandog"
 * wordDict = ["cats", "dog", "sand", "and", "cat"]
 * 输出:
 * []
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/word-break-ii
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/10/22
 */
public class WordBreakII {


    /**
     * 回溯算法
     */
    @Test
    public void dp () {
        System.out.println(wordBreak("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa")));
        System.out.println(wordBreak("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog")));
        System.out.println(wordBreak("pineapplepenapple", Arrays.asList("apple", "pen", "applepen", "pine", "pineapple")));
        System.out.println(wordBreak("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
    }


    public List<String> wordBreak(String s, List<String> wordDict) {
        List<String>[] dp = new ArrayList[s.length() + 1];
        List<String> first = new ArrayList<>();
        first.add(0, "");
        dp[0] = first;
        List<String> backtracking = wordBreakDp(s, wordDict, dp);
        if (backtracking == null) {
            return new ArrayList<>();
        } else {
            return backtracking.stream().map(res -> res.substring(0, res.length() - 1)).collect(Collectors.toList());
        }
    }

    public List<String> wordBreakDp(String s, List<String> wordDict, List<String>[] dp) {
        for (int i = 1; i <= s.length(); i++) {
            for (int j = 0; j < i; j++) {
                List<String> dpJ = dp[j];
                if (dpJ == null) {
                    continue;
                }
                String substring = s.substring(j, i);
                if (wordDict.contains(substring)) {
                    List<String> dpI = dp[i];
                    if (dpI == null) {
                        dpI = new ArrayList<>();
                        dp[i] = dpI;
                    }
                    for (String s1 : dpJ) {
                        dpI.add(s1 + substring + " ");
                    }
                }
            }
        }
        return dp[s.length()];
    }


    /**************************上面解法超时且超出内存限制******************************/

    public List<String> wordBreakII(String s, List<String> wordDict) {
        List<String> list = new ArrayList<>();
        List<String> wordList = new ArrayList<>();
        // 先判断有没有可能被拆分... {@link xl.test.algorithm.leetcode.WordBreak}
        if(wordBreak_check(s,wordDict)){
            add(list, wordList, s, wordDict);
        }
        return list;
    }
    private void add(List<String> list,List<String> wordList,String s,List<String> wordDict){
        for(String str : wordDict){
            if(s.startsWith(str)){
                if(s.length() == str.length()){
                    StringBuilder b = new StringBuilder();
                    for(String word : wordList){
                        b.append(word).append(" ");
                    }
                    b.append(str);
                    list.add(b.toString());
                }else{
                    wordList.add(str);
                    add(list, wordList, s.substring(str.length()), wordDict);
                    wordList.remove(wordList.size()-1);
                }
            }
        }
    }

    public boolean wordBreak_check(String s, List<String> wordDict) {
        int maxWordLength = 0;//字典中单词最长长度
        for(int i=0;i<wordDict.size();i++){
            maxWordLength = Math.max(maxWordLength,wordDict.get(i).length());
        }
        boolean[] dp = new boolean [s.length()+1];
        dp[0] = true;
        for(int i=1;i<s.length()+1;i++){
            int x = i-maxWordLength>0?i-maxWordLength:0;
            for(int j=x;j<i;j++){
                if(dp[j]&&wordDict.contains(s.substring(j,i))){//s存在以第j位为末尾的单词并且截取第j到i位的单词存在于字典中
                    dp[i] = true;
                    break;
                }
            }
        }
        return dp[s.length()];
    }


    /**************************上面题解有针对特定字符串优化, 下面为官方题解***********************************/


    HashMap<Integer, List<String>> map = new HashMap<>();

    public List<String> wordBreak3(String s, Set<String> wordDict) {
        return word_Break(s, wordDict, 0);
    }

    /**
     * 我们可以看出许多子问题的求解都是冗余的，也就是我们对于相同的子串调用了函数多次。
     *
     * 为了避免这种情况，我们使用记忆化的方法，我们使用一个 key:valuekey:value 这样的哈希表来进行优化。
     * 在哈希表中， keykey 是当前考虑字符串的开始下标， valuevalue 包含了所有从头开始的所有可行句子。
     * 下次我们遇到相同位置开始的调用时，我们可以直接从哈希表里返回结果，而不需要重新计算结果。
     *
     * 通过记忆化的方法，许多冗余的子问题都可以被省去，回溯树得到了剪枝，所以极大程度降低了时间复杂度。
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/word-break-ii/solution/dan-ci-chai-fen-ii-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     * @param s
     * @param wordDict
     * @param start
     * @return
     */
    public List<String> word_Break(String s, Set<String> wordDict, int start) {
        if (map.containsKey(start)) {
            return map.get(start);
        }
        LinkedList<String> res = new LinkedList<>();
        if (start == s.length()) {
            res.add("");
        }
        for (int end = start + 1; end <= s.length(); end++) {
            if (wordDict.contains(s.substring(start, end))) {
                List<String> list = word_Break(s, wordDict, end);
                for (String l : list) {
                    res.add(s.substring(start, end) + (l.equals("") ? "" : " ") + l);
                }
            }
        }
        map.put(start, res);
        return res;
    }

    /**
     * 官方 dp题解
     * 也超时
     * @param s
     * @param wordDict
     * @return
     */
    public List<String> wordBreak4(String s, List<String> wordDict) {
        LinkedList<String>[] dp = new LinkedList[s.length() + 1];
        LinkedList<String> initial = new LinkedList<>();
        initial.add("");
        dp[0] = initial;
        for (int i = 1; i <= s.length(); i++) {
            LinkedList<String> list = new LinkedList<>();
            for (int j = 0; j < i; j++) {
                if (dp[j].size() > 0 && wordDict.contains(s.substring(j, i))) {
                    for (String l : dp[j]) {
                        list.add(l + (l.equals("") ? "" : " ") + s.substring(j, i));
                    }
                }
            }
            dp[i] = list;
        }
        return dp[s.length()];
    }

    @Test
    public void dp2() {
        System.out.println(wordBreak4("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaabaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
                Arrays.asList("a","aa","aaa","aaaa","aaaaa","aaaaaa","aaaaaaa","aaaaaaaa","aaaaaaaaa","aaaaaaaaaa")));
        System.out.println(wordBreak4("catsanddog", Arrays.asList("cat", "cats", "and", "sand", "dog")));
        System.out.println(wordBreak4("pineapplepenapple", Arrays.asList("apple", "pen", "applepen", "pine", "pineapple")));
        System.out.println(wordBreak4("catsandog", Arrays.asList("cats", "dog", "sand", "and", "cat")));
    }

}
