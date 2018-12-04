package com.effective.common.http.base;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public class Worker implements Runnable {

    private IBaseHttpClient baseHttpClient;
    private String url;

    public Worker(IBaseHttpClient baseHttpClient, String url) {
        this.baseHttpClient = baseHttpClient;
        this.url = url;
    }

    /**
     * 这里强调一下，wait必须使用synchronized
     */
    @Override
    public  void run() {
        AtomicInteger i = new AtomicInteger(1);
        boolean isWait = false;
        try {
            synchronized (this) {
                do {
                    int n = i.getAndIncrement() % 2;
                    System.out.println(Thread.currentThread().getName() + ":" + n);
                    if (n == 0) {
                        String status = baseHttpClient.httpGet(url);
                        System.out.println(Thread.currentThread().getName() + ":" + status);
                    } else {
                        if (!isWait) {
                            System.out.println(Thread.currentThread().getName() + ":wait");
                            wait();
                        } else {
                            continue;
                        }
                    }
                    Thread.sleep(1000);
                } while (true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
