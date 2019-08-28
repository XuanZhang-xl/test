package algorithm.leetcode.str;

import org.junit.Test;

/**
 * created by xzhang on 2017/08/13
 *
 */
public class FindLongestCommonPrefix {

    /*
    * Write a function to find the longest common prefix string amongst an array of strings.
    * 思路：
    * 1. 取前两位，获得最大前缀
    * 2. 取第三位，与前两位的最大前缀比较， 获得前三位最大前缀
    * 3. 照2遍历完这个数组
    * */
    String[] strArr = new String[]{"a4353","awefdd","awefvfv","awefde","aw422","awefvd","awefrf","awe323"};
    String resString;

    @Test
    public void findLongestCommonPrefix(){
        for (int i = 0; i <strArr[0].length(); i++) {
            if (strArr[0].charAt(i) == strArr[1].charAt(i)) {
                continue;
            }
            resString = strArr[0].substring(0, i);
            System.out.println(resString);
            break;
        }
        String result = findLongestCommonPrefix0(2);
        System.out.println(result);
    }

    private String findLongestCommonPrefix0(int index){
        if (index >= strArr.length) {
            return resString;
        }
        for (int i = 0; i < resString.length(); i++) {
            if (resString.charAt(i) == strArr[index].charAt(i)) {
                continue;
            }
            resString = resString.substring(0, i);
            break;
        }
        System.out.println(resString);
        findLongestCommonPrefix0(++index);
        return resString;
    }
}
