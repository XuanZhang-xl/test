package xl.test.algorithm.unionfind;

import org.junit.Test;
import xl.test.algorithm.utils.GetData;

/**
 * 给定一个由 '1'（陆地）和 '0'（水）组成的的二维网格，计算岛屿的数量。一个岛被水包围，并且它是通过水平方向或垂直方向上相邻的陆地连接而成的。你可以假设网格的四个边均被水包围。
 *
 * 示例 1:
 *
 * 输入:
 * 11110
 * 11010
 * 11000
 * 00000
 *
 * 输出: 1
 * 示例 2:
 *
 * 输入:
 * 11000
 * 11000
 * 00100
 * 00011
 *
 * 输出: 3
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * 
 * 
 * 111111
 * 100001
 * 101101
 * 100001
 * 111111
 * 
 * 
 *
 * created by XUAN on 2019/11/19
 */
public class NumberOfIslands {

    @Test
    public void numIslands() {
        System.out.println(numIslands(GetData.getCharArraySample1()));
        System.out.println(numIslands(GetData.getCharArraySample2()));
        System.out.println(numIslands(GetData.getCharArraySample3()));

        System.out.println(numIslands(GetData.getRandomZeroOneCharArray(10, 10)));
        System.out.println(numIslands(GetData.getRandomZeroOneCharArray(100, 100)));
        System.out.println(numIslands(GetData.getRandomZeroOneCharArray(1000, 1000)));
        System.out.println(numIslands(GetData.getRandomZeroOneCharArray(10000, 10000)));
    }

    /**
     * 时间复杂度: O(NM)
     * 空间复杂度: O(NM)
     * @param grid
     * @return
     */
    public int numIslands(char[][] grid) {
        if (grid == null || grid.length == 0 || grid[0] == null || grid[0].length == 0) {
            return 0;
        }
        int row = grid.length;
        int length = grid[0].length;
        UnionFindSet unionFindSet = new UnionFindSet(row * length);

        int rootsSize = 0;

        for (int i = 0; i < grid.length; i++) {
            char[] chars = grid[i];
            for (int j = 0; j < chars.length; j++) {
                if (chars[j] == '1') {
                    boolean checked = false;
                    // 检查右边是否为1, 如果为1, 建立关系
                    // 从左往右走的, 所以只要看右边的是否有关系即可, 不用回头看左边的了
                    if (j + 1 < chars.length && chars[j + 1] == '1') {
                        unionFindSet.initRelation(i * length + j, i * length + j + 1);
                        checked = true;
                    }
                    // 检查下边是否为1, 如果为1, 建立关系
                    // 从上往下走的, 所以只要看下面的是否有关系即可, 不用回头看上边的了
                    if (i + 1 < grid.length && grid[i + 1][j] == '1') {
                        unionFindSet.initRelation(i * length + j, (i + 1) * length + j);
                        checked = true;
                    }
                    if (checked) {
                        continue;
                    }

                    // 当前值是1, 但是上面两步都没进去, 可能是孤立的1, 检查
                    if (j - 1 >= 0 && chars[j - 1] == '1') {
                        // 左边为1, 不符合孤立条件
                        continue;
                    }
                    if (i - 1 >= 0 && grid[i - 1][j] == '1') {
                        // 上面边为1, 不符合孤立条件
                        continue;
                    }
                    rootsSize++;
                }
            }
        }
        return rootsSize + unionFindSet.listRoots().size();
    }

}
