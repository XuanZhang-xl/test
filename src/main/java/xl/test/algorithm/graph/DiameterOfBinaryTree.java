package xl.test.algorithm.graph;

import org.junit.Test;
import xl.test.algorithm.TreeNode;
import xl.test.algorithm.utils.TreeNodeUtil;

/**
 * 给定一棵二叉树，你需要计算它的直径长度。一棵二叉树的直径长度是任意两个结点路径长度中的最大值。这条路径可能穿过根结点。
 *
 * 示例 :
 * 给定二叉树
 *
 *           1
 *          / \
 *         2   3
 *        / \
 *       4   5
 * 返回 3, 它的长度是路径 [4,2,1,3] 或者 [5,2,1,3]。
 *
 * 注意：两结点之间的路径长度是以它们之间边的数目表示。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/diameter-of-binary-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/11/21
 */
public class DiameterOfBinaryTree {

    @Test
    public void diameterOfBinaryTree() {
        TreeNode treeNode = TreeNodeUtil.buildTree(new int[]{1, 2, 3, 4, 5});
        System.out.println(diameterOfBinaryTree(treeNode));
        treeNode = TreeNodeUtil.buildTree(new Integer[]{4,-7,-3,null,null,-9,-3,9,-7,-4,null,6,null,-6,-6,null,null,0,6,5,null,9,null,null,-1,-4,null,null,null,-2});
        System.out.println(diameterOfBinaryTree(treeNode));
    }

    public int diameterOfBinaryTree(TreeNode root) {
        if (root == null) {
            return 0;
        }
        // 由于是算边的数量, 所以要 - 1, 但是最后要算root节点的左右两边, 所以要+2, 就扯平了
        int count =  diameterOfBinaryTreeRecursion(root.left) - 1 + diameterOfBinaryTreeRecursion(root.right) - 1 + 2;
        count = Math.max(count, diameterOfBinaryTree(root.left));
        count = Math.max(count, diameterOfBinaryTree(root.right));
        // 比较当前数量, 左边数量, 右边数量, 返回最大值
        return count;
    }

    /**
     * 获得节点及其子节点的个数
     * @param root
     * @return
     */
    public int diameterOfBinaryTreeRecursion(TreeNode root) {
        if (root == null) {
            return 0;
        }
        return Math.max(diameterOfBinaryTreeRecursion(root.left), diameterOfBinaryTreeRecursion(root.right)) + 1;
    }

    /**
     * 官方答案
     * 怎么能写这么简洁
     */
    @Test
    public void diameterOfBinaryTree2() {
        TreeNode treeNode = TreeNodeUtil.buildTree(new int[]{1, 2, 3, 4, 5});
        System.out.println(diameterOfBinaryTree2(treeNode));
    }

    int ans;
    public int diameterOfBinaryTree2(TreeNode root) {
        ans = 1;
        depth(root);
        return ans - 1;
    }
    public int depth(TreeNode node) {
        if (node == null) return 0;
        int L = depth(node.left);
        int R = depth(node.right);
        ans = Math.max(ans, L+R+1);
        return Math.max(L, R) + 1;
    }
}
