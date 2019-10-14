package xl.test.algorithm.leetcode.utils;

import org.springframework.util.StringUtils;

/**
 * created by XUAN on 2019/10/14
 */
public class StringUtil {

    public static boolean isPalindrome (String s) {
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

    private StringUtil() {
    }
}
