package xl.test.algorithm.graph;

import org.junit.Test;
import xl.test.algorithm.utils.GetData;

import java.util.LinkedList;
import java.util.Queue;

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
 * 使用深度优先搜索与广度优先搜索
 *
 * created by XUAN on 2019/11/20
 */
public class NumberOfIslands {

    @Test
    public void numIslands() {
        System.out.println(numIslandsDFS(GetData.getCharArraySample1()));
        System.out.println(numIslandsDFS(GetData.getCharArraySample2()));
        System.out.println(numIslandsDFS(GetData.getCharArraySample3()));
        System.out.println(numIslandsBFS(GetData.getCharArraySample1()));
        System.out.println(numIslandsBFS(GetData.getCharArraySample2()));
        System.out.println(numIslandsBFS(GetData.getCharArraySample3()));

        //System.out.println(numIslands(GetData.getRandomZeroOneCharArray(10, 10)));
        //System.out.println(numIslands(GetData.getRandomZeroOneCharArray(100, 100)));
        //System.out.println(numIslands(GetData.getRandomZeroOneCharArray(1000, 1000)));

    }


    /**
     * 深度优先搜索
     * @param grid
     * @return
     */
    public int numIslandsDFS(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;
        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    num_islands++;
                    dfs(grid, r, c);
                }
            }
        }

        return num_islands;
    }

    void dfs(char[][] grid, int r, int c) {
        int nr = grid.length;
        int nc = grid[0].length;

        if (r < 0 || c < 0 || r >= nr || c >= nc || grid[r][c] == '0') {
            return;
        }

        // 将当前的 '1'变为0
        grid[r][c] = '0';
        // 搜索左边的值, 如果是1则将变为0, 且根节点数量不变, 可以不管
        //dfs(grid, r - 1, c);
        // 搜索右边的值, 如果是1则将变为0, 且根节点数量不变
        dfs(grid, r + 1, c);
        // 搜索上面的值, 如果是1则将变为0, 且根节点数量不变, 可以不管
        //dfs(grid, r, c - 1);
        // 搜索下面的值, 如果是1则将变为0, 且根节点数量不变
        dfs(grid, r, c + 1);
    }


    /**
     * 广度优先搜索
     * @param grid
     * @return
     */
    public int numIslandsBFS(char[][] grid) {
        if (grid == null || grid.length == 0) {
            return 0;
        }

        int nr = grid.length;
        int nc = grid[0].length;
        int num_islands = 0;

        for (int r = 0; r < nr; ++r) {
            for (int c = 0; c < nc; ++c) {
                if (grid[r][c] == '1') {
                    num_islands++;
                    // 一上来直接标记为已经遍历
                    grid[r][c] = '0';
                    // 先进先出队列, 临时存储遍历到的数据
                    Queue<Integer> neighbors = new LinkedList<>();
                    // 二维变一维, 将当前节点加入队列
                    neighbors.add(r * nc + c);
                    while (!neighbors.isEmpty()) {
                        // 移除,拿到头节点
                        int id = neighbors.remove();
                        // 获得节点位置
                        int row = id / nc;
                        int col = id % nc;
                        // 检查头节点的上面的节点是否被遍历过, 如果没有, 加入队列, 并标记为已遍历
                        if (row - 1 >= 0 && grid[row-1][col] == '1') {
                            neighbors.add((row-1) * nc + col);
                            grid[row-1][col] = '0';
                        }
                        // 下面节点, 操作同上
                        if (row + 1 < nr && grid[row+1][col] == '1') {
                            neighbors.add((row+1) * nc + col);
                            grid[row+1][col] = '0';
                        }
                        // 前面节点, 操作同上
                        if (col - 1 >= 0 && grid[row][col-1] == '1') {
                            neighbors.add(row * nc + col-1);
                            grid[row][col-1] = '0';
                        }
                        // 后面节点, 操作同上
                        if (col + 1 < nc && grid[row][col+1] == '1') {
                            neighbors.add(row * nc + col+1);
                            grid[row][col+1] = '0';
                        }
                    }
                }
            }
        }

        return num_islands;
    }
}
