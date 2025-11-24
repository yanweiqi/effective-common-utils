package com.effective.common.flux;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;

import java.time.Duration;

/**
 * 响应式编程案例汇总
 * 包含背压、错误处理、组合操作符等演示
 */
public class SamplesFluxTest {

        /**
         * 基础 Flux 订阅演示
         */
        @Test
        public void testSubscribe() {
                System.out.println("--- 开始测试基础订阅 ---");
                Flux.just("33333", "22222").subscribe(x -> System.out.println("接收到: " + x));
                System.out.println("--- 测试完成 ---");
        }

        /**
         * combineLatest 演示：组合最新值（实时组合）
         * 特点：任一流发出新值时，与其他流的最新值组合输出
         * 区别于 zip：zip 是一一配对，combineLatest 是动态组合最新值
         */
        @Test
        public void testCombineLatest() {
                System.out.println("--- combineLatest 基本用法 ---");
                Flux.combineLatest(
                                arr -> (String) arr[0] + "," + (String) arr[1],
                                Flux.just("A", "B", "C"),
                                Flux.just("1", "2")).subscribe(x -> System.out.println("combineLatest: " + x));
                System.out.println("说明：每当任一流发出新值，就与另一流的最新值组合");

                System.out.println("\n--- combineLatest vs zip 对比 ---");
                System.out.println("zip 输出（一一配对）：");
                Flux.zip(
                                Flux.just("A", "B", "C"),
                                Flux.just("1", "2"),
                                (a, b) -> a + b).subscribe(x -> System.out.println("  " + x));

                System.out.println("combineLatest 输出（组合最新值）：");
                Flux.combineLatest(
                                arr -> arr[0] + "" + arr[1],
                                Flux.just("A", "B", "C"),
                                Flux.just("1", "2")).subscribe(x -> System.out.println("  " + x));

                System.out.println("\n--- combineLatest 实际应用：温度+湿度监控 ---");
                Flux<String> temperature = Flux
                                .just("20°C", "21°C", "22°C")
                                .delayElements(Duration.ofMillis(100));

                Flux<String> humidity = Flux
                                .just("60%", "65%")
                                .delayElements(Duration.ofMillis(150));

                Flux.combineLatest(
                                arr -> "温度: " + arr[0] + ", 湿度: " + arr[1],
                                temperature,
                                humidity)
                                .blockLast(); // 等待完成并输出
                System.out.println("说明：任何传感器更新时，都输出当前两者的最新组合");
        }

        /**
         * zip 演示：配对组合流（拉链式合并）
         * 特点：将多个流的元素一一配对，使用函数组合成新元素
         */
        @Test
        public void testZip() {
                System.out.println("--- zip 基本用法（两个流配对） ---");
                Flux.zip(
                                Flux.just("A", "B", "C"),
                                Flux.just("1", "2", "3"),
                                (letter, number) -> letter + number)
                                .subscribe(x -> System.out.println("zip: " + x));

                System.out.println("\n--- zip 配对规则（短的流决定长度） ---");
                Flux.zip(Flux.just("A", "B", "C", "D"), // 4个元素
                                Flux.just("1", "2"), // 2个元素
                                (letter, number) -> letter + number).subscribe(x -> System.out.println("zip: " + x));
                System.out.println("说明：只输出 A1, B2，因为第二个流只有2个元素");

                System.out.println("\n--- zip 三个流配对 ---");
                Flux.zip(objects -> (String) objects[0] + (String) objects[1] + ": " + (String) objects[2],
                                Flux.just("用户", "订单"),
                                Flux.just("ID", "状态"),
                                Flux.just("001", "已完成"))
                                .subscribe(x -> System.out.println(x));
        }

        /**
         * concat 演示：串行合并流（按顺序执行）
         * 特点：等第一个流完全结束后，才订阅第二个流
         */
        @Test
        public void testConcat() {
                System.out.println("--- concat 基本用法 ---");
                Flux.concat(Flux.just("A", "B"), Flux.just("1", "2"))
                                .subscribe(x -> System.out.println("concat: " + x));

                System.out.println("\n--- concat 性能测试（串行执行） ---");
                long start = System.currentTimeMillis();
                Flux.concat(
                                Flux.just("第一个流").delayElements(Duration.ofMillis(100)),
                                Flux.just("第二个流").delayElements(Duration.ofMillis(100))).blockLast(); // 等待完成
                System.out.println("concat 总耗时: " + (System.currentTimeMillis() - start) + "ms");
                System.out.println("说明：串行执行，总耗时 = 各流耗时之和");
        }

        /**
         * merge 演示：并行合并流（同时执行）
         * 特点：立即订阅所有流，谁先发出数据谁先输出，不保证顺序
         */
        @Test
        public void testMerge() {
                System.out.println("--- merge 基本用法 ---");
                Flux.merge(
                                Flux.just("A", "B"),
                                Flux.just("1", "2")).subscribe(x -> System.out.println("merge: " + x));

                System.out.println("\n--- merge 性能测试（并行执行） ---");
                long start = System.currentTimeMillis();
                Flux.merge(
                                Flux.just("第一个流").delayElements(Duration.ofMillis(100)),
                                Flux.just("第二个流").delayElements(Duration.ofMillis(100))).blockLast(); // 等待完成
                System.out.println("merge 总耗时: " + (System.currentTimeMillis() - start) + "ms");
                System.out.println("说明：并行执行，总耗时 = 最慢的流的耗时");
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
                                () -> System.out.println("完成"));

                Flux<Integer> flux2 = Flux.just(1, 2, 0, 4)
                                .map(i -> 10 / i)
                                .onErrorReturn(-2);
                flux2.subscribe(
                                data -> System.out.println("收到: " + data),
                                err -> System.out.println("订阅时错误: " + err),
                                () -> System.out.println("完成"));
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

        /**
         * Flux 基本创建方式演示
         */
        @Test
        public void testFluxCreation() {
                System.out.println("--- 数组创建 Flux ---");
                Flux.just(new Integer[] { 1, 2, 3, 4 })
                        .subscribe(x -> System.out.println("数组元素: " + x));

                System.out.println("\n--- 可变参数创建 Flux ---");
                Flux.just(1, 2, 3, 4)
                        .subscribe(x -> System.out.println("参数元素: " + x));

                System.out.println("\n--- 从集合创建 Flux ---");
                Flux.fromIterable(Lists.newArrayList(10, 20, 30, 40))
                        .subscribe(x -> System.out.println("集合元素: " + x));

                System.out.println("\n--- range 创建数字序列 ---");
                Flux.range(1, 5)
                        .subscribe(x -> System.out.println("序列元素: " + x));
        }

        /**
         * interval 定时流演示：周期性发出递增的 Long 值
         */
        @Test
        public void testInterval() throws InterruptedException {
                System.out.println("--- interval 定时流（每秒发出一个值） ---");
                
                Flux.interval(Duration.ofMillis(1000))
                        .map(i -> "定时任务-" + i)
                        .take(5)  // 只取前 5 个
                        .subscribe(System.out::println);

                System.out.println("等待定时流完成...");
                Thread.sleep(6000);
                System.out.println("测试完成");
        }

        /**
         * interval 并发流演示：多个定时流同时执行
         */
        @Test
        public void testConcurrentIntervals() throws InterruptedException {
                System.out.println("--- 两个并发的 interval 流 ---");
                Flux.interval(Duration.ofMillis(1000))
                        .map(i -> "流1-执行" + i)
                        .take(5)
                        .subscribe(System.out::println);
                        
                Flux.interval(Duration.ofMillis(1000))
                        .map(i -> "流2-执行" + i)
                        .take(5)
                        .subscribe(System.out::println);
                System.out.println("等待两个流完成...");
                Thread.sleep(6000);
                System.out.println("测试完成");
        }

        /**
         * delayElements 延时发送演示：每个元素发送前延迟指定时间
         */
        @Test
        public void testDelayElements() throws InterruptedException {
                System.out.println("--- delayElements 延时发送（每个元素延迟1秒） ---");
                Flux.fromIterable(Lists.newArrayList(100, 200, 300, 400))
                        .delayElements(Duration.ofMillis(1000L))
                        .subscribe(x -> System.out.println("延时元素: " + x + " (时间: " + System.currentTimeMillis() % 10000 + ")"));
                
                System.out.println("开始时间: " + System.currentTimeMillis() % 10000);
                System.out.println("等待延时流完成...");
                Thread.sleep(5000);
                System.out.println("测试完成");
        }
}
