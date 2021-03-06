package xl.test.algorithm.unionfind;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * 给定一个由表示变量之间关系的字符串方程组成的数组，每个字符串方程 equations[i] 的长度为 4，并采用两种不同的形式之一："a==b" 或 "a!=b"。
 * 在这里，a 和 b 是小写字母（不一定不同），表示单字母变量名。
 *
 * 只有当可以将整数分配给变量名，以便满足所有给定的方程时才返回 true，否则返回 false。
 *
 * 示例 1：
 *
 * 输入：["a==b","b!=a"]
 * 输出：false
 * 解释：如果我们指定，a = 1 且 b = 1，那么可以满足第一个方程，但无法满足第二个方程。没有办法分配变量同时满足这两个方程。
 *
 * 示例 2：
 *
 * 输出：["b==a","a==b"]
 * 输入：true
 * 解释：我们可以指定 a = 1 且 b = 1 以满足满足这两个方程。
 *
 * 示例 3：
 *
 * 输入：["a==b","b==c","a==c"]
 * 输出：true
 *
 * 示例 4：
 *
 * 输入：["a==b","b!=c","c==a"]
 * 输出：false
 *
 * 示例 5：
 *
 * 输入：["c==c","b==d","x!=z"]
 * 输出：true
 *
 *
 *
 * 提示：
 *
 *     1 <= equations.length <= 500
 *     equations[i].length == 4
 *     equations[i][0] 和 equations[i][3] 是小写字母
 *     equations[i][1] 要么是 '='，要么是 '!'
 *     equations[i][2] 是 '='
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/satisfiability-of-equality-equations
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 *
 *
 * 并查集求解
 * created by XUAN on 2019/9/6
 */
public class EquationsPossible {

    UnionFindSet unionFindSet = new UnionFindSet();

    @Test
    public void equationsPossible() {
        unionFindSet = new UnionFindSet(26);
        String[] equations = new String[]{"c==c","b==d","x!=z"};
        System.out.println(equationsPossible(equations));
    }

    public boolean equationsPossible(String[] equations) {
        if (equations == null || equations.length == 0) {
            return false;
        }
        List<String> unequals = new ArrayList<>();
        for (String equation : equations) {
            int begin = fromCharToInt(equation.charAt(0));
            int end = fromCharToInt(equation.charAt(equation.length() - 1));
            if (equation.substring(1, equation.length() - 1).equals("==")) {
                unionFindSet.initRelation(begin, end);
            } else {
                unequals.add(equation);
            }
        }

        if (unequals.size() > 0) {
            for (String unequal : unequals) {
                int begin = fromCharToInt(unequal.charAt(0));
                int end = fromCharToInt(unequal.charAt(unequal.length() - 1));
                int rootI = unionFindSet.findRoot(begin);
                int rootJ = unionFindSet.findRoot(end);
                if (rootI == rootJ && rootI != -1) {
                    return false;
                }
            }
        }
        return true;
    }

    private int fromCharToInt(char ch) {
        return ch - 97;
    }

    public static void main(String[] args){
        System.out.println((int)'a' - 97);
    }

}
