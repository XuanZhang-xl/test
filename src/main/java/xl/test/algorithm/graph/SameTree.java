package xl.test.algorithm.graph;

import org.junit.Test;
import xl.test.algorithm.TreeNode;
import xl.test.algorithm.utils.TreeNodeUtil;

/**
 * 相同的树
 *
 * 给定两个二叉树，编写一个函数来检验它们是否相同。
 *
 * 如果两个树在结构上相同，并且节点具有相同的值，则认为它们是相同的。
 *
 * 示例 1:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   3     2   3
 *
 *         [1,2,3],   [1,2,3]
 *
 * 输出: true
 * 示例 2:
 *
 * 输入:      1          1
 *           /           \
 *          2             2
 *
 *         [1,2],     [1,null,2]
 *
 * 输出: false
 * 示例 3:
 *
 * 输入:       1         1
 *           / \       / \
 *          2   1     1   2
 *
 *         [1,2,1],   [1,1,2]
 *
 * 输出: false
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/same-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * created by XUAN on 2019/11/15
 */
public class SameTree {

    @Test
    public void isSameTree() {
        TreeNode treeNode0 = TreeNodeUtil.buildTree(new int[]{1, 2, 3});
        //System.out.println(isSameTree(treeNode0, treeNode0));
        TreeNode treeNode1 = TreeNodeUtil.buildTree(new int[]{});
        TreeNode treeNode2 = TreeNodeUtil.buildTree(new int[]{0});
        System.out.println(isSameTree(treeNode1, treeNode2));
    }



    public boolean isSameTree(TreeNode p, TreeNode q) {
        if (p == null && q == null) {
            return true;
        }
        if ((p == null || q == null) || p.getVal() != q.getVal()) {
            return false;
        }
        if (p.getLeft() != null && q.getLeft() != null) {
            if (!isSameTree(p.getLeft(), q.getLeft())) {
                return false;
            }
        } else if (p.getLeft() == null && q.getLeft() == null) {
        } else {
            return false;
        }
        if (p.getRight() != null && q.getRight() != null) {
            if (!isSameTree(p.getRight(), q.getRight())) {
                return false;
            }
        } else if (p.getRight() == null && q.getRight() == null) {
        } else {
            return false;
        }
        return true;
    }

    public boolean isSameTree2(TreeNode p, TreeNode q) {
        if (p == null && q == null) return true;
        if (q == null || p == null) return false;
        if (p.getVal() != q.getVal()) return false;
        return isSameTree2(p.getRight(), q.getRight()) && isSameTree2(p.getLeft(), q.getLeft());
    }

}
