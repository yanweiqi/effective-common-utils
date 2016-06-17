package com.effective.common.concurrent.forkjoin;

import java.util.*;
import java.util.concurrent.RecursiveTask;

/**
 * Created by yanweiqi on 2016/5/16.
 */
public class MyTask extends RecursiveTask<Integer> {

    /**
	 * 
	 */
	private static final long serialVersionUID = -1379194934968891038L;
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


    public static void main(String[] args){
        List list = new ArrayList();
        list.add("12");
        list.add("44");
        String arrayStr = list.toString();
        System.out.println(arrayStr);

        List<String> list2 = new ArrayList<String>();
        Collections.addAll(list2,arrayStr.split(","));
        for (String s:list2) {
          System.out.println(s);
        }

    }
}
