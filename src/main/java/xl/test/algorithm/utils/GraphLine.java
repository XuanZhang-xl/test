package xl.test.algorithm.utils;

/**
 * 图论 - 边
 * created by XUAN on 2019/12/4
 */
public class GraphLine implements Comparable<GraphLine> {

    // 边的起点
    public Integer from;
    public GraphPoint fromPoint;

    // 边的终点
    public Integer to;
    public GraphPoint toPoint;

    // 边的权重
    public Integer power;

    public GraphLine(Integer from, Integer to, Integer power) {
        this.from = from;
        this.to = to;
        this.power = power;
    }

    public GraphLine(GraphPoint fromPoint, GraphPoint toPoint, Integer power) {
        this.fromPoint = fromPoint;
        this.toPoint = toPoint;
        this.power = power;
    }

    @Override
    public int compareTo(GraphLine o) {
        return this.power.compareTo(o.power);
    }
}
