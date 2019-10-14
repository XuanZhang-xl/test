package xl.test.framework.disruptor;

import com.lmax.disruptor.EventHandler;

import java.util.Date;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEventHandler3 implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 消费者
        System.out.println("Event: " + new Date() + "  Thread:" + Thread.currentThread().getName());
    }
}
