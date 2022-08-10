package com.effective.common.concurrent.thread;

import java.util.ArrayDeque;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ReentrantLockDemo {

    final static Lock lock = new ReentrantLock();
    final static Condition empty = lock.newCondition();
    final static Condition full = lock.newCondition();

    final static Queue<String> queue = new ArrayDeque<>(10);

    public static void main(String[] args) {

        Runnable producer = () -> {
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < 30; i++) {
                lock.lock();
                try {
                    //当队列满时候等待写入
                    if (queue.size() == 10) {
                        try {
                            full.await();//
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    String msg = "生产消息:" + i;
                    queue.add(msg);
                    System.out.println(Thread.currentThread().getName() + ":" + msg);
                    empty.signal();//给消费者信号
                } finally {
                    lock.unlock();
                }
            }
        };

        Runnable consumer = () -> {
            try {
                Thread.sleep(1000 * 5);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            while (true) {
                lock.lock();
                try {
                    if (queue.isEmpty()) {
                        try {
                            empty.await();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        String msg = queue.remove();
                        System.out.println(Thread.currentThread().getName() + ":" + msg + "已消费");
                        full.signal();//
                    }
                } finally {
                    lock.unlock();
                }
            }
        };
        new Thread(producer).start();
        new Thread(consumer).start();
    }


}
