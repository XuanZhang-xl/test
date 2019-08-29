package algorithm.leetcode.str;

import org.junit.Test;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 题目地址:  https://leetcode-cn.com/problems/distinct-subsequences/
 * 题解:      https://mp.weixin.qq.com/s/w8045wiYQcC8jllhO-merg
 * 给定一个字符串 S 和一个字符串 T，计算在 S 的子序列中 T 出现的个数。
 * <p>
 * 一个字符串的一个子序列是指，通过删除一些（也可以不删除）字符且不干扰剩余字符相对位置所组成的新字符串。（例如，"ACE" 是 "ABCDE" 的一个子序列，而 "AEC" 不是）
 * <p>
 * 示例 1:
 * <p>
 * 输入: S = "rabbbit", T = "rabbit"
 * 输出: 3
 * 解释:
 * <p>
 * 如下图所示, 有 3 种可以从 S 中得到 "rabbit" 的方案。
 * (上箭头符号 ^ 表示选取的字符)
 * <p>
 * rabbbit
 * ^^^^ ^^
 * rabbbit
 * ^^ ^^^^
 * rabbbit
 * ^^^ ^^^
 * <p>
 * 示例 2:
 * <p>
 * 输入: S = "babgbag", T = "bag"
 * 输出: 5
 * 解释:
 * <p>
 * 如下图所示, 有 5 种可以从 S 中得到 "bag" 的方案。
 * (上箭头符号 ^ 表示选取的字符)
 * <p>
 * babgbag
 * ^^ ^
 * babgbag
 * ^^    ^
 * babgbag
 * ^    ^^
 * babgbag
 * ^  ^^
 * babgbag
 * ^^^
 * <p>
 * created by XUAN on 2019/8/27
 */
public class DistinctSubsequences {

    @Test
    public void numDistinct1() {
        //String s = "babgbag";
        //String t = "bag";
        String s = "babgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbag";
        String t = "bagbagbag";
        //long begin1 = System.currentTimeMillis();
        //int i1 = numDistinctBacktracking1(s, 0, t, 0);
        //long end1 = System.currentTimeMillis();
        //System.out.println("回溯方法1 共有" + i1 + "种方案, 用时" + (end1 - begin1));

        long begin2 = System.currentTimeMillis();
        int i2 = numDistinctBacktracking2(s, 0, t, 0);
        long end2 = System.currentTimeMillis();
        System.out.println("回溯方法2 共有" + i2 + "种方案, 用时" + (end2 - begin2));

        long begin3 = System.currentTimeMillis();
        int i3 = numDistinctBacktracking3(s, 0, t, 0);
        long end3 = System.currentTimeMillis();
        System.out.println("回溯方法3 共有" + i3 + "种方案, 用时" + (end3 - begin3));
    }

    /**
     * 使用回溯算法, 时间复杂度很高, 基本会超时
     *
     * @param s       母串
     * @param s_start 从母串s_start位置开始匹配
     * @param t       子串
     * @param t_start 从子串的t_start位置开始匹配
     * @return
     */
    private int numDistinctBacktracking1(String s, int s_start, String t, int t_start) {
        //T 是空串，表示匹配结束且匹配到了, 选法就是 1 种
        // 这个判断必须在前面, 因为当两个串都匹配完的时候, 是匹配到的, 需要返回1
        if (t_start == t.length()) {
            return 1;
        }
        //S 是空串，表示匹配结束且没匹配到, 选法是 0 种
        if (s_start == s.length()) {
            return 0;
        }
        int count = 0;
        //当前字符相等
        if (s.charAt(s_start) == t.charAt(t_start)) {
            // 这时候有两种可能
            // 1. 匹配完一个字符后, 去匹配下一个字符
            // 2. 母串在s_start这个字符后面, 还有另外的字符可以匹配t_start

            //从 S 选择当前的字符，此时 S 跳过这个字符, T 也跳过一个字符。
            count = numDistinctBacktracking1(s, s_start + 1, t, t_start + 1)
                    //S 不选当前的字符，此时 S 跳过这个字符，T 不跳过字符。
                    + numDistinctBacktracking1(s, s_start + 1, t, t_start);
            //当前字符不相等
        } else {
            //S 只能不选当前的字符，此时 S 跳过这个字符， T 不跳过字符。
            count = numDistinctBacktracking1(s, s_start + 1, t, t_start);
        }
        return count;
    }

    Map<String, Integer> cache = new HashMap<>();
    private int numDistinctBacktracking2(String s, int s_start, String t, int t_start) {
        if (t_start == t.length()) {
            return 1;
        }
        if (s_start == s.length()) {
            return 0;
        }
        Integer count = 0;
        int s_startTemp = s_start + 1;
        String key1 = s_startTemp + "_" + t_start;
        if (s.charAt(s_start) == t.charAt(t_start)) {
            int t_startTemp = t_start + 1;
            String key2 = s_startTemp + "_" + t_startTemp;
            Integer cachedCount1 = cache.get(key1);
            Integer cachedCount2 = cache.get(key2);
            if (cachedCount1 == null) {
                cachedCount1 = numDistinctBacktracking2(s, s_start + 1, t, t_start);
                cache.put(key1, cachedCount1);
            }
            if (cachedCount2 == null) {
                cachedCount2 = numDistinctBacktracking2(s, s_start + 1, t, t_start + 1);
                cache.put(key2, cachedCount2);
            }

            count = cachedCount1 + cachedCount2;
        } else {
            count = cache.get(key1);
            if (count == null) {
                count = numDistinctBacktracking2(s, s_start + 1, t, t_start);
                cache.put(key1, count);
            }
        }
        return count;
    }

    Map<String, Integer> cache2 = new HashMap<>();
    private int numDistinctBacktracking3(String s, int s_start, String t, int t_start) {
        if (t_start == t.length()) {
            return 1;
        }
        if (s_start == s.length()) {
            return 0;
        }
        int count = 0;
        String key = s_start + "_" + t_start;
        //先判断之前有没有求过这个解
        if (cache2.containsKey(key)) {
            return cache2.get(key);
        }
        if (s.charAt(s_start) == t.charAt(t_start)) {
            count = numDistinctBacktracking3(s, s_start + 1, t, t_start + 1)
                    + numDistinctBacktracking3(s, s_start + 1, t, t_start);
        } else {
            count = numDistinctBacktracking3(s, s_start + 1, t, t_start);
        }
        cache2.put(key, count);
        return count;
    }


    /**
     * 使用动态规划算法求解
     * <p>
     * 理解:
     * s = "babgbag"
     * t = "bag"
     * <p>
     *    *  b  a  b  g  b  a  g
     * *  1  1  1  1  1  1  1  1
     * b  0  1  1  2  2  3  3  3   这一行代表的是遍历s到某个字符的时候 b 的匹配次数
     * a  0  0  1  1  1  1  4  4   这一行代表的是遍历s到某个字符的时候 ba 的匹配次数
     * g  0  0  0  0  1  1  1  5   这一行代表的是遍历s到某个字符的时候 bag 的匹配次数
     * <p>
     * 初始化:
     * 1. t为空时, 表示可以匹配, 所以最后一列都是1, 代表匹配1次
     * 2. s为空时, 表示不匹配, 所以最后一行都是0, 代表不匹配
     */
    @Test
    public void numDistinct2() {
        String s = "babgbagbabgbag";
        //String s = "babgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbagbabgbag";
        //String t = "bagbagbag";
        String t = "bag";

        long begin1 = System.currentTimeMillis();
        int i1 = numDistinctDynamicProgramming1(s, t);
        long end1 = System.currentTimeMillis();
        System.out.println("动态规划方法1 共有" + i1 + "种方案, 用时" + (end1 - begin1));

        long begin2 = System.currentTimeMillis();
        int i2 = numDistinctDynamicProgramming2(s, t);
        long end2 = System.currentTimeMillis();
        System.out.println("动态规划方法2 共有" + i2 + "种方案, 用时" + (end2 - begin2));

        long begin3 = System.currentTimeMillis();
        int i3 = numDistinctDynamicProgramming3(s, t);
        long end3 = System.currentTimeMillis();
        System.out.println("动态规划方法3 共有" + i3 + "种方案, 用时" + (end3 - begin3));

        long begin4 = System.currentTimeMillis();
        int i4 = numDistinctDynamicProgramming4(s, t);
        long end4 = System.currentTimeMillis();
        System.out.println("动态规划方法4 共有" + i4 + "种方案, 用时" + (end4 - begin4));
    }

    public int numDistinctDynamicProgramming1(String s, String t) {
        int[][] dp = new int[t.length() + 1][s.length() + 1];
        //初始化第一行
        for (int j = 0; j <= s.length(); j++) {
            dp[0][j] = 1;
        }

        for (int i = 1; i <= t.length(); i++) {
            for (int j = 1; j <= s.length(); j++) {
                if (t.charAt(i - 1) == s.charAt(j - 1)) {
                    //对应于两种情况，选择当前字符和不选择当前字符
                    dp[i][j] = dp[i - 1][j - 1] + dp[i][j - 1];
                } else {
                    // 当前字符不相等, 不选择当前字符
                    dp[i][j] = dp[i][j - 1];
                }
            }
        }
        return dp[t.length()][s.length()];
    }


    /**
     * 二维换一维 严格按照二维的流程 参见上面矩阵
     * 只要额外一个变量记录上面方法1的 dp[i-1][j-1] 这个变量就可以, 这样可以大幅减少空间复杂度
     *
     * @param s
     * @param t
     * @return
     */
    public int numDistinctDynamicProgramming2(String s, String t) {
        int[] dp = new int[s.length() + 1];
        Arrays.fill(dp, 1);
        int pre = 1;
        //每行算一次
        for (int i = 1; i <= t.length(); i++) {
            //0-n算n+1次
            for (int j = 0; j <= s.length(); j++) {
                //先保存dp[j]下次用
                int temp = dp[j];
                if (j == 0) {
                    dp[j] = 0;
                } else {
                    if (t.charAt(i - 1) == s.charAt(j - 1)) {
                        dp[j] = pre + dp[j - 1];
                    } else {
                        dp[j] = dp[j - 1];
                    }
                }
                pre = temp;
            }
        }
        return dp[s.length()];
    }

    /**
     *
     * 这里是使用列作为基准统计 (列主序)
     * 正序, 倒序循环t, 是没有区别的
     * 下面的这优化方法也可以使用正序, 只要map存的是第一次出现位置, next存之后出现的位置, 最后出现位置存1就可
     * 时间复杂度O(NM)
     *
     *   循环 1  2  3  4  5  6  7
     *    *  b  a  b  g  b  a  g
     * *  1  1  1  1  1  1  1  1
     * b  0  1  1  2  2  3  3  3
     * a  0  0  1  1  1  1  4  4
     * g  0  0  0  0  1  1  1  5
     *
     * 1~7列为7次循环
     * 初始:    第一次循环b:  第二次循环a:  第三次循环b:  第四次循环g:  第五次循环b:  第六次循环a:  第七次循环g:
     * * 1       * 1         * 1         * 1           * 1          * 1          * 1          * 1
     * b 0       b 1         b 1         b 2           b 2          b 3          b 3          b 3
     * a 0       a 0         a 1         a 1           a 1          a 1          a 4          a 4
     * g 0       g 0         g 0         g 0           g 1          g 1          g 1          g 5
     *
     *
     *   循环 1  2  3  4  5  6  7  8  9  10 11 12 13 14 15
     *    *  b  a  b  g  b  a  g  b  a  b  g  b  a  g  t
     * *  1  1  1  1  1  1  1  1  1  1  1  1  1  1  1  1
     * b  0  1  1  2  2  3  3  3  4  4  5  5  6  6  6  6
     * a  0  0  1  1  1  1  4  4  4  8  8  8  8  14 14 14
     * g  0  0  0  0  1  1  1  5  5  5  5  13 13 13 27 27
     * b  0  0  0  0  0  1  1  1  6  6  11 11 24 24 24 24
     * t  0  0  0  0  0  0  0  0  0  0  0  0  0  0  0  24
     *
     *
     * @param s
     * @param t
     * @return
     */
    public int numDistinctDynamicProgramming3(String s, String t) {
        // dp[0]表示空串
        int[] dp = new int[t.length() + 1];
        // dp[0]永远是1，因为不管S多长，都只能找到一个空串，与T相等
        dp[0] = 1;

        // 倒序
        //for (int i = 0; i < s.length(); i++) {
        //    for (int j = t.length() - 1; j >= 0; j--) {
        //        if (t.charAt(j) == s.charAt(i)) {
        //            dp[j + 1] += dp[j];
        //        }
        //    }
        //}

        // 正序
        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < t.length(); j++) {
                if (t.charAt(j) == s.charAt(i)) {
                    dp[j + 1] += dp[j];
                }
            }
        }
        return dp[t.length()];
    }

    /**
     * 列主序 先构造字典 就不用遍历t了
     * 这样就优化成了答案上的2ms的了
     * 时间复杂度O(N) ~ O(NM)
     *
     * @param s
     * @param t
     * @return
     */
    public int numDistinctDynamicProgramming4(String s, String t) {
        // dp[0]表示空串
        int[] dp = new int[t.length() + 1];
        // dp[0]永远是1，因为不管S多长，都只能找到一个空串，与T相等
        dp[0] = 1;

        //t的字典, 取128是因为 所有字符的ASCII码绝对不会超过128
        int[] map = new int[128];
        Arrays.fill(map, -1);

        // 从尾部遍历的时候可以遍历 next类似链表 无重复值时为-1，
        // 有重复时例如从rabbit的b开始索引在map[b] = 2 next[2] 指向下一个b的索引为3
        // 这里的map[]与next[]数组都是为了代替下面这个循环+判断
        // for (int j = t.length() - 1; j >= 0; j--) {
        //     if (t.charAt(j) == s.charAt(i)) {
        //        dp[j + 1] += dp[j];
        //     }
        // }
        // 这段代码的寻址就可以从map[s.charAt(i)] 找到索引j 在用next[j] 一直找和 s.charAt(i)相等的字符 其他的就可以跳过了
        int[] nexts = new int[t.length()];
        for (int i = 0; i < t.length(); i++) {
            int c = t.charAt(i);
            // 如果字符第一次出现, 则对应位置存 -1 , 如果第n次出现, 则存第n-1次(也就是上一次)出现时的位置, 比如
            //"bagbagbag", 则nexts为 [-1, -1, -1, 0, 1 ,2 ,3 ,4 ,5]
            nexts[i] = map[c];
            // 这一行代码使map里存的是字符最后一次在t中出现的位置, 比如 b字符在"bagbagbag"里最后一次出现是在6位, 则 map[98] = 6
            map[c] = i;
        }

        for (int i = 0; i < s.length(); i++) {
            // 从s中拿出字符, 通过map[]找到该字符在t中出现的最后位置, 再通过nexts[]找出该字符出现的所有位置, 就可以跳过所有无意义的比较判断
            // 当遍历到该字符第一次出现时, j=-1, 退出循环
            for (int j = map[s.charAt(i)]; j >= 0; j = nexts[j]) {
                dp[j + 1] += dp[j];
            }
        }
        return dp[t.length()];
    }

    public static void main(String[] args){
        String data = "abc";
        char char1 = data.charAt(0);
        char char2 = data.charAt(1);
        char char3 = data.charAt(2);
        int int1 = data.charAt(0);
        int int2 = data.charAt(1);
        int int3 = data.charAt(2);
        int int21 = data.getBytes()[0];
        int int22 = data.getBytes()[1];
        int int23 = data.getBytes()[2];

        System.out.println(char1);
        System.out.println(char2);
        System.out.println(char3);

        System.out.println(int1);
        System.out.println(int2);
        System.out.println(int3);

        System.out.println(int21);
        System.out.println(int22);
        System.out.println(int23);

    }

}
