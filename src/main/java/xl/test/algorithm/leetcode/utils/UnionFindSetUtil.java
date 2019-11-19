package xl.test.algorithm.leetcode.utils;

import com.alibaba.fastjson.JSON;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * 并查集util
 *
 * 使用数组实现
 * created by XUAN on 2019/11/19
 */
public class UnionFindSetUtil {

    /**
     * 存储树形结构
     */
    private static int[] element = null;

    /**
     * 记录每课树的高度, 矮树向高树合并, 防止退化成链表
     */
    private static int[] heights = null;

    /**
     * 存储根节点
     */
    private static Set<Integer> roots = new HashSet<>();


    public static void initRelation(int i, int j) {
        int rootI = findRoot(i);
        int rootJ = findRoot(j);
        if (rootI != rootJ) {
            // 根不同, 则连起来
            if (heights[rootI] == heights[rootJ]) {
                // 高度相同, 随便合并, 但高度加一
                element[rootI] = rootJ;
                heights[rootJ]++;
                roots.add(rootJ);
                roots.remove(rootI);
            } else if (heights[rootI] > heights[rootJ]) {
                element[rootJ] = rootI;
                roots.add(rootI);
                roots.remove(rootJ);
            } else {
                element[rootI] = rootJ;
                roots.add(rootJ);
                roots.remove(rootI);
            }
        }
    }

    public static int findRoot(int x) {
        while (element[x] != -1) {
            x = element[x];
        }
        return x;
    }

    public static void initArray (int size) {
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

    public static Set<Integer> listRoots() {
        return roots;
    }

    public static void printElement() {
        if (element != null) {
            System.out.println(JSON.toJSONString(element));
        }
    }
}
