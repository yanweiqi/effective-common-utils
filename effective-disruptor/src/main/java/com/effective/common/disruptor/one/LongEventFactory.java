package com.effective.common.disruptor.one;

import com.lmax.disruptor.EventFactory;

public class LongEventFactory implements EventFactory<LongEvent> {


    @Override
    public LongEvent newInstance() {
        return new LongEvent.LongEventBuilder().build();
    }



}
