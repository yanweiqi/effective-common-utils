package com.effective.common.concurrent.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by yanweiqi on 2016/5/16.
 */
public class ExecTask extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 2;
    private int start;
    private int end;

    public ExecTask(int start, int end) {
        this.start = start;
        this.end = end;
    }

    //计算1*1+2*2+3*3+...........100*100的值。
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecTask task = new ExecTask(4, 5);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> result = forkJoinPool.submit(task);
        System.out.println(result.get());
        forkJoinPool.shutdown();
    }

    @Override
    protected Integer compute() {
        if (start == end) {
            return start * end;
        }
        ExecTask newTask2 = new ExecTask(start + 1, end);
        newTask2.fork();
        return start * start + newTask2.join();
    }
}
