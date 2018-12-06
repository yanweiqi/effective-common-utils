package com.effective.common.concurrent.thread;

import org.junit.jupiter.api.Test;

public class ThreadLocalTest {

    static ThreadLocal<Integer> loopThread = new ThreadLocal();

    @Test
    public void loopThreadTest(){

        new Thread(()->{
            loopThread.set(100);
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + " loopThread:" + loopThread.get());
                loopThread.set(loopThread.get()+1);
            }
        }).start();

        new Thread(()->{
            loopThread.set(10);
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + " loopThread:" + loopThread.get());
                loopThread.set(loopThread.get()+1);
            }
        }).start();

        new Thread(()->{
            loopThread.set(1000);
            for(int i=0;i<10;i++){
                System.out.println(Thread.currentThread().getName() + " loopThread:" + loopThread.get());
                loopThread.set(loopThread.get()+1);
            }
        }).start();
    }
}
