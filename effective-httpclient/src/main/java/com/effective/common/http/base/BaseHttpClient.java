package com.effective.common.http.base;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.util.EntityUtils;

import java.io.IOException;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public abstract class BaseHttpClient implements IBaseHttpClient {

    protected static PoolingHttpClientConnectionManager poolingConnManager = new PoolingHttpClientConnectionManager();

    static {
        poolingConnManager.setMaxTotal(200);
        poolingConnManager.setDefaultMaxPerRoute(3); //每个连接的路由
    }

    protected static RequestConfig requestConfig = RequestConfig.custom()
            .setConnectTimeout(2000)
            .setConnectionRequestTimeout(50)
            .setSocketTimeout(1050)
            .build();

    public abstract HttpClient getHttpClient();

    public String httpGet(String url) throws IOException {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = getHttpClient().execute(httpGet);
        return getContent(response);
    }

    public String getContent(HttpResponse response) {
        HttpEntity entity = response.getEntity();// 获取响应实体
        String content = null;
        try {
            content = EntityUtils.toString(entity, "UTF-8");// 用string接收响应实体
            EntityUtils.consume(entity);// 消耗响应实体，并关闭相关资源占用
        } catch (ParseException e1) {
            e1.printStackTrace();
        } catch (IOException e1) {
            e1.printStackTrace();
        } finally {
            if (response instanceof CloseableHttpResponse) {
                CloseableHttpResponse closeableHttpResponse = (CloseableHttpResponse) response;
                try {
                    closeableHttpResponse.close();
                    System.out.println("closeableHttpResponse " + Thread.currentThread().getName() + " close");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return content;
    }
}
