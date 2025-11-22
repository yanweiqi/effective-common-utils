package com.effective.common.flux;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;

//import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@Slf4j
public class SamplesMono {

    static final Map<String, String> templates = new LinkedHashMap<String, String>() {
        {
            put("aDB", "a");
            put("bDB", "b");
            put("cDB", "c");
        }
    };


    /*
    StopWatch stopWatch = new StopWatch();
        Flux.fromIterable(templates.entrySet())
                .filterWhen((t) -> {
                    String k = t.getKey();
                    String v = t.getValue();
                    try {
                        Thread.sleep(sleep);//mock DB query
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    System.out.println(String.format(
                            "Thread id:%s, Thread name:%s, query:%s, value:%s , used ms:%s",
                            Thread.currentThread().getId(),
                            Thread.currentThread().getName(),
                            k,
                            v,
                            sleep)
                    );
                    return Mono.just(v.equals("b"));
                })
                .next()
                .doOnSuccess(templateEntry -> log.info("Match {} ", templateEntry.getKey()))
                .map(Map.Entry::getValue)
                .onErrorResume(NoSuchElementException.class, e -> Mono.empty())
                .onErrorMap(IndexOutOfBoundsException.class, Exception::new)
                .doOnTerminate(() -> System.out.println(stopWatch.getTime()));
        //Thread.sleep(1000 * 60);//mock DB query
     */

    static Executor delegate = new ThreadPoolExecutor(
            100,
            300,
            100,
            TimeUnit.SECONDS,
            new ArrayBlockingQueue(100)
    );

    @Test
    public void createStreamFromMonoCreate() {
        AtomicInteger onDispose = new AtomicInteger();
        AtomicInteger onCancel = new AtomicInteger();
        StepVerifier.create(Mono.create(s -> {
                    s.onDispose(onDispose::getAndIncrement)
                            .onCancel(onCancel::getAndIncrement)
                            .success("test1");
                }))
                .expectNext("test1")
                .verifyComplete();
        assertThat(onDispose.get(), is(1));
        assertThat(onCancel.get(), is(0));
    }

    @Test
    public void monoCreateOnCancel() throws InterruptedException {
        AtomicBoolean cancelled = new AtomicBoolean();
        Mono m1 = Mono.create(
                s -> s.onCancel(() -> cancelled.set(true)).success("test")
        );
        Thread t1 = new Thread(() -> {
            m1.block();
            //assertThat(cancelled.get(),is(true));
            m1.subscribe(s -> {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("2222->" + s);
            });
        });

        System.out.println("美丽的分割线----");

        Thread t2 = new Thread(() -> {
            Mono m2 = Mono.create(s -> s.onCancel(() -> cancelled.set(true)).success("ywq"));
            m2.block();

            m2.subscribe(s -> {
                System.out.println("3333->" + s);
            });
            //assertThat(cancelled.get(),is(false));
        });

        t1.start();
        t2.start();

        Thread.sleep(2000);
    }

    public static void main(String[] args) throws InterruptedException {


/*
        CompletableFuture<Integer> b = getB(7);
        CompletableFuture<Integer> c = getB(8);
        b.whenComplete((x,e)->{
            if(null == e) {
                System.out.println("b 结果处理---->"+x);
            } else {
                e.printStackTrace();
            }
        });

        c.whenComplete((x,e)->{
            if(null == e) {
                System.out.println("c 结果处理---->"+x);
            } else {
                e.printStackTrace();
            }
        });
 */
    }


    public static Mono<Integer> getA(Integer m) {
        Random r = new Random(m);
        Integer y = 0;
        for (int i = 0; i < 10; i++) {
            y = r.nextInt();
            try {
                Thread.sleep(500);//mock DB query
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(y);
        }
        return Mono.just(y);
    }


    protected static CompletableFuture<Integer> getB(Integer m) {

        CompletableFuture<Integer> cp = new CompletableFuture();
        Random r = new Random(m);
        Integer y = 0;
        for (int i = 0; i < 10; i++) {
            y = r.nextInt();
            try {
                Thread.sleep(500);//mock DB query
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(y);
        }

        cp.complete(y);
        return cp;
    }


    @Test
    public void testF1() {
        Flux<String> flux = Flux.generate(
                () -> 0,
                (state, sink) -> {
                    sink.next("3 x " + state + " = " + 3 * state);
                    if (state == 10) sink.complete();
                    return state + 1;
                });
        flux.subscribe(System.out::println);
    }

    //不可变对象的使用
    @Test
    public void testF2() {
        Flux<String> flux = Flux.generate(
                AtomicInteger::new,
                (state, sink) -> {
                    long i = state.getAndIncrement();
                    sink.next("3 x " + i + " = " + 3 * i);
                    if (i == 10) sink.complete();
                    return state;
                });
        flux.subscribe(System.out::println);
    }
}
