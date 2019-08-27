package algorithm;

import com.alibaba.fastjson.JSON;
import org.junit.Test;

import java.util.Random;

/**
 * 高纳德洗牌算法
 * 每一个元素都能等概率的出现在每一个位置
 * created by XUAN on 2019/8/27
 */
public class KnuthShuffle {

    private int[] elements = null;

    private Random random = new Random();

    @Test
    public void knuthShuffle () {
        int size = 54;
        if (elements == null) {
            elements = new int[size];
            for (int i = 0; i < size; i++) {
                elements[i] = i;
            }
        }
        knuthShuffle0(size);
        System.out.println(JSON.toJSONString(elements));
    }

    private void knuthShuffle0(int size) {
        // 从高位到低位遍历, 方便生成随机数
        for (int i = size - 1; i > 0 ; i--) {
            swap(i, random.nextInt(i));
        }
    }

    private void swap(int i, int j) {
        int temp = elements[i];
        elements[i] = elements[j];
        elements[j] = temp;
    }

}
