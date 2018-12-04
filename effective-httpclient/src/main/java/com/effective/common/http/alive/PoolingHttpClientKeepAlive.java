package com.effective.common.http.alive;

import com.effective.common.http.base.BaseHttpClient;
import com.effective.common.http.monitor.IdleConnectionMonitorThread;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;

/**
 * Created by yanweiqi on 2018/12/4.
 */
public class PoolingHttpClientKeepAlive extends BaseHttpClient {

    private static String url = "http://127.0.0.1:8080/ready";

    @Override
    public HttpClient getHttpClient() {
        return HttpClients
                .custom()
                .setConnectionManager(poolingConnManager)
                .setKeepAliveStrategy(new MyKeepAliveStrategy())
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void main(String[] args) throws InterruptedException {
        long runtime = 0;
        long oneTime = 1000;
        PoolingHttpClientKeepAlive poolingHttpClient = new PoolingHttpClientKeepAlive();
        new Thread(() -> {
            try {
                for (int i = 0; i < 3; i++) {
                    String status = poolingHttpClient.httpGet(url);
                    System.out.println(Thread.currentThread().getName() + ":" + status);
                }
                Thread.sleep(oneTime);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();


        IdleConnectionMonitorThread staleMonitor = new IdleConnectionMonitorThread(poolingConnManager);
        staleMonitor.start();

        while (true) {
            System.out.println("running time " + runtime);
            Thread.sleep(oneTime);
            runtime += oneTime;

            if (runtime > oneTime * 10) {
                staleMonitor.shutdown();
            }
        }
    }
}