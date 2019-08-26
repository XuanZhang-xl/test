package algorithm.unionfind;

import org.junit.Test;

import java.util.Random;

/**
 * 查并集
 * created by XUAN on 2019/08/26
 */
public class UnionFindSet {

    /**
     * 存储树形结构
     */
    private int[] element = null;

    /**
     * 记录每课树的高度, 矮树向高树合并, 防止退化成链表
     */
    private int[] heights = null;

    /**
     * 使用数组实现查并集
     */
    @Test
    public void implementWithArray() {
        Random random =new Random();
        int size = 50;
        random.nextInt(size);
        initArray(50);
        initRelation(1, 3);
        initRelation(1, 2);
        initRelation(2, 3);
        initRelation(1, 49);
        initRelation(5, 3);
        initRelation(43, 20);
        initRelation(41, 20);
        initRelation(42, 20);
        initRelation(42, 2);

        System.out.println(findRelation(1, 30));
        System.out.println(findRelation(23, 24));
        System.out.println(findRelation(45,1));
        System.out.println(findRelation(1,1));
    }

    private boolean findRelation(int i, int j) {
        return findRoot(i) == findRoot(j);
    }

    private void initRelation(int i, int j) {
        int rootI = findRoot(i);
        int rootJ = findRoot(j);
        if (rootI != rootJ) {
            // 根不同, 则连起来
            if (heights[rootI] == heights[rootJ]) {
                // 高度相同, 随便合并, 但高度加一
                element[rootI] = rootJ;
                heights[rootJ]++;
            } else if (heights[rootI] > heights[rootJ]) {
                element[rootJ] = rootI;
            } else {
                element[rootI] = rootJ;
            }
        }
    }

    private int findRoot(int x) {
        while (element[x] != -1) {
            x = element[x];
        }
        return x;
    }

    public void initArray (int size) {
        if (element == null) {
            element = new int[size];
            for (int i : element) {
                element[i] = -1;
            }
        }
        if (heights == null) {
            heights = new int[size];
            for (int i : heights) {
                heights[i] = 1;
            }
        }
    }

    public static void main(String[] args) {
        Random random =new Random();
        int size = 50;
        for (int i = 0; i < size * 3000; i++) {
            int nextInt = random.nextInt(size);
            if (nextInt >= 50) {
                System.out.println(nextInt);
            }
        }
    }

}
