package algorithm;

import java.util.ArrayList;
import java.util.List;

/**
 * created by XUAN on 2019/5/23
 */
public class BuildTree {

    /**
     * 获得一个tree的根节点
     * @param num
     * @return
     */
    public static TreeNode getTree (Integer num) {
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
