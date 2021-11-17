package com.effective.common.disruptor.three;

import com.effective.common.disruptor.two.Trade;
import com.effective.common.disruptor.two.TradeHandler;
import com.lmax.disruptor.*;

import java.util.concurrent.*;

public class WorkProcessorMain {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        int BUFFER_SIZE = 1024;
        int THREAD_NUMBERS = 4;
        EventFactory<Trade> eventFactory = () -> Trade.builder().build();

        final RingBuffer<Trade> ringBuffer = RingBuffer.createSingleProducer(eventFactory, BUFFER_SIZE);
        SequenceBarrier sequenceBarrier = ringBuffer.newBarrier();
        ExecutorService executors = Executors.newFixedThreadPool(THREAD_NUMBERS);

        WorkerPool<Trade> workerPool = new WorkerPool<>(ringBuffer, sequenceBarrier, new IgnoreExceptionHandler(), new TradeHandler());
        workerPool.start(executors);
        // 如果存在多个消费者，那么重复执行上面三行代码，把TradeHandler换成其他消费者类
        Future<?> future = executors.submit((Callable<Trade>) () -> {
            long seq;
            for (int i = 0; i < 10; i++) {
                seq = ringBuffer.next();                           // 占一个坑-----ringBuffer一个可用区块
                ringBuffer.get(seq).setPrice(Math.random() * 9999);// 给这个区块放入数据
                ringBuffer.publish(seq);                           // 发布这个区块的数据使handler(consumer)可见
            }
            return null;
        });
        future.get();// 等待生成者结束
        Thread.sleep(1000);// 等待一秒，等消费者处理完成
        workerPool.halt();// 通知事件（或者说消息）处理器，可以结束了（并不是马上结束）
        executors.shutdown();// 终止线程
    }
}