import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * 排列组合
 * 极少有机会用排列组合, 因为时间复杂度太高
 * created by XUAN on 2019/7/29
 */
public class PermutationAndCombination {
    public static Stack<Integer> stack = new Stack<Integer>();
    public static void main(String[] args) {
        int shu[] = {-1, 0, 1, 2, -1, -4};
        int targ = 3;
        //System.out.println("全排列:");
        //System.out.println(allPermutation(shu,targ));
        //System.out.println("排列:");
        //permutation(shu,targ,0);
        System.out.println("组合:");
        System.out.println(combination(shu,targ));
    }


    public static List<List<Integer>> allPermutation(int[] shu, int targ) {
        List<List<Integer>> result = new ArrayList<>();
        allPermutation(shu, targ, 0, result);
        return result;
    }

    /**
     * 全排列实现：随机选择数组长度的次数的所有可能
     * @param shu   待选择的数组
     * @param targ  要选择多少个次
     * @param cur   当前选择的是第几次
     */
    private static void allPermutation(int[] shu, int targ, int cur, List<List<Integer>> result) {
        if(cur == targ) {
            result.add((Stack)stack.clone());
            return;
        }
        for(int i=0;i<shu.length;i++) {
            stack.add(shu[i]);
            allPermutation(shu, targ, cur+1, result);
            stack.pop();
        }
    }

    public static List<List<Integer>> permutation(int[] shu, int targ){
        List<List<Integer>> result = new ArrayList<>();
        permutation(shu, targ, 0, result);
        return result;
    }
    /**
     * 排列实现：全排列的基础上， 不重复
     * @param shu   待选择的数组
     * @param targ  要选多少个元素
     * @param cur   当前选择的是第几次
     */
    private static void permutation(int[] shu, int targ, int cur, List<List<Integer>> result) {
        if(cur == targ) {
            result.add((Stack)stack.clone());
            return;
        }
        for(int i=0;i<shu.length;i++) {
            if(!stack.contains(shu[i])) {
                stack.add(shu[i]);
                permutation(shu, targ, cur + 1, result);
                stack.pop();
            }
        }
    }


    public static List<List<Integer>> combination(int[] shu, int targ) {
        List<List<Integer>> result = new ArrayList<>();
        combination(shu, targ, 0, 0, result);
        return result;
    }
    /**
     * 组合实现：排列基础上， 随机选择次数（最大为数组长度）
     * @param shu   待选择的数组
     * @param targ  要选多少个元素
     * @param has   当前有多少个元素
     * @param cur   当前选到的下标
     *
     * 1    2   3     //开始下标到2
     * 1    2   4     //然后从3开始
     */
    private static void combination(int[] shu, int targ, int has, int cur, List<List<Integer>> result) {
        if(has == targ) {
            result.add((Stack)stack.clone());
            return;
        }
        for(int i=cur;i<shu.length;i++) {
            stack.add(shu[i]);
            combination(shu, targ, has+1, i+1, result);
            stack.pop();
        }
    }
}
