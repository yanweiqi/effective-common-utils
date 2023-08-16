package com.effective.common.netty.cluster.utils;

import java.util.UUID;

/**
 * UUID生成工具类
 *
 *
 */
public class UUIDUtil {

    /**
     * 获得UUID的方法
     *
     * @return
     */
    public static String createUUID(){
        return UUID.randomUUID().toString().replaceAll("-","");
    }

    /**
     * 获得系统时间戳
     *
     * @return
     */
    public static long createIDForLong(){
        return System.currentTimeMillis();
    }

}
