package com.effective.common.concurrent.thread;

import java.util.ArrayDeque;
import java.util.Queue;

public class ProductAndConsume {

    //final static int size = 10;
    final static Queue<String> queue = new ArrayDeque<>(30);
    final static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        Runnable producer = () -> {
            for (int i = 0; i < 100; i++) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String msg = "消息：" + i;
                //队列未满，一直往里放消息
                synchronized (lock) {
                    while (5 == queue.size()) {
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    queue.offer(msg);
                    lock.notify();
                }
                System.out.println(Thread.currentThread().getName() + ":" + msg + " 已发送");
            }
        };


        Runnable consumer = () -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 5);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                synchronized (lock) {
                    String msg = queue.poll();
                    System.out.println(Thread.currentThread().getName() + ":" + msg + "已消费");
                    lock.notify();
                }
            }
        };
        new Thread(producer).start();
        new Thread(consumer).start();
    }


}
