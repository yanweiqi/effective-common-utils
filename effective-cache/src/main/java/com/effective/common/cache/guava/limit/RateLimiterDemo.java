package com.effective.common.cache.guava.limit;


import com.google.common.util.concurrent.RateLimiter;

import java.util.concurrent.TimeUnit;

public class RateLimiterDemo {

    public static void main(String[] args) throws InterruptedException {

        //testFixedWarmingUp();
        testBurstWarmingUp();
        //testSmoothWarmingUp();
        Thread.sleep(1000 * 60 * 5);
    }

    /**
     * 平滑固定限流
     */
    public static void testFixedWarmingUp() {
        final RateLimiter r = RateLimiter.create(2);
        new Thread(() -> {
            while (true) {
                Double acq = r.acquire(1) * 1000;
                System.out.println(Thread.currentThread().getName() + ":" + System.currentTimeMillis() + " get 1 tokens " + acq);
            }
        }).start();
    }

    public static void testBurstWarmingUp() {
        final RateLimiter r = RateLimiter.create(2);
        new Thread(() -> {
            while (true) {
                Double acq = r.acquire(2) * 1000;
                System.out.println("acq:" + Thread.currentThread().getName() + ":" + System.currentTimeMillis() + " get 2 tokens " + "acq:" + acq);

                Double acq1 = r.acquire(1) * 1000;
                System.out.println(Thread.currentThread().getName() + ":" + System.currentTimeMillis() + " get 1 tokens " + "acq1:" + acq1);
            }
        }).start();
    }

    /**
     * 平滑预热限流
     */
    public static void testSmoothWarmingUp() {
        final RateLimiter r = RateLimiter.create(
                /*每秒的令牌数量*/2,
                /*预热时间*/3,
                /*预热单位*/TimeUnit.SECONDS
        );
        new Thread(() -> {
            while (true) {
                Double acq = r.acquire(1) * 1000;
                System.out.println(Thread.currentThread().getName() + ":" + System.currentTimeMillis() + " get 1 tokens " + acq);
            }
        }).start();
    }

}
