package com.effective.common.disruptor.four;

import com.effective.common.disruptor.two.Trade;
import com.lmax.disruptor.BusySpinWaitStrategy;
import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.EventHandlerGroup;
import com.lmax.disruptor.dsl.ProducerType;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {

    public static void main(String[] args) throws InterruptedException {

        long beginTime = System.currentTimeMillis();

        int bufferSize = 1024;

        ExecutorService executor = Executors.newFixedThreadPool(8);
        Disruptor<Trade> disruptor = new Disruptor<>(() ->
                Trade.builder().build(),
                bufferSize, executor,
                ProducerType.SINGLE,
                new BusySpinWaitStrategy()
        );

        //菱形操作
        //使用disruptor创建消费者组C1,C2
        //EventHandlerGroup<Trade> handlerGroup = disruptor.handleEventsWith(new Handler1(),new Handler2());
        //声明在C1,C2完事之后执行JMS消息发送操作 也就是流程走到C3
        //disruptor.after(new Handler3());
        //輸出結果：
//		handler1 set name:
//		handler2 set price:
//		handler3: name: h1 , price: 17.0;  instance: com.moudle.disruptorDemo.generate1.Trade@220a5c4d

        //六边形操作
        Handler1 h1 = new Handler1();
        Handler2 h2 = new Handler2();
        Handler3 h3 = new Handler3();
        Handler4 h4 = new Handler4();
        Handler5 h5 = new Handler5();
        disruptor.handleEventsWith(h1);
        disruptor.after(h1).handleEventsWith(h2);
        disruptor.after(h2).handleEventsWith(h3);
        disruptor.after(h3).handleEventsWith(h4);
        disruptor.after(h4).handleEventsWith(h5);
        //输出结果：
        // handler1 set name:
        // handler2 set price:
        // handler4 set addName:
        // handler5 set add price:
        // handler3: name: h1h4 , price: 20.0;  instance: com.moudle.disruptorDemo.generate1.Trade@5e6d6957

		//顺序执行
		disruptor.handleEventsWith(new Handler1()).
    	handleEventsWith(new Handler2()).
    	handleEventsWith(new Handler3());
		//输出结果：
        //handler1 set name:
        //handler2 set price:
        //handler3: name: h1 , price: 17.0;  instance: com.moudle.disruptorDemo.generate1.Trade@331d6441

        disruptor.start();//启动
        CountDownLatch latch = new CountDownLatch(1);
        //生产者准备
        executor.submit(new TradePublisher(disruptor, latch));
        latch.await();//等待生产完成
        disruptor.shutdown();
        executor.shutdown();

    }
}
