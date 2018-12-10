package com.effective.common.concurrent.forkjoin;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * Created by yanweiqi on 2016/5/16.
 */
public class ExecTaskSplit extends RecursiveTask<Integer> {

    private static final int THRESHOLD = 10;
    private int start;
    private int end;

    public ExecTaskSplit(int start, int end) {
        this.start = start;
        this.end = end;
    }


    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecTaskSplit task = new ExecTaskSplit(1, 100);
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Future<Integer> result = forkJoinPool.submit(task);
        System.out.println(result.get());
        forkJoinPool.shutdown();
    }

    @Override
    protected Integer compute() {
        Integer sum = 0;

        if ((end - start) <= THRESHOLD) {
            for (int i = start; i <= end; i++) {
                sum += i;
            }
        } else {
            int middle = (start + end) / 2;

            ExecTaskSplit left = new ExecTaskSplit(start, middle);
            ExecTaskSplit right = new ExecTaskSplit(middle + 1, end);
            left.fork();
            right.fork();

            int leftSum = left.join();
            int rightSum = right.join();
            sum = leftSum + rightSum;
            System.out.println(Thread.currentThread().getName()+":"+ middle+":"+sum);
        }
        return sum;
    }
}
