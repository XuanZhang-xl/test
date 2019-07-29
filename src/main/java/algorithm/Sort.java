package algorithm;

/**
 * 排序
 * created by XUAN on 2019/7/29
 */
public class Sort {


    /**
     * 快速排序
     * @param in
     * @param left
     * @param right
     * @return
     */
    public static int[] quickSort(int[] in, int left, int right){
        int key = in[left]; //选定数组第一个数字作为key
        int start = left;
        int end = right;
        while(start<end){
            //从右向左遍历,找到小于key的,放入下标strat中。
            while(start < end && key<=in[end]){
                end--;
            }
            in[start] = in[end];

            //从左向右遍历,找到大于key的,放入下标end中。
            while(start < end && key > in[start]){
                start++;
            }
            in[end] = in[start];
        }
        //此时start==end,这就是所谓的轴，把key放入轴中，轴左边的都<key,轴右边的都>key
        in[start] = key;
        //此时大家想象一下，轴在数组中间，说明把数组分成两部分，此时要对这两部分分别进行快排。
        if(start>left)quickSort(in,left,start-1);
        if(end<right)quickSort(in, end+1, right);
        return in;
    }


}
