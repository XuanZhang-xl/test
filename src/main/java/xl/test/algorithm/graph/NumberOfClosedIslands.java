package xl.test.algorithm.graph;

import org.junit.Test;

/**
 * 有一个二维矩阵 grid ，每个位置要么是陆地（记号为 0 ）要么是水域（记号为 1 ）。
 *
 * 我们从一块陆地出发，每次可以往上下左右 4 个方向相邻区域走，能走到的所有陆地区域，我们将其称为一座「岛屿」。
 *
 * 如果一座岛屿 完全 由水域包围，即陆地边缘上下左右所有相邻区域都是水域，那么我们将其称为 「封闭岛屿」。
 *
 * 请返回封闭岛屿的数目。
 *
 *  
 *
 * 示例 1：
 *
 *
 *
 * 输入：grid = [[1,1,1,1,1,1,1,0],[1,0,0,0,0,1,1,0],[1,0,1,0,1,1,1,0],[1,0,0,0,0,1,0,1],[1,1,1,1,1,1,1,0]]
 * 输出：2
 * 解释：
 * 灰色区域的岛屿是封闭岛屿，因为这座岛屿完全被水域包围（即被 1 区域包围）。
 * 示例 2：
 *
 *
 *
 * 输入：grid = [[0,0,1,0,0],[0,1,0,1,0],[0,1,1,1,0]]
 * 输出：1
 * 示例 3：
 *
 * 输入：grid = [[1,1,1,1,1,1,1],
 *              [1,0,0,0,0,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,1,0,1,0,1],
 *              [1,0,1,1,1,0,1],
 *              [1,0,0,0,0,0,1],
 *              [1,1,1,1,1,1,1]]
 * 输出：2
 *  
 *
 * 提示：
 *
 * 1 <= grid.length, grid[0].length <= 100
 * 0 <= grid[i][j] <=1
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/number-of-closed-islands
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * [
 * [0,0,1,1,0,1,0,0,1,0],
 * [1,1,0,1,1,0,1,1,1,0],
 * [1,0,1,1,1,0,0,1,1,0],
 * [0,1,1,0,0,0,0,1,0,1],
 * [0,0,0,0,0,0,1,1,1,0],
 * [0,1,0,1,0,1,0,1,1,1],
 * [1,0,1,0,1,1,0,0,0,1],
 * [1,1,1,1,1,1,0,0,0,0],
 * [1,1,1,0,0,1,0,1,0,1],
 * [1,1,1,0,1,1,0,1,1,0]]
 *
 * [[1,1,0,1,1,1,1,1,1,1],
 * [0,0,1,0,0,1,0,1,1,1],
 * [1,0,1,0,0,0,1,0,1,0],
 * [1,1,1,1,1,0,0,1,0,0],
 * [1,0,1,0,1,1,1,1,1,0],
 * [0,0,0,0,1,1,0,0,0,0],
 * [1,0,1,0,0,0,0,1,1,0],
 * [1,1,0,0,1,1,0,0,0,0],
 * [0,0,0,1,1,0,1,1,1,0],
 * [1,1,0,1,0,1,0,0,1,0]]
 *
 *
 * created by XUAN on 2019/11/22
 */
public class NumberOfClosedIslands {

    @Test
    public void closedIsland() {
        //int[] a = new int[]{1,1,1,1,1,1,1};
        //int[] b = new int[]{1,0,0,0,0,0,1};
        //int[] c = new int[]{1,0,1,1,1,0,1};
        //int[] d = new int[]{1,0,1,0,1,0,1};
        //int[][] grid = new int[][]{a.clone(),b.clone(),c.clone(),d.clone(),c.clone(),b.clone(),a.clone()};
        //System.out.println(closedIsland(grid));
        //int[] a = new int[]{0,0,1,1,0,1,0,0,1,0};
        //int[] b = new int[]{1,1,0,1,1,0,1,1,1,0};
        //int[] c = new int[]{1,0,1,1,1,0,0,1,1,0};
        //int[] d = new int[]{0,1,1,0,0,0,0,1,0,1};
        //int[] e = new int[]{0,0,0,0,0,0,1,1,1,0};
        //int[] f = new int[]{0,1,0,1,0,1,0,1,1,1};
        //int[] g = new int[]{1,0,1,0,1,1,0,0,0,1};
        //int[] k = new int[]{1,1,1,1,1,1,0,0,0,0};
        //int[] l = new int[]{1,1,1,0,0,1,0,1,0,1};
        //int[] m = new int[]{1,1,1,0,1,1,0,1,1,0};
        //int[][] grid = new int[][]{a.clone(),b.clone(),c.clone(),d.clone(),e.clone(),f.clone(),g.clone(),k.clone(),l.clone(),m.clone()};
        //System.out.println(closedIsland(grid));
        int[] a = new int[]{1,1,0,1,1,1,1,1,1,1};
        int[] b = new int[]{0,0,1,0,0,1,0,1,1,1};
        int[] c = new int[]{1,0,1,0,0,0,1,0,1,0};
        int[] d = new int[]{1,1,1,1,1,0,0,1,0,0};
        int[] e = new int[]{1,0,1,0,1,1,1,1,1,0};
        int[] f = new int[]{0,0,0,0,1,1,0,0,0,0};
        int[] g = new int[]{1,0,1,0,0,0,0,1,1,0};
        int[] k = new int[]{1,1,0,0,1,1,0,0,0,0};
        int[] l = new int[]{0,0,0,1,1,0,1,1,1,0};
        int[] m = new int[]{1,1,0,1,0,1,0,0,1,0};
        int[][] grid = new int[][]{a.clone(),b.clone(),c.clone(),d.clone(),e.clone(),f.clone(),g.clone(),k.clone(),l.clone(),m.clone()};
        System.out.println(closedIsland(grid));
    }

    public int closedIsland(int[][] grid) {
        int count = 0;
        for (int i = 0; i < grid.length; i++) {
            int[] row = grid[i];
            for (int j = 0; j < row.length; j++) {
                if (row[j] == 0) {
                    if (closedIsland(grid, i, j)) {
                        count++;
                        System.out.println(i + "   " + j);
                    }
                }
            }
        }
        return count;
    }

    private boolean closedIsland(int[][] grid, int i, int j) {
        if (grid[i][j] == 1) {
            return true;
        }
        //print(grid);
        int row = grid.length;
        int length = grid[0].length;
        if (i == 0 || j == 0 || i == row - 1 || j == length - 1) {
            return false;
        }
        grid[i][j] = 1;
        return closedIsland(grid, i - 1, j) & closedIsland(grid, i, j - 1) & closedIsland(grid, i + 1, j) & closedIsland(grid, i, j + 1);
    }

    private void print(int[][] grid) {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                System.out.print(grid[i][j]);
            }
            System.out.println();
        }
        System.out.println();
    }


}
