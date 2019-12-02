package xl.test.algorithm.graph;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;

/**
 * 让我们一起来玩扫雷游戏！
 *
 * 给定一个代表游戏板的二维字符矩阵。 'M' 代表一个未挖出的地雷，'E' 代表一个未挖出的空方块，'B' 代表没有相邻（上，下，左，右，和所有4个对角线）地雷的已挖出的空白方块，数字（'1' 到 '8'）表示有多少地雷与这块已挖出的方块相邻，'X' 则表示一个已挖出的地雷。
 *
 * 现在给出在所有未挖出的方块中（'M'或者'E'）的下一个点击位置（行和列索引），根据以下规则，返回相应位置被点击后对应的面板：
 *
 * 如果一个地雷（'M'）被挖出，游戏就结束了- 把它改为 'X'。
 * 如果一个没有相邻地雷的空方块（'E'）被挖出，修改它为（'B'），并且所有和其相邻的方块都应该被递归地揭露。
 * 如果一个至少与一个地雷相邻的空方块（'E'）被挖出，修改它为数字（'1'到'8'），表示相邻地雷的数量。
 * 如果在此次点击中，若无更多方块可被揭露，则返回面板。
 *  
 *
 * 示例 1：
 *
 * 输入:
 *
 * [['E', 'E', 'E', 'E', 'E'],
 *  ['E', 'E', 'M', 'E', 'E'],
 *  ['E', 'E', 'E', 'E', 'E'],
 *  ['E', 'E', 'E', 'E', 'E']]
 *
 * Click : [3,0]
 *
 * 输出:
 *
 * [['B', '1', 'E', '1', 'B'],
 *  ['B', '1', 'M', '1', 'B'],
 *  ['B', '1', '1', '1', 'B'],
 *  ['B', 'B', 'B', 'B', 'B']]
 *
 *
 * 示例 2：
 *
 * 输入:
 *
 * [['B', '1', 'E', '1', 'B'],
 *  ['B', '1', 'M', '1', 'B'],
 *  ['B', '1', '1', '1', 'B'],
 *  ['B', 'B', 'B', 'B', 'B']]
 *
 * Click : [1,2]
 *
 * 输出:
 *
 * [['B', '1', 'E', '1', 'B'],
 *  ['B', '1', 'X', '1', 'B'],
 *  ['B', '1', '1', '1', 'B'],
 *  ['B', 'B', 'B', 'B', 'B']]
 *
 *  
 *
 * 注意：
 *
 * 输入矩阵的宽和高的范围为 [1,50]。
 * 点击的位置只能是未被挖出的方块 ('M' 或者 'E')，这也意味着面板至少包含一个可点击的方块。
 * 输入面板不会是游戏结束的状态（即有地雷已被挖出）。
 * 简单起见，未提及的规则在这个问题中可被忽略。例如，当游戏结束时你不需要挖出所有地雷，考虑所有你可能赢得游戏或标记方块的情况。
 *
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/minesweeper
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 *
 *
 * 输入:
 * [
 * ["E","E","E","E","E","E","E","E"],
 * ["E","E","E","E","E","E","E","M"],
 * ["E","E","M","E","E","E","E","E"],
 * ["M","E","E","E","E","E","E","E"],
 * ["E","E","E","E","E","E","E","E"],
 * ["E","E","E","E","E","E","E","E"],
 * ["E","E","E","E","E","E","E","E"],
 * ["E","E","M","M","E","E","E","E"]
 * ]
 * [0,0]
 *
 * 答案:
 * [
 * ["B","B","B","B","B","B","1","E"],
 * ["B","1","1","1","B","B","1","M"],
 * ["1","2","M","1","B","B","1","1"],
 * ["M","2","1","1","B","B","B","B"],
 * ["1","1","B","B","B","B","B","B"],
 * ["B","B","B","B","B","B","B","B"],
 * ["B","1","2","2","1","B","B","B"],
 * ["B","1","M","M","1","B","B","B"]
 * ]
 * 可知递归结束条件: 遇到周围有雷的方格或是到达边界。
 *
 * created by XUAN on 2019/11/21
 */
public class MineSweeper {

    @Test
    public void updateBoard () {
        char[] a = new char[]{'E', 'E', 'E', 'E', 'E'};
        char[] b = new char[]{'E', 'E', 'M', 'E', 'E'};
        char[][] board = new char[4][5];
        board[0] = a.clone();board[1] = b.clone();board[2] = a.clone();board[3] = a.clone();
        int[] click = new int[]{3,0};
        System.out.println(JSONObject.toJSONString(updateBoard(board, click)));
    }


    public char[][] updateBoard(char[][] board, int[] click) {
        if (board == null || board.length == 0) {
            return null;
        }
        // 按到地雷, 游戏结束
        if (board[click[0]][click[1]] == 'M'){
            board[click[0]][click[1]] = 'X';
        } else {
            // 按到空白(E)则变为B或数字, 并将周围空白也变为B或数字
            clickBlank(board, click[0], click[1]);
        }
        return board;
    }

    private void clickBlank(char[][] board, int i, int j) {
        if (board[i][j] == 'E') {
            int mineCount = 0;
            int row = board.length;
            int length = board[0].length;
            if (i > 0 && board[i - 1][j] == 'M') {
                mineCount++;
            }
            if (i > 0 && j > 0 && board[i - 1][j - 1] == 'M') {
                mineCount++;
            }
            if (i > 0 && j < length - 1 && board[i - 1][j + 1] == 'M') {
                mineCount++;
            }
            if (j > 0 && board[i][j - 1] == 'M') {
                mineCount++;
            }
            if (j < length - 1 && board[i][j + 1] == 'M') {
                mineCount++;
            }
            if (i < row - 1 && board[i + 1][j] == 'M') {
                mineCount++;
            }
            if (i < row - 1 && j > 0 && board[i + 1][j - 1] == 'M') {
                mineCount++;
            }
            if (i < row - 1 && j < length - 1 && board[i + 1][j + 1] == 'M') {
                mineCount++;
            }
            if (mineCount == 0) {
                board[i][j] = 'B';
            } else {
                board[i][j] = (char) (mineCount + 48);
            }
            // 只有空白的节点且周围8个节点无雷才继续查找
            if (board[i][j] == 'B') {
                if (i > 0) {
                    clickBlank(board, i - 1, j);
                }
                if (i > 0 && j > 0) {
                    clickBlank(board, i - 1, j - 1);
                }
                if (i > 0 && j < length - 1) {
                    clickBlank(board, i - 1, j + 1);
                }
                if (j > 0) {
                    clickBlank(board, i, j - 1);
                }
                if (j < length - 1) {
                    clickBlank(board, i, j + 1);
                }
                if (i < row - 1) {
                    clickBlank(board, i + 1, j);
                }
                if (i < row - 1 && j > 0) {
                    clickBlank(board, i + 1, j - 1);
                }
                if (i < row - 1 && j < length - 1) {
                    clickBlank(board, i + 1, j + 1);
                }
            }
        }
    }
}