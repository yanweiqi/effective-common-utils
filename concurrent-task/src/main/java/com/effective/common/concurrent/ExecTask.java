package com.effective.common.concurrent;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;

/**
 * Created by yanweiqi on 2016/5/16.
 */
public class ExecTask {

    //计算1*1+2*2+3*3+...........100*100的值。
    public static void main(String[] args) throws  ExecutionException, InterruptedException {
        MyTask mt = new MyTask(1,100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> result = forkJoinPool.submit(mt);
        System.out.println(result.get());
        forkJoinPool.shutdown();
    }
}
