package xl.test.algorithm;

import org.junit.Test;

/**
 * 不使用额外内存反转单向链表
 * created by XUAN on 2019/05/09
 */
public class RevertList {

    @Test
    public void revertListTest() {
        // 获得长度为10的单向链表的根节点
        SingleListNode root = initNode(1000000000);
        // 获得链表的尾节点
        SingleListNode endNode = getEndNode(root);
        // 打印链表
        printList(root);
        // 反转链表
        revertList(root, null);
        // 打印反转链表
        printList(endNode);
    }

    private SingleListNode getEndNode(SingleListNode root) {
        while (root != null && root.getNext() != null) {
            root = root.getNext();
        }
        return root;
    }

    public void revertList(SingleListNode node, SingleListNode preNode) {
        if (node.getNext() != null) {
            // 遍历至最后的节点, 并传入上一个节点
            revertList(node.getNext(), node);
        }
        // 将节点指向上一个节点
        node.setNext(preNode);
        // 如果上一个节点不为空, 则将上一个节点的指向变为空
        if (preNode != null) {
            preNode.setNext(null);
        }
    }

    public SingleListNode initNode (int nums) {
        SingleListNode root = null;
        SingleListNode pre = null;
        for (int i = 0; i < nums; i++) {
            if (pre == null) {
                pre = new SingleListNode();
                root = pre;
                pre.setData(String.valueOf(i));
            } else {
                SingleListNode node = new SingleListNode();
                node.setData(String.valueOf(i));
                pre.setNext(node);
                pre = node;
            }
        }
        return root;
    }

    public void printList(SingleListNode node) {
        StringBuilder sb = new StringBuilder(node.getData() + " --> ");
        while (node.getNext() != null) {
            sb.append(node.getNext().getData() + " --> ");
            node = node.getNext();
        }
        sb.append("null");
        System.out.println(sb.toString());
    }
}
