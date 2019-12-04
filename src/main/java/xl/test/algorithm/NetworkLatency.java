package xl.test.algorithm;

import org.junit.Test;
import xl.test.algorithm.heap.BinaryHeap;
import xl.test.algorithm.utils.GraphLine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

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

    //表示无穷远
    int INF = Integer.MAX_VALUE;

    @Test
    public void networkDelayTime() {
        int[] a = {1,2,1};
        int[] b = {1,3,12};
        int[] c = {2,3,9};
        int[] d = {2,4,3};
        int[] e = {3,5,5};
        int[] f = {4,3,4};
        int[] g = {4,5,13};
        int[] h = {4,6,15};
        int[] i = {5,6,4};
        int[][] times = {a,b,c,d,e,f,g,h,i};
        int N = 6;
        int K = 4;
        //System.out.println(networkDelayTime(times, N, K));
        //System.out.println(networkDelayTimeUsingHeap(times, N, K));

        int[][] times2 = {{2,4,10},{5,2,38},{3,4,33},{4,2,76},{3,2,64},{1,5,54},{1,4,98},{2,3,61},{2,1,0},{3,5,77},{5,1,34},{3,1,79},{5,3,2},{1,2,59},{4,3,46},{5,4,44},{2,5,89},{4,5,21},{1,3,86},{4,1,95}};
        N = 5;
        K = 1;
        System.out.println(networkDelayTime(times2, N, K));
        System.out.println(networkDelayTimeUsingHeap(times2, N, K));
    }

    /**
     *  这是暴力解法?
     * @param times
     * @param N
     * @param K
     * @return
     */
    public int networkDelayTime(int[][] times, int N, int K) {
        //矩阵 这里 +1 是因为N是从1开始的, 遍历判断边界时不用 +1
        int[][] graph = new int[N + 1][N + 1];
        //标志, 是否访问过
        boolean[] flag = new boolean[N + 1];
        //点K到各点的最短距离
        int[] path = new int[N + 1];
        //初始化
        for (int i = 1; i < graph.length; i++) {
            for (int j = 1; j < graph[i].length; j++) {
                graph[i][j] = INF;
            }
        }
        //读入边
        for (int[] time : times) {
            graph[time[0]][time[1]] = time[2];
        }
        for (int i = 1; i < path.length; i++) path[i] = INF;
        path[K] = 0;

        while (true) {
            int index = -1;
            int min = Integer.MAX_VALUE;

            //找出没有被访问过的最小值
            for (int i = 1; i < graph.length; i++) {
                if (path[i] < min && !flag[i]) {
                    min = path[i];
                    index = i;
                }
            }

            // 所有点遍历完毕
            if (index == -1) break;
            // 标记为已访问
            flag[index] = true;

            for (int i = 1; i < graph[index].length; i++) {
                // 自身及不可达的点不需要计算
                if (graph[index][i] != INF &&
                        // 已遍历过的点不需要计算
                        !flag[i] &&
                        // 路径比之前远的不需要计算
                        path[index] + graph[index][i] < path[i]) {
                    // 更新为更短的路径
                    path[i] = path[index] + graph[index][i];
                }
            }
        }
        //找出最大距离
        int max = -1;
        for (int i = 1; i < path.length; i++) {
            // 有不可达的点
            if (path[i] == INF) {
                return -1;
            }
            if (max < path[i]) {
                max = path[i];
            }
        }
        return max;
    }

    /**
     * 堆优化 dijkstra
     * @param times
     * @param N
     * @param K
     * @return
     */
    public int networkDelayTimeUsingHeap(int[][] times, int N, int K) {
        // 存储, 相当于上面的二维表
        Map<Integer, List<GraphLine>> linesMap = new HashMap<>();
        // 堆, 初始点K到K的距离为0
        BinaryHeap<GraphLine> heap = new BinaryHeap<>();
        heap.insert(new GraphLine(K, K, 0));
        //初始化
        for (int[] time : times) {
            // 非开始的节点, 存储起来备用
            List<GraphLine> graphLines = linesMap.get(time[0]);
            if (graphLines == null) {
                graphLines = new ArrayList<>();
                linesMap.put(Integer.valueOf(time[0]), graphLines);
            }
            graphLines.add(new GraphLine(time[0], time[1], time[2]));
        }
        // 已遍历的节点
        Map<Integer, Integer> checked = new HashMap();
        while (!heap.isEmplty()) {
            // 获得当前最小值
            GraphLine graphLine = heap.deleteMin();
            // 如果匹配过, continue, 没匹配过, 标记匹配
            if (checked.containsKey(graphLine.to)) {
                continue;
            }
            checked.put(graphLine.to, graphLine.power);
            // 拿到to节点的to节点
            List<GraphLine> remove = linesMap.remove(Integer.valueOf(graphLine.to));
            // 当有节点只有入度没有出度的时候, remove为null
            if (remove != null) {
                for (GraphLine next : remove) {
                    // 必须是没有确定距离的点
                    if (!checked.containsKey(next.to)) {
                        // 到达graphLine点的最短距离已确定为graphLine.power
                        // 所以到next点的距离为next.power + graphLine.power, 放入堆看是不是最短距离
                        next.power = next.power + graphLine.power;
                        heap.insert(next);
                    }
                }
            }
        }
        // 如果遍历的节点不等于全部节点 -> 有节点没有遍历到
        if (checked.size() != N) {
            return -1;
        }
        int max = 0;
        for (int power : checked.values())
            max = Math.max(max, power);
        return max;
    }
}
