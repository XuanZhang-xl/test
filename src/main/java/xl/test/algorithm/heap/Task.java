package xl.test.algorithm.heap;

/**
 * created by XUAN on 2019/3/11
 */
public class Task implements Comparable<Task> {

    private Long time;

    private String orderId;

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public int compareTo(Task task) {
        Long diff = this.getTime() - task.getTime();
        return diff.intValue();
    }

    public Task(Long time, String orderId) {
        this.time = time;
        this.orderId = orderId;
    }

    public Task() {
    }
}
