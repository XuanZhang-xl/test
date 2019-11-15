package xl.test.algorithm.graph;

import org.junit.Test;
import xl.test.algorithm.TreeNode;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个单链表，其中的元素按升序排序，将其转换为高度平衡的二叉搜索树。
 *
 * 本题中，一个高度平衡二叉树是指一个二叉树每个节点 的左右两个子树的高度差的绝对值不超过 1。
 *
 * 示例:
 *
 * 给定的有序链表： [-10, -3, 0, 5, 9],
 *
 * 一个可能的答案是：[0, -3, 9, -10, null, 5], 它可以表示下面这个高度平衡二叉搜索树：
 *
 *       0
 *      / \
 *    -3   9
 *    /   /
 *  -10  5
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/convert-sorted-list-to-binary-search-tree
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/11/15
 */
public class ConvertSortedListToBinarySearchTree {

    @Test
    public void sortedListToBST() {
        ListNode head0 = new ListNode(-10);
        ListNode head1 = new ListNode(-3);
        ListNode head2 = new ListNode(0);
        ListNode head3 = new ListNode(5);
        ListNode head4 = new ListNode(9);
        head0.next = head1; head1.next = head2; head2.next = head3; head3.next = head4;
        TreeNode treeNode = sortedListToBST(head0);
        System.out.println(treeNode);
    }

    public TreeNode sortedListToBST(ListNode head) {
        // 相当于获得了中序遍历的结果
        List<Integer> data = new ArrayList<>();
        data.add(head.val);
        while (head.next != null) {
            data.add(head.next.val);
            head = head.next;
        }
        return helper(data, 0, data.size() - 1);
    }

    private TreeNode helper(List<Integer> data, int begin, int end) {
        if (begin > end) {
            return null;
        }
        int middle = (begin + end) >> 1;
        TreeNode root = new TreeNode(data.get(middle));
        root.setLeft(helper(data, begin, middle - 1));
        root.setRight(helper(data, middle + 1, end));
        return root;
    }


    public class ListNode {
        int val;
        ListNode next;

        ListNode(int x) {
            val = x;
        }
    }

}
