package com.effective.common.disruptor.two;

import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;

import java.util.UUID;

public class TradeHandler implements EventHandler<Trade>, WorkHandler<Trade> {

    @Override
    public void onEvent(Trade event) throws Exception {
        //生成订单id
        event.setId(UUID.randomUUID().toString());
        System.out.println(event);
    }
    @Override
    public void onEvent(Trade event, long sequence, boolean endOfBatch) throws Exception {
        this.onEvent(event);
    }
}
