package xl.test.algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * created by XUAN on 2019/12/10
 */
public class UniqueBinarySearchTreesII {

    @Test
    public void generateTrees() {
        System.out.println(generateTrees(3));
        System.out.println(generateTrees(4));
        System.out.println(generateTrees(5));
    }

    public List<TreeNode> generateTrees(int n) {
        if (n == 0) {
            return new ArrayList<>();
        }
        List<TreeNode>[] dp = new List[n+1];
        // 初始化
        dp[0] = null;
        List<TreeNode> dp1 = new ArrayList<>();
        dp[1] = dp1;
        dp1.add(new TreeNode(1));

        if (n == 1) {
            return dp[1];
        }

        for (int i = 2; i <= n; i++) {
            for (int j = 1; j <= i; ++j) {
                List<TreeNode> left = dp[j - 1];
                List<TreeNode> right = dp[i - j];
                List<TreeNode> treeNodes = dp[i];
                if (treeNodes == null) {
                    treeNodes = new ArrayList<>();
                    dp[i] = treeNodes;
                }
                if (left == null) {
                    for (TreeNode treeNode : right) {
                        TreeNode current = new TreeNode(j);
                        TreeNode newTreeNode = addInt(j, treeNode);
                        current.right = newTreeNode;
                        treeNodes.add(current);
                    }
                } else if (right == null) {
                    for (TreeNode treeNode : left) {
                        TreeNode current = new TreeNode(j);
                        current.left = treeNode;
                        treeNodes.add(current);
                    }
                } else {
                    // 都不为空, 笛卡尔积
                    for (TreeNode treeNodeLeft : left) {
                        for (TreeNode treeNodeRight : right) {
                            TreeNode current = new TreeNode(j);
                            current.left = treeNodeLeft;
                            // 右边数值要重新处理 +j
                            TreeNode newTreeNode = addInt(j, treeNodeRight);
                            current.right = newTreeNode;
                            treeNodes.add(current);
                        }
                    }
                }
            }
        }
        return dp[n];
    }

    private TreeNode addInt(int i, TreeNode treeNodeRight) {
        TreeNode newNode = new TreeNode(treeNodeRight.val + i);
        addInt(newNode, i, treeNodeRight);
        return newNode;
    }
    private void addInt(TreeNode newNode, int i, TreeNode treeNode) {
        if (treeNode.left != null) {
            newNode.left = new TreeNode(treeNode.left.val + i);
            addInt(newNode.left, i, treeNode.left);
        }
        if (treeNode.right != null) {
            newNode.right = new TreeNode(treeNode.right.val + i);;
            addInt(newNode.right, i, treeNode.right);
        }
    }
}
