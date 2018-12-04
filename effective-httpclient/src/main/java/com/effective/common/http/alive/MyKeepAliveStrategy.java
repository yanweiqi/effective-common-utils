package com.effective.common.http.alive;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;

/**
 * Created by yanweiqi on 2018/12/4.
 */
public class MyKeepAliveStrategy implements ConnectionKeepAliveStrategy {


    @Override
    public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
        HeaderElementIterator it = new BasicHeaderElementIterator(httpResponse.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();

            if (value != null && param.equalsIgnoreCase("timeout")) {
                return Long.parseLong(value) * 1000;
            }
        }
        return 5 * 1000;
    }
}
