package xl.test.framework.disruptor;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.util.DaemonThreadFactory;

import java.nio.ByteBuffer;

/**
 * created by zhangxuan9 on 2019/2/26
 */
public class LmaxDisruptor {

    public static void main(String[] args) throws Exception {
        System.out.println("Thread:" + Thread.currentThread().getName());
        // The factory for the event
        LongEventFactory factory = new LongEventFactory();

        // Specify the size of the ring buffer, must be power of 2.
        int bufferSize = 1024;

        // Construct the Disruptor
        Disruptor<LongEvent> disruptor = new Disruptor<LongEvent>(factory, bufferSize, DaemonThreadFactory.INSTANCE);
        // Connect the handler
        EventHandler[] eventHandlers = {new LongEventHandler1(),new LongEventHandler2(),new LongEventHandler3()};
        EventHandlerGroup eventHandlerGroup = disruptor.handleEventsWith(eventHandlers);

        // Start the Disruptor, starts all threads running
        disruptor.start();

        // Get the ring buffer from the Disruptor to be used for publishing.
        RingBuffer<LongEvent> ringBuffer = disruptor.getRingBuffer();

        LongEventProducer producer = new LongEventProducer(ringBuffer);

        ByteBuffer bb = ByteBuffer.allocate(8);
        for (long l = 0; true; l++) {
            // Grab the next sequence
            long sequence = ringBuffer.next();
            try {
                bb.putLong(0, l);
                // Get the entry in the Disruptor
                LongEvent event = ringBuffer.get(sequence);
                // for the sequence
                event.set(bb.getLong(0));  // Fill with data
            } finally {
                ringBuffer.publish(sequence);
            }
            Thread.sleep(1000);
        }
    }

}
