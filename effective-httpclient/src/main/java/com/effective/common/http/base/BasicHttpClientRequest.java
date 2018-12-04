package com.effective.common.http.base;


import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.io.IOException;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public class BasicHttpClientRequest extends BaseHttpClient {

    private static String url = "http://127.0.0.1:8080/ready";
    private static BasicHttpClientConnectionManager connManager =new BasicHttpClientConnectionManager();

    @Override
    public HttpClient getHttpClient() {
        return HttpClients.custom()
                .setConnectionManager(connManager)
                .setDefaultRequestConfig(requestConfig)
                .build();
    }

    public static void main(String[] args) throws IOException, InterruptedException {

        BasicHttpClientRequest basicHttpClientRequest = new BasicHttpClientRequest();
        Thread t1 = new Thread(new Worker(basicHttpClientRequest,url));
        t1.start();
        while (true){
            System.out.println( t1.getName() + " stattus " + t1.isAlive() );
            Thread.sleep(1000 * 2);
            if(t1.isAlive()){
                t1.wait();
            } else {
                t1.notify();
            }
        }
    }



}

