package com.effective.common.concurrent.future;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Future;
import java.util.concurrent.TimeoutException;

public class F3 {

    private static Random rand = new Random();

    private static long t = System.currentTimeMillis();

     int getMoreData() throws InterruptedException, TimeoutException {

        System.out.println("begin to start compute");

        Thread.sleep(10000);
        System.out.println("end to start compute. passed " + (System.currentTimeMillis() - t) / 1000 + " seconds");
        if (1 == 1) {
            throw new TimeoutException("超时");
        }
        return rand.nextInt(1000);
    }

    public static void main(String[] args) throws Exception {
        F3 f3 = new F3();
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(()-> {
            try {
                return f3.getMoreData();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }
            return null;
        });
        Future<Integer> f = future.whenComplete((v, e) -> {
            System.out.println(v);
            System.out.println(e);
        });
        System.out.println(f.get());
        System.in.read();
    }
}
