package com.effective.common.concurrent.thread;

/**
 * Created by yanweiqi on 2018/12/3.
 * <p>
 * wait 必须要要用 synchronized 修饰
 */
public class WaitTest1 {

    public static void main(String[] args) throws InterruptedException {
        final WaitTest1 test1 = new WaitTest1();

        /**
         * wait1/notify1
         */
        test1.notify1(test1);

        /**
         * wait2/notify2
         */
        //test1.notify2(test1);

        /**
         * wait1/notify3
         */
        //test1.notify3(test1);
    }


    /**
     * 通过wait自动结束
     */
    public synchronized void wait1() {
        System.out.println(Thread.currentThread().getName() + " Start-----");
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " End-------");
    }

    public synchronized void notify1(WaitTest1 test1) {
       new Thread(()->test1.wait1()).start();

       new Thread(()->{
           synchronized (test1) {
               test1.notify();
           }
       }).start();

    }


    public synchronized void wait2() {
        System.out.println(Thread.currentThread().getName() + " Start-----");
        try {
            wait();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " End-------");
    }

    /**
     * 启用5个线程，通过notify()唤醒一个线程，notifyall()唤醒所有线程
     * @param test1
     */
    public void notify2(WaitTest1 test1) {
        for (int i = 0; i < 5; i++) {
            new Thread(() -> test1.wait2()).start();
        }

        synchronized (test1) {
            test1.notify();
        }

//        System.out.println("-----分割线-----");
//        synchronized (test1) {
//            test1.notifyAll();
//        }
    }

    public synchronized void wait3() {
        System.out.println(Thread.currentThread().getName() + " Start-----");
        int i = 0;
        try {
            do {
                System.out.println(Thread.currentThread().getName() + " working... " + i);
                wait();
                if (i == 5) break;
                i++;
            } while (true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(Thread.currentThread().getName() + " End-------");
    }

    /**
     * 一秒钟唤醒一次
     * @param test1
     */
    public synchronized void notify3(WaitTest1 test1) {
        new Thread(()->{
            test1.wait3();
        }).start();

        new Thread(() -> {
            while (true) {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                synchronized (test1) {
                    test1.notify();
                }
            }
        }).start();
    }



}
