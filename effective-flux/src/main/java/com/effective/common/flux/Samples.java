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


public class Samples {

    @Test
    public void test1() {
        Subscriber subscriber = new Subscriber() {
            @Override
            public void onSubscribe(Subscription s) {
                System.out.println();
            }

            @Override
            public void onNext(Object o) {

            }

            @Override
            public void onError(Throwable t) {

            }

            @Override
            public void onComplete() {

            }
        };
        Flux.just("Howdy", "Word").subscribe(subscriber);
    }

    @Test
    public void test2() {
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
        Flux.just(new Integer[]{1, 2, 3, 4}).subscribe(System.out::println);


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
        final HttpResponse<String> send = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(send.body());

    }
}



