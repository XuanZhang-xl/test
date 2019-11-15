package xl.test.algorithm.leetcode.utils;

import xl.test.algorithm.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * created by XUAN on 2019/10/18
 */
public class TreeNodeUtil {



    /**
     * 获得一个tree的根节点
     * @param data
     * @return
     */
    public static TreeNode buildTree(Integer[] data) {
        if (data == null) {
            return null;
        }
        if (data.length == 0) {
            return new TreeNode();
        }
        TreeNode root = new TreeNode(data[0]);
        int length = data.length;
        TreeNode[] nodes = new TreeNode[length];
        nodes[0] = root;
        for (int i = 1; i < length; i++) {
            if (data[i] == null) {
                continue;
            }
            TreeNode node = new TreeNode();
            node.setVal(data[i]);
            // 获得父节点
            TreeNode parentNode = nodes[((i + 1) >> 1) - 1];
            if (parentNode == null) {
                throw new RuntimeException("传入的数组无法组成二叉树!");
            }
            // 偶数为右节点,奇数为左节点
            if (i % 2 == 0) {
                parentNode.setRight(node);
            } else {
                parentNode.setLeft(node);
            }
            // 将节点放入list方便获得父节点
            nodes[i] = node;
        }
        return root;
    }


    /**
     * 获得一个tree的根节点
     * @param data
     * @return
     */
    public static TreeNode buildTree(int[] data) {
        if (data == null || data.length == 0) {
            return null;
        }
        TreeNode root = new TreeNode(data[0]);
        int length = data.length;
        TreeNode[] nodes = new TreeNode[length];
        nodes[0] = root;
        for (int i = 1; i < length; i++) {
            TreeNode node = new TreeNode();
            node.setVal(data[i]);
            // 获得父节点
            TreeNode parentNode = nodes[((i + 1) >> 1) - 1];
            // 偶数为右节点,奇数为左节点
            if (i % 2 == 0) {
                parentNode.setRight(node);
            } else {
                parentNode.setLeft(node);
            }
            // 将节点放入list方便获得父节点
            nodes[i] = node;
        }
        return root;
    }

    /**
     * 获得一个顺序 tree的根节点
     * @param num
     * @return
     */
    public static TreeNode buildTree(Integer num) {
        if (num < 0) {
            throw new RuntimeException("传入的数字不可小于0!");
        }
        // 创建根节点
        TreeNode root = new TreeNode();
        root.setData("0");
        List<TreeNode> nodes = new ArrayList<TreeNode>();
        nodes.add(root);
        for (int i = 1; i <= num; i++) {
            TreeNode node = new TreeNode();
            node.setData(String.valueOf(i));
            // 获得父节点
            TreeNode parentNode = nodes.get(((i + 1) >> 1) - 1);
            // 偶数为右节点,奇数为左节点
            if (i % 2 == 0) {
                parentNode.setRight(node);
            } else {
                parentNode.setLeft(node);
            }
            // 将节点放入list方便获得父节点
            nodes.add(node);
        }
        return root;
    }

}
