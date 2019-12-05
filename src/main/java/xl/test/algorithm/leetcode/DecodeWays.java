package xl.test.algorithm.leetcode;

import org.junit.Test;

/**
 * 一条包含字母 A-Z 的消息通过以下方式进行了编码：
 *
 * 'A' -> 1
 * 'B' -> 2
 * ...
 * 'Z' -> 26
 *
 * 给定一个只包含数字的非空字符串，请计算解码方法的总数。
 *
 * 示例 1:
 *
 * 输入: "12"
 * 输出: 2
 * 解释: 它可以解码为 "AB"（1 2）或者 "L"（12）。
 *
 * 示例 2:
 *
 * 输入: "22"
 * 输出: 2
 * 解释: 它可以解码为 2 2, 22
 *
 * 输入: "226"
 * 输出: 3
 * 解释: 它可以解码为 2 2 6, 2 26,  22 6。

 * 输入: "2266"
 * 输出: 3
 * 解释: 它可以解码为 2 2 6 6, 2 26 6, 22 6 6
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/decode-ways
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/12/5
 */
public class DecodeWays {

    @Test
    public void numDecodings() {
        //String s230 = "230";
        //System.out.println(numDecodings(s230));
        //String s311 = "101";
        //System.out.println(numDecodings(s311));
        //String s31 = "01";
        //System.out.println(numDecodings(s31));
        String s3 = "12";
        System.out.println(numDecodings(s3));
        String s1 = "226";
        System.out.println(numDecodings(s1));
        String s2 = "2266";
        System.out.println(numDecodings(s2));
        String s = "4213214";
        System.out.println(numDecodings(s));
    }

    public int numDecodings(String s) {
        if (s == null || s.length() == 0 || "0".equals(s)) {
            return 0;
        }
        int length = s.length();
        if (length == 1) {
            return 1;
        }
        // sb测试用例还有"00" 这种吗真tmd sb
        if (s.startsWith("0")) {
            return 0;
        }

        // 擦, 不能有俩连续的0
        for (int i = 1; i < s.length() - 1; i++) {
            if (s.charAt(i) == '0' && s.charAt(i + 1) == '0') {
                return 0;
            }
        }

        int[] dp = new int[s.length()];
        // 初始化
        dp[0] = 1;
        for (int i = 1; i < s.length(); i++) {
            Integer temp = Integer.valueOf(s.substring(i - 1, i + 1));
            int currentNum = Integer.valueOf(s.substring(i, i + 1));
            if (currentNum == 0) {
                if (temp > 26) {
                    return 0;
                }
                if (i == 1) {
                    dp[i] = 1;
                } else {
                    dp[i] = dp[i - 2];
                }
                continue;
            }
            int formerNum = Integer.valueOf(s.substring(i - 1, i));
            if (temp > 26 || formerNum == 0) {
                dp[i] = dp[i - 1];
            } else {
                if (i == 1) {
                    dp[i] = 2;
                } else {
                    dp[i] = dp[i - 2] + dp[i - 1];
                }
            }
        }
        return dp[length - 1];
    }


}
