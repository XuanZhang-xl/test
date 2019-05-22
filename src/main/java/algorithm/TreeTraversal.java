package algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 树的左右序遍历
 * created by XUAN on 2019/05/06
 */
public class TreeTraversal {


    @Test
    public void binaryTreeTraversal () {
        TreeNode root = getTree(20);
        leftTraversal(root);
    }

    private void leftTraversal(TreeNode node) {
        System.out.println(node.getData());
        // 两if换个位置就是左右序遍历
        if (node.getRight() != null) {
            leftTraversal(node.getRight());
        }
        if (node.getLeft() != null) {
            leftTraversal(node.getLeft());
        }
    }

    private static TreeNode getTree (Integer num) {
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
        System.out.println(nodes.size());
        return root;
    }
}
