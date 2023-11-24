package com.effective.common.disruptor.one;

import com.lmax.disruptor.EventFactory;
import com.lmax.disruptor.EventHandler;
import com.lmax.disruptor.WorkHandler;
import com.lmax.disruptor.YieldingWaitStrategy;
import com.lmax.disruptor.dsl.Disruptor;
import com.lmax.disruptor.dsl.ProducerType;
import lombok.extern.slf4j.Slf4j;

import java.nio.ByteBuffer;
import java.util.concurrent.*;

@Slf4j
public class LongEventTest {


    public static void main(String[] args) {
        log.info("------单生产者&单消费者（广播模式）--------");
        singleProducer();
        log.info("------单生产者&单消费者（广播模式）--------");
//
//        log.info("------单生产者&多消费者(广播模式)--------");
//        singleProducerMoreConsumer();
//        log.info("------单生产者&多消费者(广播模式)--------");
//
//        log.info("------单生产者&多消费者(集群模式)--------");
//        singleProducerMoreConsumerCluster();
//        log.info("------单生产者&多消费者(集群模式)--------");

//        log.info("------多生产者&多消费者(集群模式)--------");
//        moreProducerMoreConsumerCluster();
//        log.info("------多生产者&多消费者(集群模式)--------");
    }

    /**
     * 单生产者&单消费者（广播模式）
     */
    public static void singleProducer() {
        LongEventFactory eventFactory = new LongEventFactory();
        Disruptor<LongEvent> disruptor = getSingleDisruptor(eventFactory);
        disruptor.handleEventsWith(new LongEventHandler()); //设置消费者
        disruptor.start(); //启动
        LongEventProducer producer = new LongEventProducer(disruptor.getRingBuffer());  //设置生产者，传入disruptor的ringBuffer
        ByteBuffer bf = ByteBuffer.allocate(8);
        for (int i = 0; i < 6; i++) {
            producer.onData(bf.putLong(0, i)); //模拟生产数据
        }
        disruptor.shutdown();
        log.info("Disruptor 完成");
    }

    /**
     * 单生产者&多消费者(广播模式)
     */
    public static void singleProducerMoreConsumer() {
        EventHandler<LongEvent>[] consumers = new LongEventHandler[4];
        for(int i=0;i<consumers.length;i++){
            consumers[i] = new LongEventHandler();
        }

        LongEventFactory eventFactory = new LongEventFactory();
        Disruptor<LongEvent> disruptor = getSingleDisruptor(eventFactory);
        disruptor.handleEventsWith(consumers); //设置多个消费者
        disruptor.start(); //启动

        LongEventProducer producer = new LongEventProducer(disruptor.getRingBuffer());  //设置生产者，传入disruptor的ringBuffer
        ByteBuffer bf = ByteBuffer.allocate(8);
        for (int i = 0; i < 6; i++) {
            producer.onData(bf.putLong(0, i)); //模拟生产数据
        }
        disruptor.shutdown();
        log.info("Disruptor 完成");
    }

    /**
     * 单生产者&多消费者(集群模式)
     */
    public static void singleProducerMoreConsumerCluster() {

        //多消费者集群模式
        WorkHandler<LongEvent>[] consumers = new LongEventHandler[4];
        for(int i=0;i<consumers.length;i++){
            consumers[i] = new LongEventHandler();
        }

        LongEventFactory eventFactory = new LongEventFactory();
        Disruptor<LongEvent> disruptor = getSingleDisruptor(eventFactory);
        disruptor.handleEventsWithWorkerPool(consumers); //设置多个消费者,todo 集群模式 handleEventsWithWorkerPool
        disruptor.start(); //启动

        LongEventProducer producer = new LongEventProducer(disruptor.getRingBuffer());  //设置生产者，传入disruptor的ringBuffer
        ByteBuffer bf = ByteBuffer.allocate(8);
        for (int i = 0; i < 6; i++) {
            producer.onData(bf.putLong(0, i)); //模拟生产数据
        }
        disruptor.shutdown();
        log.info("Disruptor 完成");
    }


    /**
     * 多生产者&多消费者(集群模式)
     */
    public static void moreProducerMoreConsumerCluster() {
        LongEventFactory eventFactory = new LongEventFactory();
        Disruptor<LongEvent> disruptor = getSingleDisruptor(eventFactory);

        //多消费者集群模式
        WorkHandler<LongEvent>[] consumers = new LongEventHandler[4];
        for(int i=0;i<consumers.length;i++){
            consumers[i] = new LongEventHandler();
        }
        disruptor.handleEventsWithWorkerPool(consumers); //设置多个消费者

        disruptor.start(); //启动
        LongEventProducer producer1 = new LongEventProducer(disruptor.getRingBuffer());  //设置生产者，传入disruptor的ringBuffer
        LongEventProducer producer2 = new LongEventProducer(disruptor.getRingBuffer());  //设置生产者，传入disruptor的ringBuffer
        ByteBuffer bf = ByteBuffer.allocate(8);
        for (int i = 0; i < 6; i++) {
            producer1.onData(bf.putLong(0, i)); //模拟生产数据
            producer2.onData(bf.putLong(0, i)); //模拟生产数据
        }
        disruptor.shutdown();
        log.info("Disruptor 完成");
    }


    /**
     * 线程工厂
     */
    final static ThreadFactory threadFactory = Executors.defaultThreadFactory();

    /**
     * 必须2的N次方
     */
    final static int ringBufferSize = 1024 * 1024;

    /**
     * 单生产者模式，指定ProducerType.SINGLE
     *
     * @param eventFactory 事件工厂
     * @return Disruptor
     */
    public static Disruptor<LongEvent> getSingleDisruptor(EventFactory<LongEvent> eventFactory) {
        //BlockingWaitStrategy 是最低效的策略，但其对CPU的消耗最小并且在各种不同部署环境中能提供更加一致的性能表现
        //WaitStrategy BLOCKING_WAIT = new BlockingWaitStrategy();

        //SleepingWaitStrategy 的性能表现跟BlockingWaitStrategy差不多，对CPU的消耗也类似，但其对生产者线程的影响最小，适合用于异步日志类似的场景
        //WaitStrategy SLEEPING_WAIT = new SleepingWaitStrategy();

        //YieldingWaitStrategy 的性能是最好的，适合用于低延迟的系统。在要求极高性能且事件处理线数小于CPU逻辑核心数的场景中，推荐使用此策略；例如，CPU开启超线程的特性
        //WaitStrategy YIELDING_WAIT = new YieldingWaitStrategy();
        return new Disruptor<>(
                eventFactory,//工厂
                ringBufferSize, //大小
                threadFactory,//线程池
                ProducerType.SINGLE,
                new YieldingWaitStrategy() //策略
        );
    }
}
