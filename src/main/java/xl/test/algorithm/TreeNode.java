package xl.test.algorithm;

import com.alibaba.fastjson.JSON;
import xl.test.algorithm.utils.TreeNodeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * created by XUAN on 2019/05/06
 */
public class TreeNode {

    // 和leetcode统一, 可以直接拿到值
    public TreeNode left;

    public TreeNode right;

    public String data;

    public int val;

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

    @Override
    public String toString() {
        if (data == null) {
            return JSON.toJSONString(TreeNodeUtil.getIntArray(this));
        } else {
            return JSON.toJSONString(TreeNodeUtil.getStringArray(this));
        }
    }
}
