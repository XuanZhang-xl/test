package algorithm;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 数字围城一个圈, 数到3的倍数时将其移出, 直到最后一个
 * created by XUAN on 2019/05/06
 */
public class CircleRemove {

    @Test
    public void circleRemove () {
        List<Integer> nums = new ArrayList<>();
        for (int i = 0; i <= 6; i++) {
            nums.add(i);
        }

        // 报数
        int num = 0;
        // list当前位置
        int index = 0;
        while (true) {
            if (num % 3 == 0) {
                nums.remove(index);
            } else {
                index++;
            }
            num++;

            if (index >= nums.size()) {
                index = 0;
            }

            if (nums.size() == 1) {
                System.out.println(nums);
                break;
            }
        }


    }


}
