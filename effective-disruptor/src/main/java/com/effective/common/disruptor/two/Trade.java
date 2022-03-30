package com.effective.common.disruptor.two;


import lombok.Builder;
import lombok.Data;

import java.util.concurrent.atomic.AtomicInteger;

@Builder
@Data
public class Trade {
    private String id;   //id
    private String name;  //名称
    private double price;//金额
    private AtomicInteger count = new AtomicInteger(0);
}
