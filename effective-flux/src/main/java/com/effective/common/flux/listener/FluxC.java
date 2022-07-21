package com.effective.common.flux.listener;

import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class FluxC {

    interface MyEventProcessor {
        void register(MyEventListener<String> eventListener);
        void dataChunk(String... values);
        void processComplete();
    }

    interface MyEventListener<T> {
        void onDataChunk(List<T> chunk);
        void processComplete();
    }

    private MyEventProcessor myEventProcessor = new MyEventProcessor() {

        private MyEventListener<String> eventListener;
        private ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();

        @Override
        public void register(MyEventListener<String> eventListener) {
            this.eventListener = eventListener;
        }

        @Override
        public void dataChunk(String... values) {
            executor.schedule(() -> eventListener.onDataChunk(Arrays.asList(values)),
                    500,
                    TimeUnit.MILLISECONDS
            );
        }

        @Override
        public void processComplete() {
            executor.schedule(() -> eventListener.processComplete(),
                    500,
                    TimeUnit.MILLISECONDS
            );
        }
    };


    Flux<String> bridge = Flux.create(sink -> {
        myEventProcessor.register(
                new MyEventListener<>() {
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
                });
    });

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
        //myEventProcessor.dataChunk("foo", "bar", "baz");
        //myEventProcessor.processComplete();
        //bridge.subscribe();

        Thread.sleep(1000);

        System.out.println("开始干活");

//        StepVerifier.withVirtualTime(() -> bridge)
//                .expectSubscription()
//                .expectNoEvent(Duration.ofSeconds(10))
//                .then(() -> myEventProcessor.dataChunk("foo", "bar", "baz"))
//                .expectNext("foo", "bar", "baz")
//                .expectNoEvent(Duration.ofSeconds(10))
//                .then(() -> myEventProcessor.processComplete())
//                .verifyComplete();

        myEventProcessor.dataChunk("1", "2", "3");
        myEventProcessor.processComplete();


        Thread.sleep(1000 * 60);

    }

}
