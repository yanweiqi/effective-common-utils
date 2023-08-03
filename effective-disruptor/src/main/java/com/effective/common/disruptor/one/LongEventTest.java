package com.effective.common.disruptor.one;

import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Slf4j
public class LongEventTest {

    public static void main(String[] args) {
        ThreadFactory threadFactory = Executors.defaultThreadFactory();

        LongEventFactory eventFactory = new LongEventFactory();

        //必须2的N次方
        int ringBufferSize = 1024 * 1024;

        //BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
        //WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();

        //SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
        //WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();

        //YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
        //WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();

        Disruptor<LongEvent> dis = new Disruptor<>(
                eventFactory,//工厂
                ringBufferSize, //大小
                threadFactory,//线程池
                ProducerType.SINGLE,
                new YieldingWaitStrategy() //策略
        );
        dis.handleEventsWith(new LongEventHandler()); //设置handle
        dis.start();

        LongEventProducer producer = new LongEventProducer(dis.getRingBuffer()); //传入ringBuffer
        ByteBuffer bf = ByteBuffer.allocate(8);
        for (int i = 0; i < 100; i++) {
            bf.putLong(0, i);
            producer.onData(bf);
        }
        log.info("Producer 完成");

        /**
         *
         */
        dis.shutdown();

        log.info("Disruptor 完成");
    }
}
