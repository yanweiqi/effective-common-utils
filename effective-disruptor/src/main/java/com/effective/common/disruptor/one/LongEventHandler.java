package com.effective.common.disruptor.one;


import com.lmax.disruptor.EventHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class LongEventHandler implements EventHandler<LongEvent> {


    /**
     * @param longEvent 处理对象
     * @param l         sequence
     * @param b         endOfBatch
     */
    @Override
    public void onEvent(LongEvent longEvent, long l, boolean b)  {
        //消费，数据处理
        log.info(" longEvent:{} sequence:{} endOfBatch:{}", longEvent.getValue(), l, b);
    }
}
