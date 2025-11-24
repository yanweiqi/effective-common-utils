package com.effective.common.flux;

import com.google.common.collect.Lists;
import org.junit.Test;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

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
                Flux.just(new Integer[] { 1, 2, 3, 4 }).subscribe(x -> System.out.println("数组元素: " + x));

                System.out.println("\n--- 可变参数创建 Flux ---");
                Flux.just(1, 2, 3, 4).subscribe(x -> System.out.println("参数元素: " + x));

                System.out.println("\n--- 从集合创建 Flux ---");
                Flux.fromIterable(Lists.newArrayList(10, 20, 30, 40)).subscribe(x -> System.out.println("集合元素: " + x));

                System.out.println("\n--- range 创建数字序列 ---");
                Flux.range(1, 5).subscribe(x -> System.out.println("序列元素: " + x));
        }

        /**
         * interval 定时流演示：周期性发出递增的 Long 值
         */
        @Test
        public void testInterval() throws InterruptedException {
                System.out.println("--- interval 定时流（每秒发出一个值） ---");
                Flux.interval(Duration.ofMillis(1000))
                                .map(i -> "定时任务-" + i)
                                .take(5) // 只取前 5 个
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
                                .subscribe(x -> System.out.println(
                                                "延时元素: " + x + " (时间: " + System.currentTimeMillis() % 10000 + ")"));

                System.out.println("开始时间: " + System.currentTimeMillis() % 10000);
                System.out.println("等待延时流完成...");
                Thread.sleep(5000);
                System.out.println("测试完成");
        }

        /**
         * then 演示：流完成后执行后续操作
         */
        @Test
        public void testThen() {
                System.out.println("--- then 用法演示 ---");
                Flux.just("A", "B", "C")
                                .doOnNext(x -> System.out.println("处理: " + x))
                                .then(Mono.fromRunnable(() -> System.out.println("主流完成后执行 then 逻辑")))
                                .block(); // 等待完成
                System.out.println("测试完成");
        }

        /**
         * map 操作符演示：元素转换
         */
        @Test
        public void testMap() {
                System.out.println("--- map 操作符演示 ---");
                Flux<Integer> flux = Flux.just(1, 2, 3, 4);
                flux.map(x -> x * 10).subscribe(x -> System.out.println("转换后: " + x));
                System.out.println("map 测试完成");
        }

        /**
         * flatMap 操作符演示：异步/并发映射与扁平化
         * 将每个元素映射为一个内部 Flux，并合并输出（可能乱序）
         */
        @Test
        public void testFlatMap() {
                System.out.println("--- flatMap 操作符演示 ---");
                Flux.just("A", "B", "C")
                                .flatMap(ch -> Flux
                                                .just(ch.toLowerCase(), String.format("%s-%s%s",
                                                                System.currentTimeMillis() / 1000, ch, ch))
                                                .delayElements(Duration.ofMillis(1000)))
                                .doOnNext(x -> System.out.println("flatMap 输出: " + x))
                                .blockLast(); // 等待内部流全部完成
                System.out.println("flatMap 测试完成");
        }

        @Test
        public void testConcatMap() {
                System.out.println("--- concatMap 操作符演示 ---");
                Flux.just("A", "B", "C")
                                .concatMap(ch -> Flux
                                                .just(ch.toLowerCase(), String.format("%s-%s%s",
                                                                System.currentTimeMillis() / 1000, ch, ch))
                                                .delayElements(Duration.ofMillis(1000)))
                                .doOnNext(x -> System.out.println("concatMap 输出: " + x))
                                .blockLast(); // 等待内部流全部完成
                System.out.println("concatMap 测试完成");
        }

        /**
         * reduce 操作符演示：聚合操作
         * 特点：将流中的元素两两聚合，最终输出一个 Mono
         */
        @Test
        public void testReduce() {
                System.out.println("--- reduce 操作符演示 ---");
                Flux<Integer> flux = Flux.just(1, 2, 3, 4, 5);
                flux.reduce((a, b) -> a + b)
                                .subscribe(result -> System.out.println("reduce 结果: " + result));
                System.out.println("reduce 测试完成");
        }

        /**
         * collectList 操作符演示：收集结果为集合
         * 特点：将流中的所有元素收集到一个 List 中，输出一个 Mono
         */
        @Test
        public void testCollectList() {
                System.out.println("--- collectList 操作符演示 ---");
                Flux<String> flux = Flux.just("A", "B", "C", "D");
                flux.collectList()
                                .subscribe(result -> System.out.println("collectList 结果: " + result));
                System.out.println("collectList 测试完成");
        }

        /**
         * switchIfEmpty 操作符演示：主流为空时切换到备选流
         * 特点：当主流没有发出任何元素时，切换到备选流
         */
        @Test
        public void testSwitchIfEmpty() {
                System.out.println("--- switchIfEmpty 操作符演示 ---");
                Flux<String> emptyFlux = Flux.empty();
                Flux<String> fallbackFlux = Flux.just("备选A", "备选B");
                emptyFlux.switchIfEmpty(fallbackFlux)
                                .subscribe(result -> System.out.println("switchIfEmpty 输出: " + result));
                System.out.println("switchIfEmpty 测试完成");
        }

        /**
         * take 和 skip 操作符演示：截取和跳过元素
         * 特点：take 用于截取前 N 个元素，skip 用于跳过前 N 个元素
         */
        @Test
        public void testTakeAndSkip() {
                System.out.println("--- take 和 skip 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 10);

                System.out.println("take(3) 截取前 3 个元素:");
                flux.take(3).subscribe(x -> System.out.println("take 输出: " + x));

                System.out.println("skip(3) 跳过前 3 个元素:");
                flux.skip(3).subscribe(x -> System.out.println("skip 输出: " + x));

                System.out.println("take 和 skip 测试完成");
        }

        /**
         * timeout 操作符演示：超时处理
         * 特点：当元素未在指定时间内发出时，触发超时错误或切换到备选流
         */
        @Test
        public void testTimeout() {
                System.out.println("--- timeout 操作符演示 ---");

                System.out.println("超时触发错误:");
                Flux<Integer> fluxWithTimeoutError = Flux.range(1, 5)
                                .delayElements(Duration.ofMillis(200))
                                .timeout(Duration.ofMillis(100));
                fluxWithTimeoutError.subscribe(
                                x -> System.out.println("接收到: " + x),
                                err -> System.out.println("超时错误: " + err));

                System.out.println("超时切换到备选流:");
                Flux<Integer> fluxWithFallback = Flux.range(1, 5)
                                .delayElements(Duration.ofMillis(100))
                                .timeout(Duration.ofMillis(200), Flux.just(-1, -2, -3));

                fluxWithFallback.subscribe(
                                x -> System.out.println("接收到: " + x),
                                err -> System.out.println("错误: " + err));

                System.out.println("timeout 测试完成");
        }

        /**
         * timeout 操作符演示：超时处理（第一个流超时，切换到备用流）
         * 特点：当第一个流未在指定时间内发出元素时，切换到备用流
         */
        @Test
        public void testTimeoutWithFallback() throws InterruptedException {
                System.out.println("--- timeout 操作符演示（切换到备用流） ---");
                Flux<String> mainFlux = Flux.just("A", "B", "C").delayElements(Duration.ofMillis(200));
                Flux<String> fallbackFlux = Flux.just("备用1", "备用2", "备用3");
                mainFlux.timeout(Duration.ofMillis(100), fallbackFlux)
                                .subscribe(x -> System.out.println("第一个流超时，启用接收备用流: " + x),
                                                err -> System.out.println("错误: " + err),
                                                () -> System.out.println("timeoutWithFallback 测试完成"));
                Thread.sleep(1000);
        }

        /**
         * retry 操作符演示：重试机制
         * 特点：当流发生错误时，重新订阅并尝试指定次数
         */
        @Test
        public void testRetry() {
                System.out.println("--- retry 操作符演示 ---");
                Flux<Integer> fluxWithError = Flux.just(1, 2, 3, 0)
                        .map(i -> 10 / i) // 第三个元素会触发除零错误
                        .doOnSubscribe(s -> System.out.println("订阅开始"))
                        .doOnError(e -> System.out.println("发生错误: " + e));

                //遇到错误停止        
                fluxWithError.retry(1) // 重试 1 次
                        .subscribe(
                                x -> System.out.println("接收到: " + x),
                                err -> System.out.println("最终错误: " + err),
                                () -> System.out.println("完成"));

                System.out.println("retry 测试完成");
        }

        /**
         * buffer 操作符演示：分组/批处理
         * 特点：将流中的元素按指定数量分组，收集到列表中
         * 每组是一个 List
         */
        @Test
        public void testBuffer() {
                System.out.println("--- buffer 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 10);
                System.out.println("按每 3 个元素分组:");
                flux.buffer(3).subscribe(batch -> System.out.println("分组: " + batch));
                System.out.println("buffer 测试完成");
        }

        /**
         * window 操作符演示：分组/窗口批处理
         * 特点：将流中的元素按指定数量分组，输出为 Flux<Flux<T>>
         * 
         * 每组是一个子流（Flux）
         */
        @Test
        public void testWindow() {
                System.out.println("--- window 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 10);

                System.out.println("按每 3 个元素分组:");
                flux.window(3).flatMap(window -> window.collectList()) // 将每个窗口收集为列表
                        .subscribe(batch -> System.out.println("窗口: " + batch));

                System.out.println("window 测试完成");
        }

        /**
         * first/last/elementAt 操作符演示：取第一个/最后一个/指定位置元素
         */
        @Test
        public void testFirstLastElementAt() {
                System.out.println("--- first/last/elementAt 操作符演示 ---");

                Flux<Integer> flux = Flux.range(1, 10);

                flux.next().subscribe(x -> System.out.println("第一个元素: " + x));
                flux.last().subscribe(x -> System.out.println("最后一个元素: " + x));
                flux.elementAt(4).subscribe(x -> System.out.println("第 5 个元素: " + x));

                System.out.println("first/last/elementAt 测试完成");
        }

        /**
         * subscribeOn/publishOn 操作符演示：线程调度
         */
        @Test
        public void testThreadScheduling() {
                System.out.println("--- subscribeOn/publishOn 操作符演示 ---");

                Flux<Integer> flux = Flux.range(1, 5)
                        .doOnNext(x -> System.out.println("生成: " + x + " (线程: " + Thread.currentThread().getName() + ")"))
                        .subscribeOn(Schedulers.boundedElastic())
                        .publishOn(Schedulers.parallel());

                flux.subscribe(x -> System.out.println("消费: " + x + " (线程: " + Thread.currentThread().getName() + ")"));

                System.out.println("subscribeOn/publishOn 测试完成");
        }

        /**
         * cache 操作符演示：结果缓存后可以被订阅多次
         */
        @Test
        public void testCache() {
                System.out.println("--- cache 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 5).cache();
                flux.subscribe(x -> System.out.println("订阅 1 接收到: " + x));
                flux.subscribe(x -> System.out.println("订阅 2 接收到: " + x));
                System.out.println("cache 测试完成");
        }

        /**
         * errorMap 操作符演示：错误转换
         */
        @Test
        public void testErrorMap() {
                System.out.println("--- errorMap 操作符演示 ---");
                Flux<Integer> flux = Flux.just(1, 2, 0, 4)
                        .map(i -> 10 / i)
                        .onErrorMap(e -> new RuntimeException("转换后的错误: " + e.getMessage()));
                flux.subscribe(
                        x -> System.out.println("接收到: " + x),
                        err -> System.out.println("错误: " + err));
                System.out.println("errorMap 测试完成");
        }

        /**
         * doOnNext/doOnComplete/doFinally 操作符演示：生命周期钩子
         */
        @Test
        public void testLifecycleHooks() {
                System.out.println("--- doOnNext/doOnComplete/doFinally 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 5)
                        .doOnNext(x -> System.out.println("处理元素: " + x))
                        .doOnComplete(() -> System.out.println("流完成"))
                        .doFinally(signal -> System.out.println("最终信号: " + signal));
                        
                flux.subscribe(x -> System.out.println("接收到: " + x));
                System.out.println("doOnNext/doOnComplete/doFinally 测试完成");
        }

        /**
         * blockFirst/blockLast 操作符演示：阻塞获取第一个/最后一个元素
         */
        @Test
        public void testBlockFirstAndBlockLast() {
                System.out.println("--- blockFirst/blockLast 操作符演示 ---");
                Flux<Integer> flux = Flux.range(1, 10);

                Integer first = flux.blockFirst();
                System.out.println("阻塞获取第一个元素: " + first);

                Integer last = flux.blockLast();
                System.out.println("阻塞获取最后一个元素: " + last);

                System.out.println("blockFirst/blockLast 测试完成");
        }

        /**
         * startWith 操作符演示：在流前追加元素或流
         */
        @Test
        public void testStartWith() {
                System.out.println("--- startWith 操作符演示 ---");
                Flux<Integer> flux = Flux.just(3, 4, 5).startWith(1, 2); // 在流前追加元素
                flux.subscribe(x -> System.out.println("接收到: " + x));
                System.out.println("startWith 测试完成");
        }

        /**
         * combineLatestDelayError 操作符演示：组合流并延迟错误
         * @throws InterruptedException 
         */
        @Test
        public void testCombineLatestDelayError() throws InterruptedException {
                System.out.println("--- combineLatestDelayError 操作符演示 ---");

                Flux<String> flux1 = Flux.just("A", "B", "C")
                        //.delayElements(Duration.ofMillis(100))
                        ;

                Flux<String> flux2 = Flux.just("1", "2")
                        .delayElements(Duration.ofMillis(150))
                        .concatWith(Flux.error(new RuntimeException("测试错误")));

                Flux.combineLatest(
                                flux1, flux2,
                                (a, b) -> a + "-" + b)
                        .subscribe(
                                x -> System.out.println("接收到: " + x),
                                err -> System.out.println("错误: " + err));

                System.out.println("combineLatestDelayError 测试完成");

                Thread.sleep(1000);
        }
}
