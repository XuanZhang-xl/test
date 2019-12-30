package xl.test.framework.springboot.lifecycle;

import org.springframework.context.Lifecycle;

/**
 * created by XUAN on 2019/12/30
 */
public class MyLifecycle implements Lifecycle {

    private volatile boolean isRunning = false;

    @Override
    public void start() {
        System.out.println("MyLifecycle#start");
        isRunning = true;
    }

    @Override
    public void stop() {
        System.out.println("MyLifecycle#stop");
        isRunning = false;
    }

    @Override
    public boolean isRunning() {
        return isRunning;
    }
}
