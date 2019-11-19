package xl.test.algorithm.unionfind;

import com.alibaba.fastjson.JSON;
import org.junit.Test;
import xl.test.algorithm.leetcode.utils.UnionFindSetUtil;

import java.util.Random;

/**
 * 并查集
 * 时间复杂度为O(N)。
 * created by XUAN on 2019/08/26
 */
public class UnionFindSet {


    /**
     * 使用数组实现查并集
     */
    @Test
    public void implementWithArray() {
        Random random =new Random();
        int size = 1000;
        UnionFindSetUtil.initArray(size);
        // 随机建立size次关系
        for (int i = 0; i < size; i++) {
            UnionFindSetUtil.initRelation(random.nextInt(size), random.nextInt(size));
        }

        UnionFindSetUtil.printElement();
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
        return UnionFindSetUtil.findRoot(i) == UnionFindSetUtil.findRoot(j);
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
