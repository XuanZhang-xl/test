package xl.test.algorithm.unionfind;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;

/**
 * 使用数组实现查并集
 *
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
     * 存储根节点
     */
    private Set<Integer> roots = new HashSet<>();

    /**
     * 查找两个节点之间是否相关
     * @param i
     * @param j
     * @return
     */
    public boolean findRelation(int i, int j) {
        return findRoot(i) == findRoot(j);
    }

    /**
     * 建立联系
     * @param i
     * @param j
     */
    public void initRelation(int i, int j) {
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

    /**
     * 找到根节点
     * @param x
     * @return
     */
    public int findRoot(int x) {
        while (element[x] != -1) {
            x = element[x];
        }
        return x;
    }

    /**
     * 并查集初始化
     * @param size
     */
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

    public UnionFindSet() {}
    public UnionFindSet(int size) {
        initArray(size);
    }

    /**
     * 获得所有根节点
     * @return
     */
    public Set<Integer> listRoots() {
        return roots;
    }

    /**
     * 打印当前关系数组
     */
    public void printElement() {
        if (element != null) {
            System.out.println(JSON.toJSONString(element));
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

    @Test
    public void implementWithArray() {
        Random random =new Random();
        int size = 1000;
        initArray(size);
        // 随机建立size次关系
        for (int i = 0; i < size; i++) {
            initRelation(random.nextInt(size), random.nextInt(size));
        }

        printElement();
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

}
