package framework.disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEventFactory implements EventFactory<LongEvent> {

    public LongEvent newInstance() {
        return new LongEvent();
    }
}
