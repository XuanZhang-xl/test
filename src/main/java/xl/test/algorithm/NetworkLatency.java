package xl.test.algorithm;

/**
 * 有 N 个网络节点，标记为 1 到 N。
 * <p>
 * 给定一个列表 times，表示信号经过有向边的传递时间。 times[i] = (u, v, w)，其中 u 是源节点，v 是目标节点， w 是一个信号从源节点传递到目标节点的时间。
 * <p>
 * 现在，我们向当前的节点 K 发送了一个信号。需要多久才能使所有节点都收到信号？如果不能使所有节点收到信号，返回 -1。
 * <p>
 * 注意:
 * <p>
 * N 的范围在 [1, 100] 之间。
 * K 的范围在 [1, N] 之间。
 * times 的长度在 [1, 6000] 之间。
 * 所有的边 times[i] = (u, v, w) 都有 1 <= u, v <= N 且 0 <= w <= 100。
 * <p>
 * 来源：力扣（LeetCode）
 * 链接：https://leetcode-cn.com/problems/network-delay-time
 * 著作权归领扣网络所有。商业转载请联系官方授权，非商业转载请注明出处。
 * <p>
 * 最短路径算法  dijkstra
 *
 * @author XUAN
 * @since 2019/12/01
 */
public class NetworkLatency {

    public int networkDelayTime(int[][] times, int N, int K) {
        int INF = Integer.MAX_VALUE;    //表示无穷远
        int max = -1;//最大距离
        int[][] graph = new int[N + 1][N + 1];//矩阵
        boolean[] flag = new boolean[N + 1];//标志
        int[] path = new int[N + 1];//点K到各点的最短距离
        for (int i = 1; i < graph.length; i++) {//初始化
            for (int j = 1; j < graph[i].length; j++) {
                graph[i][j] = INF;
            }
        }
        for (int[] time : times) {//读入边
            graph[time[0]][time[1]] = time[2];
        }
        for (int i = 1; i < path.length; i++) path[i] = INF;
        path[K] = 0;

        while (true) {
            int index = -1;
            int min = Integer.MAX_VALUE;

            for (int i = 1; i < graph.length; i++) {    //找出最小值
                if (path[i] < min && !flag[i]) {    //没有判定距离是否为无限距离，此处不影响
                    min = path[i];
                    index = i;
                }
            }

            if (index == -1) break;
            flag[index] = true;

            for (int i = 1; i < graph[index].length; i++) {    //优化
                if (graph[index][i] != INF && !flag[i] && path[index] + graph[index][i] < path[i]) {
                    path[i] = path[index] + graph[index][i];
                }
            }
        }

        for (int i = 1; i < path.length; i++) {//找出最大距离
            if (path[i] == INF) {
                return -1;
            }
            if (max < path[i]) {
                max = path[i];
            }
        }
        return max;
    }

}
