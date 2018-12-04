package com.effective.common.concurrent.thread;

/**
 * Created by yanweiqi on 2018/12/4.
 */
public class YieldTest1 {


    public static void yield1() {
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName() + " " + i);
            try {
                Thread.sleep(1000);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Thread.yield();
        }
    }

    public static void main(String[] args) {
        YieldTest1  test1 = new YieldTest1();
        new Thread(()->test1.yield1()).start();
        new Thread(()->test1.yield1()).start();
    }
}
