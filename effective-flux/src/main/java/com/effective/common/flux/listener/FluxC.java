package com.effective.common.flux.listener;

import org.junit.jupiter.api.Test; // Updated to JUnit 5
import reactor.core.publisher.Flux;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FluxC {

    /**
     * 时间处理器
     */
    interface MyEventProcessor {

        /**
         * 注册监听事件
         *
         * @param eventListener
         */
        void register(MyEventListener<String> eventListener);

        /**
         * @param values 数组
         */
        void dataChunk(String... values);

        /**
         *
         */
        void processComplete();
    }

    /**
     * 事件监听器
     *
     * @param <T>
     */
    interface MyEventListener<T> {

        /**
         * 接收数据
         *
         * @param chunk
         */
        void onDataChunk(List<T> chunk);

        /**
         * 处理完成
         */
        void processComplete();
    }

    /**
     * 事情处理器
     */
    public MyEventProcessor myEventProcessor = new MyEventProcessor() {

        private MyEventListener<String> eventListener;

        private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        @Override
        public void register(MyEventListener<String> eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void dataChunk(String... values) {
            executor.schedule(() -> eventListener.onDataChunk(Arrays.asList(values)), 500, TimeUnit.MILLISECONDS);
        }

        @Override
        public void processComplete() {
            executor.schedule(() -> eventListener.processComplete(), 500, TimeUnit.MILLISECONDS);
        }
    };


    Flux<String> bridge = Flux.create(sink -> myEventProcessor.register(new MyEventListener<>() {
        public void onDataChunk(List<String> chunk) {
            for (String s : chunk) {
                System.out.println(s);
                sink.next(s);
            }
        }

        public void processComplete() {
            System.out.println("执行完毕处理");
            sink.complete();
        }
    }));

    public void sub() throws InterruptedException {
       bridge.subscribe();
       Thread.sleep(1000 * 30);

   }


    @Test
    public void test() throws InterruptedException {

       new Thread(()->{
           try {
               sub();
           } catch (InterruptedException e) {
               e.printStackTrace();
           }
       }).start();

        System.out.println("---分割线---");
        myEventProcessor.dataChunk("foo", "bar", "baz");

        System.out.println("开始干活");
        Thread.sleep(1000 * 5);

    }

}
