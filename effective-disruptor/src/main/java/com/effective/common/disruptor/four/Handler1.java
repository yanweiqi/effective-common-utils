package com.effective.common.disruptor.four;

import com.effective.common.disruptor.two.Trade;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Handler1 implements EventHandler<Trade>, WorkHandler<Trade> {
    public  static ObjectMapper objectMapper = new ObjectMapper();
    @Override
    public void onEvent(Trade trade) throws Exception {
        trade.setName("h1");
        log.info("handler1 观察 {}",Handler1.objectMapper.writeValueAsString(trade));
        Thread.sleep(1000);
    }

    @Override
    public void onEvent(Trade arg0, long arg1, boolean arg2) throws Exception {
        this.onEvent(arg0);
    }
}