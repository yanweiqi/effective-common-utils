package com.effective.common.concurrent;

import java.util.concurrent.RecursiveTask;

/**
 * Created by yanweiqi on 2016/5/16.
 */
public class MyTask extends RecursiveTask<Integer> {

    private int i;
    private int j;

    /**
     * @param  start
     * @param  end
     */
    public MyTask(int start,int end) {
        this.i = start;
        this.j = end;
    }

    @Override
    protected Integer compute() {
        if (i >= j) {
            return i * i;
        }
        MyTask newTask2 = new MyTask(i + 1,j);
        newTask2.fork();
        return i*i + newTask2.join();
    }
}
