package com.effective.common.disruptor.one;


import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongEventHandler implements EventHandler<LongEvent>, WorkHandler<LongEvent> {


    /**
     * @param longEvent 处理对象
     * @param l         sequence
     * @param b         endOfBatch
     */
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b)  {
        log.info("广播模式 线程{} longEvent:{} sequence:{} endOfBatch:{}",Thread.currentThread().getName(), longEvent.getValue(), l, b);
    }


    @Override
    public void onEvent(LongEvent longEvent)  {
        log.info("集群模式 线程{} longEvent:{}",Thread.currentThread().getName(), longEvent.getValue());
    }

}
