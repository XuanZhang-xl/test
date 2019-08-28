package framework.disruptor;

import com.lmax.disruptor.RingBuffer;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer)
    {
        this.ringBuffer = ringBuffer;
    }
}
