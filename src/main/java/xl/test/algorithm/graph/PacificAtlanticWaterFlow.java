package xl.test.algorithm.graph;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 给定一个 m x n 的非负整数矩阵来表示一片大陆上各个单元格的高度。“太平洋”处于大陆的左边界和上边界，而“大西洋”处于大陆的右边界和下边界。
 *
 * 规定水流只能按照上、下、左、右四个方向流动，且只能从高到低或者在同等高度上流动。
 *
 * 请找出那些水流既可以流动到“太平洋”，又能流动到“大西洋”的陆地单元的坐标。
 *
 *  
 *
 * 提示：
 *
 * 输出坐标的顺序不重要
 * m 和 n 都小于150
 *  
 *
 * 示例：
 *
 *  
 *
 * 给定下面的 5x5 矩阵:
 *
 *   太平洋 ~   ~   ~   ~   ~
 *        ~  1   2   2   3  (5) *
 *        ~  3   2   3  (4) (4) *
 *        ~  2   4  (5)  3   1  *
 *        ~ (6) (7)  1   4   5  *
 *        ~ (5)  1   1   2   4  *
 *           *   *   *   *   * 大西洋
 *
 * 返回:
 *
 * [[0, 4], [1, 3], [1, 4], [2, 2], [3, 0], [3, 1], [4, 0]] (上图中带括号的单元).
 *
 *
 * 1 2 3
 * 8 9 4
 * 7 6 5
 *
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/pacific-atlantic-water-flow
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 * created by XUAN on 2019/11/20
 */
public class PacificAtlanticWaterFlow {

    @Test
    public void pacificAtlantic() {
        //int[] a = new int[]{1,2,3};
        //int[] b = new int[]{8,9,4};
        //int[] c = new int[]{7,6,5};
        //int[][] matrix = new int[3][3];
        //matrix[0] = a;
        //matrix[1] = b;
        //matrix[2] = c;
        //System.out.println(JSONObject.toJSONString(pacificAtlantic(matrix)));
        int[] a = new int[]{1,2,2,3,5};
        int[] b = new int[]{3,2,3,4,4};
        int[] c = new int[]{2,4,5,3,1};
        int[] d = new int[]{6,7,1,4,5};
        int[] e = new int[]{5,1,1,2,4};
        int[][] matrix = new int[5][5];
        matrix[0] = a;
        matrix[1] = b;
        matrix[2] = c;
        matrix[3] = d;
        matrix[4] = e;
        System.out.println(JSONObject.toJSONString(pacificAtlantic(matrix)));
    }

    public List<List<Integer>> pacificAtlantic(int[][] matrix) {
        List<List<Integer>> result = new ArrayList<>();
        if(matrix.length == 0 || matrix[0].length == 0){
            return result;
        }
        int row = matrix.length;
        int length = matrix[0].length;
        boolean[][] pacific = new boolean[row][length];
        boolean[][] atlantic = new boolean[row][length];
        // 1. 从每一个为true的节点开始, 从上下左右四个方向查看可以流向此节点的其他节点.
        // 2. 如果可以流且其他节点为false, 则标其他节点为true, 并遍历这个节点的上下左右四个方向的节点
        for(int i = 0; i < row; ++i){
            pacific[i][0] = true;
            traverse(matrix, pacific, i, 0);
        }
        for(int j = 0; j < length; ++j){
            pacific[0][j] = true;
            traverse(matrix, pacific, 0, j);
        }
        for(int i = 0; i < row; ++i){
            atlantic[i][length-1] = true;
            traverse(matrix, atlantic, i, length-1);
        }
        for(int j = 0; j < length; ++j){
            atlantic[row-1][j] = true;
            traverse(matrix, atlantic, row-1, j);
        }
        for(int i = 0; i < row; ++i){
            for(int j = 0; j < length; ++j){
                if(pacific[i][j] && atlantic[i][j])
                    result.add(Arrays.asList(i, j));
            }
        }
        return result;
    }
    private void traverse(int[][] matrix, boolean[][] buffer, int i, int j){
        // 上面有节点, 且上面节点为false, 且上面节点大, 则设上面节点为true, 并遍历上面节点
        if(i > 0 && !buffer[i-1][j] && matrix[i-1][j] >= matrix[i][j]){
            buffer[i-1][j] = true;
            traverse(matrix, buffer, i-1, j);
        }
        // 左面有节点, 且左面节点为false, 且左面节点大, 则设左面节点为true, 并遍历左面节点
        if(j > 0 && !buffer[i][j-1] && matrix[i][j-1] >= matrix[i][j]){
            buffer[i][j-1] = true;
            traverse(matrix, buffer, i, j-1);
        }
        // 下面有节点, 且下面节点为false, 且下面节点大, 则设下面节点为true, 并遍历下面节点
        if(i < matrix.length - 1 && !buffer[i+1][j] && matrix[i+1][j] >= matrix[i][j]){
            buffer[i+1][j] = true;
            traverse(matrix, buffer, i+1, j);
        }
        // 右面有节点, 且右面节点为false, 且右面节点大, 则设右面节点为true, 并遍历右面节点
        if(j < matrix[0].length - 1 && !buffer[i][j+1] && matrix[i][j+1] >= matrix[i][j]){
            buffer[i][j+1] = true;
            traverse(matrix, buffer, i, j+1);
        }
    }

}
