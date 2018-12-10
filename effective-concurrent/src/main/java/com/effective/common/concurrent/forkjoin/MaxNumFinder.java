package com.effective.common.concurrent.forkjoin;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by yanweiqi on 2016/5/18.
 */
public class MaxNumFinder extends RecursiveTask<Integer> {

    private static final ThreadLocal<Integer> max = new ThreadLocal();

    private static final int THRESHOLD = 2;

    private int[] data;
    private int start;
    private int end;

    public MaxNumFinder(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public MaxNumFinder(int[] data) {
        this.data = data;
        this.start = 0;
        this.end = (data.length - 1);
    }

    @Override
    protected Integer compute() {
        Integer max;
        int length = end - start;
        if (length <= THRESHOLD) {
            max = compareMax(data, start, end);
            return max;
        } else if(start > end){
            return 0;
        } else {
            int middle = (end - start) / 2;
            MaxNumFinder left = new MaxNumFinder(data, start, start+middle);
            MaxNumFinder right = new MaxNumFinder(data, start+middle + 1, end);
            left.fork();
            right.fork();
            int leftResult = left.join();
            int rightResult = right.join();
            max = Math.max(leftResult, rightResult);
        }
        return max;
    }

    public synchronized Integer compareMax(int data[], int start, int end) {
        max.set(0);
        if (start == end) return data[start];

        if ((end - start) >= 1) {
            for (int i = start; i <= end; i++) {
                if (data[i] >= max.get()) {
                    max.set(data[i]);
                }
            }
        }
        return max.get();
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        int length = 10000000;
        final int[] data1 = new int[length];
        final Random random = new Random();
        for (int i = 0; i < data1.length; i++) {
            data1[i] = random.nextInt(1000);
        }

        long start = System.currentTimeMillis();
        ForkJoinPool forkJoinPool = new ForkJoinPool();
        MaxNumFinder task = new MaxNumFinder(data1);
        Future<Integer> future = forkJoinPool.submit(task);
        long end = System.currentTimeMillis();
        System.out.println(Thread.currentThread().getName() + "max:" + future.get()+":use time:"+(end-start));

        start = System.currentTimeMillis();
        Arrays.sort(data1, 0, length);
        end = System.currentTimeMillis();
        System.out.println("use time:"+ (end - start));
        //System.out.println(Arrays.toString(data1));

    }
}
