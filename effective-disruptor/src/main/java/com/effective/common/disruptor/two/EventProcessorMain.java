package com.effective.common.disruptor.two;

import com.lmax.disruptor.BatchEventProcessor;
import com.lmax.disruptor.RingBuffer;
import com.lmax.disruptor.SequenceBarrier;
import com.lmax.disruptor.YieldingWaitStrategy;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@Slf4j
public class EventProcessorMain {

    @SneakyThrows
    public static void main(String[] args) {

        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;
        /*
         * createSingleProducer创建一个单生产者的RingBuffer，
         * 第一个参数叫EventFactory，从名字上理解就是"事件工厂"，其实它的职责就是产生数据填充RingBuffer的区块。
         * 第二个参数是RingBuffer的大小，它必须是2的指数倍 目的是为了将求模运算转为&运算提高效率
         * 第三个参数是RingBuffer的生产都在没有可用区块的时候(可能是消费者（或者说是事件处理器） 太慢了)的等待策略
         */
        final RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(
                () -> new Trade.TradeBuilder().build(),
                BUFFER_SIZE,
                new YieldingWaitStrategy()
        );

        //创建一个线程池
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);

        //创建SequenceBarrier
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();

        //创建消息处理器
        BatchEventProcessor<Trade> transProcessor = new BatchEventProcessor<>(ringBuffer, sequenceBarrier, new TradeHandler());

        //这一步的目的是把消费者的位置信息引用注入到生产者 如果只有一个消费者的情况可以省略
        ringBuffer.addGatingSequences(transProcessor.getSequence());

        //把消息处理器提交到线程池
        executors.submit(transProcessor);

        //如果存在多个消费者，那么重复执行上面三行代码，把TradeHandler换成其他消费者类
        Future<?> future = executors.submit(new Callable<Trade>() {
            @Override
            public Trade call() throws Exception {
                long seq;
                for (int i = 0; i < 10; i++) {
                    seq = ringBuffer.next();//占一个坑-----ringBuffer一个可用区块
                    ringBuffer.get(seq).setPrice(Math.random() * 9999);//给这个区块放入数据
                    ringBuffer.publish(seq);//发布这个区块的数据使handler(consumer)可见
                }
                return null;
            }
        });

        future.get();//等待生成者结束
        log.info("等待2秒线程结束");
        Thread.sleep(2000);//等待一秒，等消费者处理完成
        log.info("通知处理器来处理");
        transProcessor.halt();//通知事件（或者说消息）处理器，可以结束了（并不是马上结束）
        executors.shutdown();//终止线程
    }
}
