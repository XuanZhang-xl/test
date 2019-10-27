package xl.test.algorithm;

/**
 * created by XUAN on 2019/05/06
 */
public class TreeNode {

    private TreeNode left;

    private TreeNode right;

    private String data;

    private int val;

    public TreeNode getLeft() {
        return left;
    }

    public void setLeft(TreeNode left) {
        this.left = left;
    }

    public TreeNode getRight() {
        return right;
    }

    public void setRight(TreeNode right) {
        this.right = right;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getVal() {
        return val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    public TreeNode(String data) {
        this.data = data;
    }

    public TreeNode(int val) {
        this.val = val;
    }

    public TreeNode() {
    }
}