package com.effective.common.http.async;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;
import org.apache.http.impl.nio.client.CloseableHttpAsyncClient;
import org.apache.http.impl.nio.client.HttpAsyncClients;
import org.apache.http.nio.IOControl;
import org.apache.http.nio.client.methods.AsyncCharConsumer;
import org.apache.http.nio.client.methods.HttpAsyncMethods;
import org.apache.http.nio.protocol.HttpAsyncRequestProducer;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.nio.CharBuffer;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by yanweiqi on 2018/12/4.
 */
public class HttpAsyncClientTest {

    private static String getContent(HttpResponse response){
        HttpEntity entity = response.getEntity();// 获取响应实体
        String content = null;
        try {
            content = EntityUtils.toString(entity, "UTF-8");// 用string接收响应实体
            EntityUtils.consume(entity);// 消耗响应实体，并关闭相关资源占用
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return content;
    }

    @Test
    public void async1() throws ExecutionException, InterruptedException, IOException {
        CloseableHttpAsyncClient client = HttpAsyncClients.createDefault();
        client.start();
        HttpGet request = new HttpGet("http://127.0.0.1:8080/ready/timeout");
        Future<HttpResponse> future = client.execute(request, null);
        HttpResponse response = future.get();
        System.out.println(getContent(response));
        System.out.println("complete....");
    }

    @Test
    public void async2(){
        CloseableHttpAsyncClient httpclient = HttpAsyncClients.createDefault();
        httpclient.start();
        HttpGet request = new HttpGet("http://127.0.0.1:8080/ready/timeout");

        HttpAsyncRequestProducer producer3 = HttpAsyncMethods.create(request);

        AsyncCharConsumer<HttpResponse> consumer3 = new AsyncCharConsumer<HttpResponse>() {

            HttpResponse response;

            @Override
            protected void onResponseReceived(final HttpResponse response) {
                this.response = response;
            }

            @Override
            protected void onCharReceived(final CharBuffer buf, final IOControl ioctrl) throws IOException {
                // Do something useful
            }

            @Override
            protected void releaseResources() {
            }

            @Override
            protected HttpResponse buildResult(final HttpContext context) {
                return this.response;
            }
        };

        httpclient.execute(producer3, consumer3, new FutureCallback<HttpResponse>() {

            public void completed(final HttpResponse response3) {
                System.out.println(request.getRequestLine() + "->" + response3.getStatusLine());
            }

            public void failed(final Exception ex) {
                System.out.println(request.getRequestLine() + "->" + ex);
            }

            public void cancelled() {
                System.out.println(request.getRequestLine() + " cancelled");
            }
        });
    }
}
