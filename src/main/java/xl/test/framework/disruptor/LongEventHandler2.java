package xl.test.framework.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEventHandler2 implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 消费者
        System.out.println("Event: " + event + "  Thread:" + Thread.currentThread().getName());
    }
}
