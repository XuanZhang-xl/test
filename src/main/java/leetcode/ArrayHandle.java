package leetcode;

import org.junit.Test;

/**
 * 数组处理
 * created by XUAN on 2019/8/1
 */
public class ArrayHandle {


    /**
     * 给定两个大小为 m 和 n 的有序数组 nums1 和 nums2。
     *
     * 请你找出这两个有序数组的中位数，并且要求算法的时间复杂度为 O(log(m + n))。
     *
     * 你可以假设 nums1 和 nums2 不会同时为空。
     *
     * 示例 1:
     *
     * nums1 = [1, 3]
     * nums2 = [2]
     *
     * 则中位数是 2.0
     *
     * 示例 2:
     *
     * nums1 = [1, 2]
     * nums2 = [3, 4]
     *
     * 则中位数是 (2 + 3)/2 = 2.5
     *
     * TODO:失败了
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/median-of-two-sorted-arrays
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     * @param nums1
     * @param nums2
     * @return
     */

    @Test
    public void findMedianSortedArrays() {
        int[] nums1 = new int[]{1, 2, 3};
        int[] nums2 = new int[]{1, 2, 2};
        //int[] nums1 = new int[]{1, 3};
        //int[] nums2 = new int[]{2};
        //int[] nums1 = new int[]{1};
        //int[] nums2 = new int[]{1};
        //int[] nums1 = new int[]{2};
        //int[] nums2 = new int[]{-2, -1};
        //int[] nums1 = new int[]{};
        //int[] nums2 = new int[]{1, 2, 3,4,5,6};
        //int[] nums1 = new int[]{1};
        //int[] nums2 = new int[]{3, 4};
        //int[] nums1 = new int[]{1,2,3,4,5,6,7,8};
        //int[] nums2 = new int[]{3, 4};
        System.out.println(findMedianSortedArrays0(nums1, nums2));
    }

    public double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int totalLength = nums1.length + nums2.length;
        boolean odd = (totalLength & 1) == 1;
        if (nums1.length == 0) {
            return odd ? nums2[nums2.length / 2] : ((double) (nums2[nums2.length / 2 - 1] + nums2[nums2.length / 2])) / 2;
        }
        if (nums2.length == 0) {
            return odd ? nums1[nums1.length / 2] : ((double) (nums1[nums1.length / 2 - 1] + nums1[nums1.length / 2])) / 2;
        }
        // 被排除的数量这么多就停止循环, 奇数取最大值就是中位数, 偶数取最大两位除二就是中位数
        int excludeNum = totalLength - totalLength / 2 - 1;
        // 当前被排除的数量
        int tempExclude = 0;

        int iExclude = 0;
        int jExclude = 0;

        Boolean iBig = null;
        // i,j左右两边的数的数量加起来永远相等或差1
        int i = nums1.length / 2;
        int j = nums2.length / 2;

        while (tempExclude != excludeNum) {
            if (nums1[i] < nums2[j]) {
                // i右移, j左移
                jExclude = nums2.length - j - 1;
                tempExclude = tempExclude + jExclude;
                if (iBig == null) {
                    iBig = false;
                }
                if (iBig) {
                    break;
                }
                j = j / 2;
                i = i + (nums1.length - i) / 2;
            } else {
                // i左移, j右移
                iExclude = nums1.length - i - 1;
                tempExclude = tempExclude + iExclude;
                if (iBig == null) {
                    iBig = true;
                }
                if (!iBig) {
                    break;
                }
                if (i == 0) {
                    // i已达最小,
                }
                i = i / 2;
                j = j + (nums2.length - j) / 2;
            }
        }
        if (odd) {
            // 奇数
            if (jExclude == nums2.length) {
                return nums1[i];
            }
            if (iExclude == nums1.length) {
                return nums2[j];
            }
            return (double) Math.max(nums1[i], nums2[j]);
        } else {
            // 偶数
            int i1 = nums1[i];
            int j1 = nums2[j];
            int medium = i1 + j1;
            if (i - 1 >= 0 && medium < nums1[i] + nums1[i - 1]) {
                medium = nums1[i] + nums1[i - 1];
            }
            if (j - 1 >= 0 && medium < nums2[j] + nums2[j - 1]) {
                medium = nums2[j] + nums2[j - 1];
            }
            return ((double) medium) / 2;
        }
    }

    /**
     * 考虑二分。可以发现，如果两个序列归并，那么第一个中位数之前会有tot=(n+m-1)/2个数。
     * 考虑这些数的来源，设第一个序列的前p个数，和第二个序列的前tot-p个数构成了这些数。
     * 对p二分，设当前值为mid，判定方法是如果第一个序列下标为mid的数小于第二个序列下标为tot-mid-1的数，则说明p太小。
     *
     *  首先我们在第一个数组里选一个位置进行切分，共有n + 1种切法，
     *  第i种切法表示左边有i个元素，对应的，切割线左边的第一个元素是nums1[i - 1]，右边第一个是nums1[i]。
     *  当我们确定了第一个数组的切割点之后，就可以确定数组二的一个唯一的切割点，
     * @param nums1
     * @param nums2
     * @return
     */
    public double findMedianSortedArrays0 (int[] nums1, int[] nums2) {
        int n = nums1.length;
        int m = nums2.length;
        if(n == 0 && m == 0) {
            return 0.0;
        }
        int pos = (n + m - 1) >> 1;
        int l = Math.max(0, pos - m);
        int r = Math.min(pos, n);
        while(l < r){
            int mid = (l + r) >> 1;
            if(nums1[mid] < nums2[pos - mid - 1]) {
                l = mid + 1;
            } else {
                r = mid;
            }
        }
        // 判断总长奇偶性
        if(((n + m) & 1) == 1) {
            if(l >= n) {
                return nums2[pos - l];
            }
            if(pos - l >= m) {
                return nums1[l];
            }
            if(nums1[l] < nums2[pos - l]) {
                return nums1[l];
            }
            return nums2[pos - l];
        }else{
            if(l >= n) {
                return (nums2[pos - l] + nums2[pos - l + 1]) / 2;
            }
            if(pos - l >= m) {
                return (nums1[l] + nums1[l + 1]) / 2;
            }
            if(pos - l + 1 < m && nums1[l] > nums2[pos - l + 1]) {
                return (nums2[pos - l] + nums2[pos - l + 1]) / 2;
            } else if(l + 1 < n && nums2[pos - l] > nums1[l + 1]) {
                return (nums1[l] + nums1[l + 1]) / 2;
            } else {
                return (nums1[l] + nums2[pos - l]) / 2;
            }
        }
    }
}
