package com.effective.common.concurrent.future;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import org.junit.Test;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.concurrent.*;

public class BasicFuture {

    @Test
    public void testBase() throws ExecutionException, InterruptedException {
        ExecutorService es = Executors.newFixedThreadPool(10);
        Future<Integer> f = es.submit(() -> {
            // 长时间的异步计算
            // ……
            // 然后返回结果
            return 100;
        });
        //        while(!f.isDone())
        //            ;
        f.get();
    }

    @Test
    public void testNetty() {
        /*
        String host = "";
        int port=0;
        Bootstrap b = Bootstrap;
        ChannelFuture future = b.connect(new InetSocketAddress(host, port));
        future.addListener(new ChannelFutureListener()
        {
            @Override
            public void operationComplete(ChannelFuture future) throws Exception
            {
                if (future.isSuccess()) {
                    // SUCCESS
                }
                else {
                    // FAILURE
                }
            }
        });
         */
    }

    @Test
    public void testGuava(){
        /*
        final String name = ...;
        inFlight.add(name);
        ListenableFuture<Result> future = service.query(name);
        future.addListener(new Runnable() {
            public void run() {
                processedCount.incrementAndGet();
                inFlight.remove(name);
                lastProcessed.set(name);
                logger.info("Done with {0}", name);
            }
        }, executor);
         */
    }


    @Test
    public void t1() throws ExecutionException, InterruptedException {
        CompletableFuture<Integer> future = CompletableFuture.supplyAsync(() -> {
            int i = 1/0;
            return 100;
        });
        future.getNow(00);//有点特殊，如果结果已经计算完则返回结果或者抛出异常，否则返回给定的valueIfAbsent值
        future.join();//返回计算的结果或者抛出一个unchecked异常(CompletionException)，它和get对抛出的异常的处理有些细微的区别，你可以运行下面的代码进行比较：
        future.get(); //同步获取
    }



    @Test
    public void t2() throws IOException {
    }
}
