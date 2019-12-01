package xl.test.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定 n 个非负整数表示每个宽度为 1 的柱子的高度图，计算按此排列的柱子，下雨之后能接多少雨水。
 *
 * 上面是由数组 [0,1,0,2,1,0,1,3,2,1,2,1] 表示的高度图，在这种情况下，可以接 6 个单位的雨水（蓝色部分表示雨水）。 感谢 Marcos 贡献此图。
 *
 * 示例:
 *
 * 输入: [0,1,0,2,1,0,1,3,2,1,2,1]
 * 输出: 6
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/trapping-rain-water
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * @author XUAN
 * @since 2019/12/01
 */
public class TrappingRainWater {


    @Test
    public void trap() {
        int[] height = {0,1,0,2,1,0,1,3,2,1,2,1};
        System.out.println(trap(height));
        System.out.println(trapDualPointer(height));
    }


    /**
     * 双指针法
     *
     * 从动态编程方法的示意图中我们注意到，只要 rightMax[i]>leftMax[i]（元素 0 到元素 6），积水高度将由 left_max 决定，
     * 类似地 leftMax[i]>rightMax[i]（元素 8 到元素 11）。
     * 所以我们可以认为如果一端有更高的条形块（例如右端），积水的高度依赖于当前方向的高度（从左到右）。
     * 当我们发现另一侧（右侧）的条形块高度不是最高的，我们则开始从相反的方向遍历（从右到左）。
     * 我们必须在遍历时维护 leftMax 和 rightMax ，但是我们现在可以使用两个指针交替进行，实现 1 次遍历即可完成。
     *
     * 作者：LeetCode
     * 链接：https://leetcode-cn.com/problems/trapping-rain-water/solution/jie-yu-shui-by-leetcode/
     * 来源：力扣（LeetCode）
     * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
     *
     * @param height
     * @return
     */
    private int trapDualPointer(int[] height) {
        int left = 0, right = height.length - 1;
        int ans = 0;
        int leftMax = 0, rightMax = 0;
        while (left < right) {
            if (height[left] < height[right]) {
                if (height[left] >= leftMax) {
                    leftMax = height[left];
                } else {
                    ans += (leftMax - height[left]);
                }
                ++left;
            } else {
                if (height[right] >= rightMax) {
                    rightMax = height[right];
                }  else {
                    ans += (rightMax - height[right]);
                }
                --right;
            }
        }
        return ans;
    }

    /**
     * 遍历每个位置上可以有多少雨水
     * @param height
     * @return
     */
    public int trap(int[] height) {
        // 找出向右看列表
        int[] rightTarget = findRightTarget(height);
        // 找出向左看列表
        int[] leftTarget = findLeftTarget(height);
        // 总和
        int total = 0;

        // 遍历第一和最后外的每个位置
        for (int i = 1; i < height.length - 1; i++) {
            // 左边或右边没有比当前更高的, 说明当前蓄水量是0
            if (leftTarget[i] <= height[i] || rightTarget[i] <= height[i]) {
                continue;
            }
            // 左右都有. 选低的
            total = total + Math.min(leftTarget[i], rightTarget[i]) - height[i];
        }
        return total;
    }

    private int[] findLeftTarget(int[] height) {
        int[] res = new int[height.length];
        int temp = 0;
        for (int i = height.length - 1; i >= 0; i--) {
            if (height[i] > temp) {
                temp = height[i];
            }
            res[i] = temp;
        }
        return res;
    }

    private int[] findRightTarget(int[] height) {
        int[] res = new int[height.length];
        int temp = 0;
        for (int i = 0; i < height.length; i++) {
            if (height[i] > temp) {
                temp = height[i];
            }
            res[i] = temp;
        }
        return res;
    }
}
