package algorithm;

import org.junit.Test;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * 找出三个数之和等于目标数字
 * created by XUAN on 2019/04/29
 */
public class FindThreeInt {


    @Test
    public void findThreeInt () {
        int[] ints = new int[] {5,2,4,12,14,7,3,13,1,3,17,18};
        int target = 50;
        Set<Integer> set = new HashSet<>();
        for (int i = 0; i < ints.length; i++) {
            Set<Integer> set2 = new HashSet<>(set);
            for (int j = i + 1; j < ints.length; j++) {
                int res = target - ints[i] - ints[j];
                if (set2.contains(res)) {
                    System.out.println("i " + ints[i] + "  j " + ints[j] + "  res " + res);
                    System.out.println("成功");
                    return;
                }
                set2.add(ints[j]);
            }
            set.add(ints[i]);
        }
        System.out.println("失败");
    }
}
