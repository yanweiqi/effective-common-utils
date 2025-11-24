package com.effective.common.flux;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Arrays;
import java.util.List;

/**
 * 响应式编程案例汇总
 * 包含背压、错误处理、组合操作符等演示
 */
public class SamplesFluxTest {
            /**
             * 组合操作符演示：merge、zip、concat、combineLatest
             */
            @Test
            public void testCombineOperators() {
                System.out.println("--- merge ---");
                Flux.merge(Flux.just("A", "B"), Flux.just("1", "2"))
                        .subscribe(x -> System.out.println("merge: " + x));

                System.out.println("--- zip ---");
                Flux.zip(Flux.just("A", "B"), Flux.just("1", "2"), (a, b) -> a + b)
                        .subscribe(x -> System.out.println("zip: " + x));

                System.out.println("--- concat ---");
                Flux.concat(Flux.just("A", "B"), Flux.just("1", "2"))
                        .subscribe(x -> System.out.println("concat: " + x));

                System.out.println("--- combineLatest ---");
                Flux.combineLatest(
                        arr -> arr[0] + "," + arr[1],
                        Flux.just("A", "B"),
                        Flux.just("1", "2")
                ).subscribe(x -> System.out.println("combineLatest: " + x));
            }
            /**
             * 错误处理演示：onErrorReturn、onErrorResume、doOnError
             */
            @Test
            public void testErrorHandling() {
                Flux<Integer> flux = Flux.just(1, 2, 0, 4)
                        .map(i -> 10 / i)
                        .doOnError(e -> System.out.println("doOnError: " + e))
                        .onErrorResume(e -> {
                            System.out.println("onErrorResume: " + e);
                            return Flux.just(-1);
                        });
                flux.subscribe(
                        data -> System.out.println("收到: " + data),
                        err -> System.out.println("订阅时错误: " + err),
                        () -> System.out.println("完成")
                );

                Flux<Integer> flux2 = Flux.just(1, 2, 0, 4)
                        .map(i -> 10 / i)
                        .onErrorReturn(-2);
                flux2.subscribe(
                        data -> System.out.println("收到: " + data),
                        err -> System.out.println("订阅时错误: " + err),
                        () -> System.out.println("完成")
                );
            }
        /**
         * 背压演示：自定义 Subscriber 控制每次请求 2 个元素
         */
        @Test
        public void testBackpressure() {
                Flux<Integer> flux = Flux.range(1, 10);
                flux.subscribe(new Subscriber<Integer>() {
                        private Subscription subscription;
                        private int count = 0;

                        @Override
                        public void onSubscribe(Subscription s) {
                                this.subscription = s;
                                s.request(2); // 首次请求 2 个
                        }

                        @Override
                        public void onNext(Integer integer) {
                                System.out.println("收到: " + integer);
                                count++;
                                if (count % 2 == 0) {
                                        subscription.request(2); // 每收到 2 个再请求 2 个
                                }
                        }

                        @Override
                        public void onError(Throwable t) {
                                System.err.println("错误: " + t);
                        }

                        @Override
                        public void onComplete() {
                                System.out.println("完成");
                        }
                });
        }

    @Test
    public void test1() {
                        // 基础用法：自定义 Subscriber，输出每个元素
                        // 背压演示：每次请求 2 个元素，控制流速
                        // 错误处理演示：onErrorResume、onErrorReturn、doOnError
                        // 组合操作符演示：merge、zip、concat、combineLatest
                Subscriber<String> subscriber = new Subscriber<String>() {
                        @Override
                        public void onSubscribe(Subscription s) {
                        }

                        @Override
                        public void onNext(String o) {
                                System.out.println("onNext: " + o);
                        }

                        @Override
                        public void onError(Throwable t) {
                        }

                        @Override
                        public void onComplete() {
                        }
                };
                Flux.just("33333", "22222")
                                .subscribe(x->subscriber.onNext(x));
    }

    @Test
    public void test2() {
                // 字符流处理：去重、排序、行号绑定
        List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumps",
                "over",
                "the",
                "lazy",
                "dog"
        );
        Flux<Integer> lines = Flux.range(1, Integer.MAX_VALUE);
        Flux<String> wordsFlux = Flux.fromIterable(words);
        wordsFlux.flatMap(word -> Flux.fromArray(word.split("")))
                .distinct()
                .sort()
                .zipWith(lines, (word, line) -> line + " " + word)
                .subscribe(System.out::println);
    }

    @Test
    public void test3() throws InterruptedException, IOException {
                // 多种 Flux 创建与异步流演示
        Flux.just(new Integer[]{1, 2, 3, 4})
                .subscribe(System.out::println);

        //使用可变参数创建Flux
        Flux.just(1, 2, 3, 4)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(1000))
                //  map可以对数据进行处理
                .map(i -> "执行内容1：" + i)
                //限制执行10次
                .take(5)
                .subscribe(System.out::println);

        Flux.interval(Duration.ofMillis(1000))
                //  map可以对数据进行处理
                .map(i -> "执行内容2：" + i)
                //限制执行10次
                .take(5)
                .subscribe(System.out::println);

        Flux.fromIterable(Lists.newArrayList(100, 2777, 3777, 4777))
                //延时发送
                .delayElements(Duration.ofMillis(1000L))
                .subscribe(System.out::println);
        System.out.println("========");
        //避免主线程提前结束
        Thread.sleep(1000 * 15);

    }

    public static void main(String[] args) throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://127.0.0.1:8080/posts"))
                .timeout(Duration.ofSeconds(20))
                .header("Content-type", "application/json")
                .GET()
                .build();
        final HttpResponse<String> send = httpClient.send(
                request,
                HttpResponse.BodyHandlers.ofString()
        );
        System.out.println(send.body());

    }
}
