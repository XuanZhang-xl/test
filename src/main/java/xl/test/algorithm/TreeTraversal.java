package xl.test.algorithm;

import org.junit.Test;
import xl.test.algorithm.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 树的左中右序遍历
 *
 * 左中右序的判断标准是中间节点什么时候被遍历
 * created by XUAN on 2019/05/06
 */
public class TreeTraversal {


    /**
     * 前序遍历
     *
     * 中--> 左 --> 右
     */
    @Test
    public void frontBinaryTreeTraversal () {
        TreeNode root = TreeNodeUtil.buildTree(10);
        frontTraversal(root);
    }

    private void frontTraversal(TreeNode node) {
        System.out.println(node.getData());
        if (node.getLeft() != null) {
            frontTraversal(node.getLeft());
        }
        if (node.getRight() != null) {
            frontTraversal(node.getRight());
        }
    }

    /**
     * 中序遍历
     * 左子树-->中间节点-->右子树
     */
    @Test
    public void middleBinaryTreeTraversal () {
        TreeNode root = TreeNodeUtil.buildTree(10);
        middleTraversal(root);
    }

    private void middleTraversal(TreeNode node) {
        if (node.getLeft() != null) {
            middleTraversal(node.getLeft());
        }
        System.out.println(node.getData());
        if (node.getRight() != null) {
            middleTraversal(node.getRight());
        }
    }

    /**
     * 右序遍历(后序遍历)
     *
     * 左-->右-->中
     */
    @Test
    public void rightBinaryTreeTraversal () {
        TreeNode root = TreeNodeUtil.buildTree(10);
        rightTraversal(root);
    }

    private void rightTraversal(TreeNode node) {
        if (node.getLeft() != null) {
            rightTraversal(node.getLeft());
        }
        if (node.getRight() != null) {
            rightTraversal(node.getRight());
        }
        System.out.println(node.getData());
    }

    /**
     * 中序遍历, 如果是null的话会把null打印出来,
     *
     * 叶子节点的null不会打印,
     */
    @Test
    public void frontBinaryTreeTraversalWithNull () {
        TreeNode one = new TreeNode(1);
        TreeNode two = new TreeNode(2);
        TreeNode three = new TreeNode(3);
        three.left = one;
        one.right = two;
        System.out.println(TreeNodeUtil.getIntArray(three));
    }
}
