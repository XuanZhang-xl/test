package algorithm.unionfind;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Random;

/**
 * 并查集
 * 时间复杂度为O(N)。
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
        int size = 1000;
        initArray(size);
        // 随机建立size次关系
        for (int i = 0; i < size; i++) {
            initRelation(random.nextInt(size), random.nextInt(size));
        }

        System.out.println(JSON.toJSONString(element));
        // 随机查看是否有关
        int yes = 0;
        int no = 0;
        for (int i = 0; i < size; i++) {
            int index1 = random.nextInt(size);
            int index2 = random.nextInt(size);
            boolean relation = findRelation(index1, index2);
            if (relation) {
                yes++;
            } else {
                no++;
            }
            System.out.println(index1 + " 与 " + index2 + (relation ? " 有" : " 无") + "关");
        }
        System.out.println("有关个数: "+ yes + ", 无关个数" + no);
    }

    private boolean findRelation(int i, int j) {
        int rootI = findRoot(i);
        return rootI != -1 && rootI == findRoot(j);
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
            for (int i = 0; i < size; i++) {
                element[i] = -1;
            }
        }
        if (heights == null) {
            heights = new int[size];
            for (int i = 0; i < size; i++) {
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
