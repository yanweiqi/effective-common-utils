package com.effective.common.concurrent;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.*;

/**
 * Created by yanweiqi on 2016/5/18.
 */
public class MaximumFinder extends RecursiveTask<Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6438997477419676192L;

	private static final int SEQUENTIAL_THRESHOLD = 5;

    private final int[] data;
    private final int start;
    private final int end;

    public MaximumFinder(int[] data, int start, int end) {
        this.data = data;
        this.start = start;
        this.end = end;
    }

    public MaximumFinder(int[] data) {
        this(data, 0, data.length);
    }

    @Override
    protected Integer compute() {
        final int length = end - start;
        CopyOnWriteArrayList<Integer> list = new CopyOnWriteArrayList<Integer>();
        if (length < SEQUENTIAL_THRESHOLD) {
            list.add(compute());
            return computeDirectly();
        }
        else{
            final int split = length / 2;
            final MaximumFinder left = new MaximumFinder(data, start, start + split);
            final MaximumFinder right = new MaximumFinder(data, start + split, end);
            left.fork();
            right.fork();
            int leftResult = left.join();
            int rightResult = right.join();
            return Math.max(leftResult,rightResult);
        }
    }

    private Integer computeDirectly() {
        System.out.println(Thread.currentThread() + " computing: " + start + " to " + end);
        int max = Integer.MIN_VALUE;
        for (int i = start; i < end; i++) {
            if (data[i] > max) {
                max = data[i];
            }
        }
        System.out.println(Thread.currentThread() + "max:"+max);
        return max;
    }

    public static void main(String[] args) {
        // create a random data set
        int length = 35;
        final int[] data = new int[length];
        final Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            data[i] = random.nextInt(1000);
        }
        Arrays.sort(data,0,length);
        for (int n :data) {
            System.out.print(n+",");
        }
        System.out.println("\n");
        // submit the task to the pool
        final ForkJoinPool pool = ForkJoinPool.commonPool();
        final MaximumFinder finder = new MaximumFinder(data);
        System.out.println(pool.invoke(finder));
    }
}
