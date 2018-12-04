package com.effective.common.http.base;

import org.apache.http.HttpHost;
import org.apache.http.conn.ConnectionPoolTimeoutException;
import org.apache.http.conn.ConnectionRequest;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.impl.conn.BasicHttpClientConnectionManager;

import java.util.concurrent.ExecutionException;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public class BasicHttpClientRoute {

    public static void main(String[] args) throws InterruptedException, ExecutionException, ConnectionPoolTimeoutException {
        long start = System.currentTimeMillis();
        BasicHttpClientConnectionManager connManager = new BasicHttpClientConnectionManager();
        HttpRoute route = new HttpRoute(new HttpHost("www.baeldung.com", 80));
        ConnectionRequest connRequest = connManager.requestConnection(route, null);
        do {
            long current = System.currentTimeMillis();
            System.out.println(connRequest.cancel());
            long interval = (current - start) / 1000;
            if(interval <= 10) connManager.close();
        } while (!connRequest.cancel());
        System.out.println("end");
    }


}
