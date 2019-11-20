package xl.test.algorithm.graph;

import org.apache.commons.math3.util.Pair;
import org.junit.Test;
import xl.test.algorithm.TreeNode;
import xl.test.algorithm.utils.TreeNodeUtil;

import java.util.LinkedList;
import java.util.Queue;

/**
 * 给定一个二叉树，找出其最大深度。
 *
 * 二叉树的深度为根节点到最远叶子节点的最长路径上的节点数。
 *
 * 说明: 叶子节点是指没有子节点的节点。
 *
 * 示例：
 * 给定二叉树 [3,9,20,null,null,15,7]，
 *
 *     3
 *    / \
 *   9  20
 *     /  \
 *    15   7
 * 返回它的最大深度 3 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/maximum-depth-of-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/11/15
 */
public class MaximumDepthOfBinaryTree {

    @Test
    public void maxDepth() {
        TreeNode treeNode = TreeNodeUtil.buildTree(new Integer[]{3, 9, 20, null, null, 15, 7});
        System.out.println(maxDepth(treeNode));
        System.out.println(maxDepth2(treeNode));
    }



    public int maxDepth(TreeNode root) {
        return maxDepthRecursion(root, 0);
    }
    public int maxDepthRecursion(TreeNode node, int max) {
        if (node == null) {
            return max;
        }
        return Math.max(maxDepthRecursion(node.getLeft(), max + 1), maxDepthRecursion(node.getRight(), max + 1));

    }

    public int maxDepth2(TreeNode root) {
        Queue<Pair<TreeNode, Integer>> stack = new LinkedList<>();
        if (root != null) {
            stack.add(new Pair(root, 1));
        }

        int depth = 0;
        while (!stack.isEmpty()) {
            Pair<TreeNode, Integer> current = stack.poll();
            root = current.getKey();
            int current_depth = current.getValue();
            if (root != null) {
                depth = Math.max(depth, current_depth);
                stack.add(new Pair(root.getLeft(), current_depth + 1));
                stack.add(new Pair(root.getRight(), current_depth + 1));
            }
        }
        return depth;
    }
}
