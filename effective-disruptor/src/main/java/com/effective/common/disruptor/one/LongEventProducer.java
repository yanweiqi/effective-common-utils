package com.effective.common.disruptor.one;

import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class LongEventProducer {

    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     *
     * @param bf nio缓冲对象
     */
    public void onData(ByteBuffer bf) {
        //可以把ringBuffer看做一个事件队列，那么next就是得到下面一个事件槽
        long sequence = ringBuffer.next();
        try {
            //用上面的索引取出一个空的事件用于填充
            LongEvent l = ringBuffer.get(sequence);
            l.setValue(bf.getLong(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
