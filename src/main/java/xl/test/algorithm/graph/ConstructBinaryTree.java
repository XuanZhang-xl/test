package xl.test.algorithm.graph;

import org.junit.Test;
import xl.test.algorithm.TreeNode;

import java.util.HashMap;
import java.util.Map;

/**
 * 根据 遍历结果 构造二叉树
 *
 * created by XUAN on 2019/11/15
 */
public class ConstructBinaryTree {

    int preIdx = 0;

    /**
     * 根据一棵树的前序遍历与中序遍历构造二叉树。
     *
     * 注意:
     * 你可以假设树中没有重复的元素。
     *
     * 例如，给出
     *
     * 前序遍历 preorder = [3,9,20,15,7]  中左右
     * 中序遍历 inorder = [9,3,15,20,7]   左中右
     * 返回如下的二叉树：
     *
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/construct-binary-tree-from-preorder-and-inorder-traversal
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     *
     */
    @Test
    public void buildTreeFromPreOrderAndInorder() {
        int[] preorder = new int[]{3,9,20,15,7};
        int[] inorder = new int[]{9,3,15,20,7};
        TreeNode treeNode = buildTreeFromPreOrderAndInorder(preorder, inorder);
        System.out.println(treeNode);
    }

    public TreeNode buildTreeFromPreOrderAndInorder(int[] preorder, int[] inorder) {
        preIdx = 0;
        Map<Integer, Integer> idxMap = new HashMap<Integer, Integer>();
        // build a hashmap value -> its index
        int idx = 0;
        for (Integer val : inorder)
            idxMap.put(val, idx++);
        return helper(idxMap,  preorder, 0, inorder.length);
    }

    public TreeNode helper(Map<Integer, Integer> idxMap, int[] preorder, int inLeft, int inRight) {
        // if there is no elements to construct subtrees
        if (inLeft == inRight)
            return null;

        // pick up pre_idx element as a root
        int rootVal = preorder[preIdx];
        TreeNode root = new TreeNode(rootVal);

        // root splits inorder list
        // into left and right subtrees
        int index = idxMap.get(rootVal);

        // recursion
        preIdx++;
        // build left subtree
        root.setLeft(helper(idxMap, preorder, inLeft, index));
        // build right subtree
        root.setRight(helper(idxMap, preorder, index + 1, inRight));
        return root;
    }

    /**
     * 根据一棵树的中序遍历与后序遍历构造二叉树。
     *
     * 注意:
     * 你可以假设树中没有重复的元素。
     *
     * 例如，给出
     *
     * 中序遍历 inorder = [9,3,15,20,7]    左中右
     * 后序遍历 postorder = [9,15,7,20,3]  左右中
     * 返回如下的二叉树：
     *
     *     3
     *    / \
     *   9  20
     *     /  \
     *    15   7
     *
     * 来源：力扣（LeetCode）
     * 链接：https://leetcode-cn.com/problems/construct-binary-tree-from-inorder-and-postorder-traversal
     * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
     */
    @Test
    public void buildTreeFromInOrderAndPostOrder() {
        int[] inorder = new int[]{9,3,15,20,7};
        int[] postorder = new int[]{9,15,7,20,3};
        TreeNode treeNode = buildTreeFromInOrderAndPostOrder(inorder, postorder);
        System.out.println(treeNode);
    }

    public TreeNode buildTreeFromInOrderAndPostOrder(int[] inorder, int[] postorder) {

        Map<Integer, Integer> idMap = new HashMap<>();
        for (int i = 0; i < inorder.length; i++) {
            idMap.put(inorder[i], i);
        }
        preIdx = inorder.length - 1;
        return helperInOrderAndPostOrder(idMap, postorder, 0, inorder.length);
    }

    private TreeNode helperInOrderAndPostOrder(Map<Integer, Integer> idMap, int[] postorder, int begin, int end) {
        if (begin == end) {
            return null;
        }
        Integer id = postorder[preIdx];
        TreeNode root = new TreeNode(id);
        Integer index = idMap.get(id);

        preIdx--;
        // 必须右边先遍历, 因为中在最后面, 所以必须从后往前看, 这么看的话, 右在左前面
        root.setRight(helperInOrderAndPostOrder(idMap, postorder, index + 1, end));
        root.setLeft(helperInOrderAndPostOrder(idMap, postorder, begin, index));
        return root;
    }
}
