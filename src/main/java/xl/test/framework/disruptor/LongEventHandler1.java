package xl.test.framework.disruptor;

import com.lmax.disruptor.EventHandler;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEventHandler1 implements EventHandler<LongEvent> {
    @Override
    public void onEvent(LongEvent event, long sequence, boolean endOfBatch) throws Exception {
        // 消费者, 目前只是打印一行数据
        System.out.println("Event: " + event.getValue() + "  Thread:" + Thread.currentThread().getName());
    }
}
