package xl.test.algorithm.leetcode;

import org.junit.Before;
import org.junit.Test;
import xl.test.algorithm.TreeNode;
import xl.test.algorithm.leetcode.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**<p>
 * 给定一个二叉树，判断其是否是一个有效的二叉搜索树。
 *
 * 假设一个二叉搜索树具有如下特征：
 *
 * 节点的左子树只包含小于当前节点的数。
 * 节点的右子树只包含大于当前节点的数。
 * 所有左子树和右子树自身必须也是二叉搜索树。
 * 示例 1:
 *
 * 输入:
 *     2
 *    / \
 *   1   3
 * 输出: true
 * 示例 2:
 *
 * 输入:
 *     5
 *    / \
 *   1   4
 *      / \
 *     3   6
 * 输出: false
 * 解释: 输入为: [5,1,4,null,null,3,6]。
 *      根节点的值为 5 ，但是其右子节点值为 4 。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/validate-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * </p>
 *
 * created by XUAN on 2019/10/18
 */
public class ValidateBinarySearchTree {

    List<TreeNode> nodes = new ArrayList<>();

    @Test
    public void isValidBST() {
        for (TreeNode node : nodes) {
            System.out.println(isValidBST(node));
        }
    }

    public boolean isValidBST(TreeNode root) {
        return checkTreeNode(root, root.getVal());
    }

    private boolean checkLeftRecursion(TreeNode leftNode, int val) {
        if (leftNode.getVal() >= val) {
            return false;
        }
        return checkTreeNode(leftNode, val);
    }

    private boolean checkRightRecursion(TreeNode rightNode, int val) {
        if (rightNode.getVal() <= val) {
            return false;
        }
        return checkTreeNode(rightNode, val);
    }

    private boolean checkTreeNode(TreeNode node, int val) {
        boolean left = true;
        boolean right = true;
        if (node.getLeft() != null) {
            left = checkLeftRecursion(node.getLeft(), node.getVal());
        }
        if (node.getRight() != null) {
            right = checkRightRecursion(node.getRight(), node.getVal());
        }
        return left && right;
    }

    /******************上面方法不行, 获取不到parent的parent节点*****************/


    int last = Integer.MIN_VALUE;
    @Test
    public void isValidBST2() {
        for (TreeNode node : nodes) {
            last = Integer.MIN_VALUE;
            System.out.println(isValidBST2(node));
        }
    }
    public boolean isValidBST2(TreeNode node) {
        if (node == null) {
            return true;
        }
        if (isValidBST2(node.getLeft())) {
            if (last < node.getVal()) {
                last = node.getVal();
                return isValidBST2(node.getRight());
            }
        }
        return false;
    }






    @Before
    public void buildTree() {
        nodes.add(TreeNodeUtil.buildTree(new Integer[]{2, 1, 3}));
        nodes.add(TreeNodeUtil.buildTree(new Integer[]{5, 1, 4, null, null, 3, 6}));
        nodes.add(TreeNodeUtil.buildTree(new Integer[]{10, 5, 15, null, null, 6, 20}));
    }

}
