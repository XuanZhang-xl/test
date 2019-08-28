package leetcode;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

}
