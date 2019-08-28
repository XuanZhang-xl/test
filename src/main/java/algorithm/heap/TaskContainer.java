package algorithm.heap;

/**
 * created by XUAN on 2019/3/11
 */
public class TaskContainer implements Runnable {

    private Task taskInfo;

    @Override
    public void run() {
        //System.out.println(taskInfo.getOrderId() + " 任务已执行");
    }

    public TaskContainer(Task taskInfo) {
        this.taskInfo = taskInfo;
    }

    public TaskContainer() {
    }
}
