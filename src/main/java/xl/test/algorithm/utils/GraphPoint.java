package xl.test.algorithm.utils;

import java.util.ArrayList;
import java.util.List;

/**
 * 图论 - 点
 * created by XUAN on 2019/12/4
 */
public class GraphPoint {

    // 编号
    public Integer index;
    // 入度
    public List<GraphLine> in;
    // 出度
    public List<GraphLine> out;

    public void addIn(GraphLine line) {
        addLine(in, line);
    }
    public void addOut(GraphLine line) {
        addLine(out, line);
    }
    private void addLine(List<GraphLine> lines, GraphLine line) {
        if (line == null) {
            return;
        }
        if (lines == null) {
            lines = new ArrayList<>();
        }
        lines.add(line);
    }
}
