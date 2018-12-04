package com.effective.common.http.pool;

import com.effective.common.http.base.BaseHttpClient;
import com.effective.common.http.base.Worker;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public class PoolingHttpClient extends BaseHttpClient {


    private static String url = "http://127.0.0.1:8080/ready";

    @Override
    public HttpClient getHttpClient() {
        return HttpClients
                .custom()
                .setConnectionManager(poolingConnManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        PoolingHttpClient poolingHttpClient = new PoolingHttpClient();

        Worker w1 = new Worker(poolingHttpClient, url);
        Thread t1 = new Thread(w1);

        t1.start();
        while (true) {
            Thread.sleep(1000);
            System.out.println("running ...");
            synchronized (w1) {
                w1.notify();
            }
        }
    }
}
