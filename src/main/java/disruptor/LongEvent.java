package disruptor;

import com.lmax.disruptor.EventFactory;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LongEvent {
    private long value;

    public long getValue() {
        return value;
    }

    public void setValue(long value) {
        this.value = value;
    }

    public void set(long value) {
        this.value = value;
    }
}
