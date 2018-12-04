package com.effective.common.http.base;

import org.apache.http.HttpResponse;

import java.io.IOException;

/**
 * Created by yanweiqi on 2018/11/30.
 */
public interface IBaseHttpClient {

    String httpGet(String url) throws IOException;

    String getContent(HttpResponse response);
}
