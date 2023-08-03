package com.effective.common.disruptor.one;

import com.lmax.disruptor.RingBuffer;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;

@Slf4j
public class LongEventProducer {


    private final RingBuffer<LongEvent> ringBuffer;

    public LongEventProducer(final RingBuffer<LongEvent> ringBuffer) {
        this.ringBuffer = ringBuffer;
    }

    /**
     *
     * @param bf nio缓冲对象
     */
    public void onData(ByteBuffer bf) {
        //ringBuffer是一个事件队列，那么next返回最后一条记录的位置
        long sequence = ringBuffer.next();
        try {
            //sequence位置取出一个空的事件
            LongEvent event = ringBuffer.get(sequence);
            event.setValue(bf.getLong(0));
        } catch (Exception e) {
            log.error(e.getMessage(), e);
        } finally {
            ringBuffer.publish(sequence);
        }
    }
}
