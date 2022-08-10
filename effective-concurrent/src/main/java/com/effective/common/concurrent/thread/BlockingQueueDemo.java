package com.effective.common.concurrent.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;

public class BlockingQueueDemo {

    static final int size = 50;

    static final int total = 70;

    static final BlockingQueue<String> queue = new ArrayBlockingQueue<>(size);

    static final AtomicInteger incr = new AtomicInteger();


    public static void main(String[] args) {
        Runnable producer = () -> {
            for (int i = 0; i < total; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String msg = "消息：" + i;
                try {
                    queue.offer(msg);
                } catch (Exception e1) {
                    e1.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName() + ":" + msg + " 已发送");
            }
        };

        Runnable consumer = () -> {
            while (true) {
                try {
                    Thread.sleep(1000 * 2);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    if (queue.size() == size) {
                        List<String> list = new ArrayList<>();
                        for (int i = 0; i < size; i++) {
                            list.add(queue.take());
                        }
                        list.forEach((s) -> {
                            try {
                                System.out.println(Thread.currentThread().getName() + ":" + s + "已消费");
                                incr.incrementAndGet();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        });
                    }
                    if (total - incr.get() < size) {
                        System.out.println("最后一次消费 incr:" + incr.incrementAndGet());
                        String s = queue.take();
                        System.out.println("最后一次消费:" + s);

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        new Thread(producer).start();
        //new Thread(producer).start();
        new Thread(consumer).start();

    }
}
