package com.effective.common.netty.cluster.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期函数
 */
public interface DateParser {

    /**
     * 把字符串转换成日期
     *
     * @param value
     * @return
     * @throws ParseException
     */
    Date parse(String value) throws ParseException;


    /**
     * 基于日期格式化进行转换
     */
    class SimpleDateParser implements DateParser {
        //日期格式化
        protected final SimpleDateFormat format;

        public SimpleDateParser(SimpleDateFormat format) {
            this.format = format;
        }

        @Override
        public Date parse(String value) throws ParseException {
            synchronized (format) {
                return value == null || value.isEmpty() ? null : format.parse(value);
            }
        }
    }
}
