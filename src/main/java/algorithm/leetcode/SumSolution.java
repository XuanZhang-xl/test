package algorithm.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
     *
     *
     * 解决: 排序后双指针
     * @param nums
     * @return
     */
    @Test
    public void threeSum() {
        long begin = System.currentTimeMillis();
        //int[] nums = new int[]{12,13,-10,-15,4,5,-8,11,10,3,-11,4,-10,4,-7,9,1,8,-5,-1,-9,-4,3,-14,-11,14,0,-8,-6,-2,14,-9,-4,11,-8,-14,-7,-9,4,10,9,9,-1,7,-10,7,1,6,-8,12,12,-10,-7,0,-9,-3,-1,-1,-4,8,12,-13,6,-7,13,5,-14,13,12,6,8,-2,-8,-15,-10,-3,-1,7,10,7,-4,7,4,-4,14,3,0,-10,-13,11,5,6,13,-4,6,3,-13,8,1,6,-9,-14,-11,-10,8,-5,-6,-7,9,-11,7,12,3,-4,-7,-6,14,8,-1,8,-4,-11};
        //int[] nums = new int[]{0,3,0,1,1,-1,-5,-5,3,-3,-3,0};
        int[] nums = new int[]{-1,0,1,2,-1,-4};
        System.out.println(threeSum0(nums));
        long end = System.currentTimeMillis();
        System.out.println("use " + (end - begin));
    }
    public List<List<Integer>> threeSum0(int[] nums) {
        List<List<Integer>> result = new ArrayList<>();
        if (nums == null || nums.length < 3) {
            return result;
        }
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int first = nums[i];
            if (i > 0 && first == nums[i - 1]) {
                continue;
            }
            int begin = i+1;
            int end = nums.length - 1;
            while (begin < end) {
                int second = nums[begin];
                if (begin > 0 && begin - 1 != i && second == nums[begin - 1]) {
                    begin++;
                    continue;
                }
                int third = nums[end];
                if (end < nums.length - 1 && third == nums[end + 1]) {
                    end--;
                    continue;
                }
                int sum = first + second + third;
                if (sum < 0) {
                    begin++;
                } else if (sum == 0) {
                    result.add(Arrays.asList(first, second, third));
                    begin++;
                } else {
                    end--;
                }
            }
        }
        return result;
    }


    /**
     * 给定一个包括 n 个整数的数组 nums 和 一个目标值 target。找出 nums 中的三个整数，使得它们的和与 target 最接近。返回这三个数的和。假定每组输入只存在唯一答案。
     *
     * 例如，给定数组 nums = [-1，2，1，-4], 和 target = 1.
     *
     * 与 target 最接近的三个数的和为 2. (-1 + 2 + 1 = 2).
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/3sum-closest
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums
     * @param target
     * @return
     */
    @Test
    public void threeSumClosest() {
        int[] nums = new int[]{-55,-24,-18,-11,-7,-3,4,5,6,9,11,23,33};
        int target = 0;
        System.out.println(threeSumClosest0(nums, target));
    }

    public int threeSumClosest0(int[] nums, int target) {
        int result = 0;
        int absoluteDistance = 0;
        Arrays.sort(nums);
        for (int i = 0; i < nums.length - 2; i++) {
            int first = nums[i];
            if (i > 0 && first == nums[i - 1]) {
                continue;
            }
            int begin = i+1;
            int end = nums.length - 1;
            while (begin < end) {
                int second = nums[begin];
                if (begin > 0 && begin - 1 != i && second == nums[begin - 1]) {
                    begin++;
                    continue;
                }
                int third = nums[end];
                if (end < nums.length - 1 && third == nums[end + 1]) {
                    end--;
                    continue;
                }
                int sum = first + second + third;
                if (sum != target) {
                    int distance = Math.abs(target - sum);
                    if (absoluteDistance == 0 || absoluteDistance > distance) {
                        absoluteDistance = distance;
                        result = sum;
                    }
                    if (sum < target) {
                        begin++;
                    } else {
                        end--;
                    }
                } else {
                    return target;
                }
            }
        }
        return result;
    }


    public class ListNode {
        int val;
        ListNode next;
        ListNode(int x) { val = x; }
    }


    /**
     * 给出两个 非空 的链表用来表示两个非负的整数。其中，它们各自的位数是按照 逆序 的方式存储的，并且它们的每个节点只能存储 一位 数字。
     *
     * 如果，我们将这两个数相加起来，则会返回一个新的链表来表示它们的和。
     *
     * 您可以假设除了数字 0 之外，这两个数都不会以 0 开头。
     *
     * 示例：
     *
     * 输入：(2 -> 4 -> 3) + (5 -> 6 -> 4)
     * 输出：7 -> 0 -> 8
     * 原因：342 + 465 = 807
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/add-two-numbers
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param l1
     * @param l2
     * @return
     */
    @Test
    public void addTwoNumbers() {
        ListNode l11 = new ListNode(8);
        ListNode l21 = new ListNode(0);

        ListNode l12 = new ListNode(1);
        //ListNode l13 = new ListNode(3);
        l11.next = l12;
        //l12.next = l13;
        //
        //ListNode l22 = new ListNode(6);
        //ListNode l23 = new ListNode(4);
        //l21.next = l22;
        //l22.next = l23;
        ListNode node = addTwoNumbers0(l11, l21);
        while (node != null) {
            System.out.println(node.val);
            node = node.next;
        }
    }


    public ListNode addTwoNumbers0(ListNode l1, ListNode l2) {
        ListNode node = new ListNode(0);
        ListNode tempNode = node;
        ListNode tempNodel1 = l1;
        ListNode tempNodel2 = l2;
        while (tempNode != null) {
            tempNode = addTwoNumbers00(tempNodel1, tempNodel2, tempNode);
            tempNodel1 = tempNodel1 == null ? null : tempNodel1.next;
            tempNodel2 = tempNodel2 == null ? null : tempNodel2.next;
        }
        return node;
    }

    private ListNode addTwoNumbers00(ListNode l1, ListNode l2, ListNode node) {
        int l1Value = 0;
        if (l1 != null) {
            l1Value = l1.val;
        }
        int l2Value = 0;
        if (l2 != null) {
            l2Value = l2.val;
        }
        boolean advance = false;
        int rest = l1Value + l2Value + node.val;
        if (rest > 9) {
            rest = rest - 10;
            advance = true;
        }
        node.val = rest;
        if (advance) {
            // 进位必然继续
            ListNode newNode = new ListNode(1);
            node.next = newNode;
            return newNode;
        } else {
            //  不进位
            // 本身或next都是null, 返回null
            if (l1 == null && (l2 == null || (l2 != null && l2.next == null))) {
                return null;
            }
            if (l1 != null && l1.next == null && (l2 == null || (l2 != null && l2.next == null))) {
                return null;
            }
            else {
                ListNode newNode = new ListNode(0);
                node.next = newNode;
                return newNode;
            }
        }
    }
}
