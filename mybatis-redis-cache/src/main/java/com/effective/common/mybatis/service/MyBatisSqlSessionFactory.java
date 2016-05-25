package com.effective.common.mybatis.service;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.*;

import java.io.*;

/**
 * Created by yanweiqi on 2016/5/25.
 */
public class MyBatisSqlSessionFactory {
    private static final SqlSessionFactory FACTORY;

    static {
        try {
            Reader reader = Resources.getResourceAsReader("Configuration.xml");
            FACTORY = new SqlSessionFactoryBuilder().build(reader);
        } catch (IOException e) {
            throw new RuntimeException("Fatal Error. Cause: " + e, e);
        }
    }

    public static SqlSessionFactory getSqlSessionFactory() {
        return FACTORY;
    }
}
