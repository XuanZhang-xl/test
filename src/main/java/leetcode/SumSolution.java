package leetcode;

import algorithm.Sort;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * created by XUAN on 2019/7/29
 */
public class SumSolution {

    /**
     * 给定一个整数数组 nums 和一个目标值 target，请你在该数组中找出和为目标值的那 两个 整数，并返回他们的数组下标。
     *
     * 你可以假设每种输入只会对应一个答案。但是，你不能重复利用这个数组中同样的元素。
     *
     * 示例:
     *
     * 给定 nums = [2, 7, 11, 15], target = 9
     *
     * 因为 nums[0] + nums[1] = 2 + 7 = 9
     * 所以返回 [0, 1]
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/two-sum
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @param target
     * @return
     */
    @Test
    public void twoSum() {
        //int[] nums = new int[]{2, 54, 4, 11, 1, 100, 6, 156};
        int[] nums = new int[]{3,2,4};
        //int target = 256;
        int target = 6;
        int[] ints = twoSum0(nums, target);
        System.out.println(ints[0]);
        System.out.println(ints[1]);
    }
    public int[] twoSum0(int[] nums, int target) {
        Map<Integer, Integer> map = new HashMap<>();
        for (int i = 0; i < nums.length; i++) {
            Integer num = nums[i];
            map.put(num, i);
        }
        for (int i = 0; i < nums.length; i++) {
            int rest = target - nums[i];
            Integer restInteger = Integer.valueOf(rest);
            Integer index = map.get(restInteger);
            if (index == null) {
                continue;
            }
            int indexInt = index.intValue();
            if (map.containsKey(restInteger) && i != indexInt) {
                return new int[]{i, indexInt};
            }
        }
        return null;
    }


    /**
     * Given an array nums of n integers, are there elements a, b, c in nums such that a + b + c = 0? Find all unique triplets in the array which gives the sum of zero.
     *
     * Note:
     *
     * The solution set must not contain duplicate triplets.
     *
     * Example:
     *
     * Given array nums = [-1, 0, 1, 2, -1, -4],
     *
     * A solution set is:
     * [
     *   [-1, 0, 1],
     *   [-1, -1, 2]
     * ]
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3sum
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @return
     */
    @Test
    public void threeSum() {
        long begin = System.currentTimeMillis();
        //int[] nums = new int[]{12,13,-10,-15,4,5,-8,11,10,3,-11,4,-10,4,-7,9,1,8,-5,-1,-9,-4,3,-14,-11,14,0,-8,-6,-2,14,-9,-4,11,-8,-14,-7,-9,4,10,9,9,-1,7,-10,7,1,6,-8,12,12,-10,-7,0,-9,-3,-1,-1,-4,8,12,-13,6,-7,13,5,-14,13,12,6,8,-2,-8,-15,-10,-3,-1,7,10,7,-4,7,4,-4,14,3,0,-10,-13,11,5,6,13,-4,6,3,-13,8,1,6,-9,-14,-11,-10,8,-5,-6,-7,9,-11,7,12,3,-4,-7,-6,14,8,-1,8,-4,-11};
        int[] nums = new int[]{0,3,0,1,1,-1,-5,-5,3,-3,-3,0};
        System.out.println(threeSum0(nums));
        long end = System.currentTimeMillis();
        System.out.println("use " + (end - begin));
    }
    public List<List<Integer>> threeSum0(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        Arrays.sort(nums);
        for (int i = 0; i < nums.length; i++) {
            int first = nums[i];
            int begin = 0;
            int end = nums.length - 1;
            while (true) {
                if (begin == i) {
                    begin++;
                    continue;
                }
                if (end == i) {
                    end++;
                    continue;
                }
                if (begin == end) {
                    break;
                }
                int second = nums[begin];
                if (begin > 0 && begin - 1 != i && second == nums[begin - 1]) {
                    continue;
                }
                int third = nums[end];
                if (end < nums.length - 1 && end + 1 != i && third == nums[end + 1]) {
                    continue;
                }
                if (first + second + third == 0) {
                    List<Integer> answer = new ArrayList<>(first);
                    result.add(answer);
                }
            }
        }
        return result;
    }
}
