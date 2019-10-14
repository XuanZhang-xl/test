package xl.test.algorithm.leetcode;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 是否回文, 只看英文字符忽略其他字符
 * created by XUAN on 2019/7/23
 */
public class IsPalindrome {

    @Test
    public void isPalindrome () {
        List<String> datas = new ArrayList<>();
        datas.add("A man, a plan, a canal: Panama");
        datas.add("a");
        datas.add("aw");
        datas.add("awa");
        datas.add("awwa");
        datas.add("  a w w a      ");
        datas.add("race a car");
        datas.add("0P");
        for (String data : datas) {
            isPalindrome0(data);
        }

    }
    public void isPalindrome0(String data) {
        data = data.toLowerCase();
        int start = 0;
        int end = data.length() - 1;
        byte[] bytes = data.getBytes();
        while(true) {
            if (start == end || end < start) {
                System.out.println(data + " 是回文");
                break;
            }
            int startByte = bytes[start];
            int endByte = bytes[end];
            if (startByte < 48 || (startByte > 57 && startByte < 65) || (startByte > 90 && startByte < 97) || startByte > 122) {
                start++;
                continue;
            }
            if (endByte < 48 || (endByte > 57 && endByte < 65) || (endByte > 90 && endByte < 97) || endByte > 122) {
                end--;
                continue;
            }
            if (startByte == endByte || (startByte >= 97 && startByte - 32 == endByte) || (endByte >= 97 && endByte - 32 == startByte)) {
                start++;
                end--;
                continue;
            } else {
                System.out.println(data + " 不是回文");
                break;
            }
        }
    }

    @Test
    public void testByte () {
        System.out.println((byte)'a');
        System.out.println((byte)'z');
        System.out.println((byte)'A');
        System.out.println((byte)'Z');
        System.out.println((byte)'0');
        System.out.println((byte)'9');
        System.out.println((byte)'a' - (byte)'A');
        System.out.println((byte)'z' - (byte)'Z');
    }


    @Test
    public void partition() {
        String data = "wefbawavawewabdnjxn";

        // 分治算法:


    }

    public void divideAndConquer (String data) {
        if (data.length() > 2) {

        }
    }



    /*****************************排列组合方法1开始************************************/
    @Test
    public void  getData () {
        List<Integer> arrayList = new ArrayList<>();
        List<String> arrayListData = new ArrayList<>();
        Stack<Integer> data = new Stack<>();
        int length = 8;
        for (int i = 0; i < 10; i++) {
            arrayList.add(i);
        }
        getData0(arrayList, data, arrayListData, length, false);
        System.out.println(arrayListData);
        System.out.println(arrayListData.size());
    }

    /**
     * 排列算法
     * 思路：
     * 递归，第一层把循环把n个数中的第i个装入结果的第一个位置
     *       把剩下的n-2个数循环装入第二个位置
     *       把剩下的n-3个数循环装入第三个位置
     *       .....
     *       直到最后一个数，返回
     * 注意：下到下一层的时候注意new新的list不然，原来的list也会被改变
     *       其次：
     *       在递归返回的时候应该把记录的data最后一个去掉，否则前面的结果会
     *       影响循环过程。
     * @param arrayList
     * @param data
     * @param arrayListData
     * @param length
     */
    private void getData0(List<Integer> arrayList ,Stack<Integer> data, List<String> arrayListData, int length, boolean isRepeat){
        getData00(arrayList, data, arrayListData, arrayList.size(), length, isRepeat);
    }
    private void getData00(List<Integer> arrayList ,Stack<Integer> data, List<String> arrayListData, int totalLength, int length, boolean isRepeat){
        if (arrayList.size()== totalLength - length) {
            arrayListData.add(data.toString());
            return;
        }
        for (int i = 0; i < arrayList.size(); i++) {
            if (isRepeat || !data.contains(arrayList.get(i))) {
                data.push(arrayList.get(i));
                List<Integer> newArrayList = new ArrayList<>(arrayList);
                newArrayList.remove(i);
                getData00(newArrayList,data,arrayListData, totalLength, length, isRepeat);
                if (!data.empty()){
                    data.pop();
                }
                if (data.size() == length) {
                    break;
                }
            }
        }
    }

    /*****************************排列组合方法1结束************************************/




}
