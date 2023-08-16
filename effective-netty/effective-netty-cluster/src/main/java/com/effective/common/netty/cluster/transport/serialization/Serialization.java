package com.effective.common.netty.cluster.transport.serialization;

import java.util.List;

/**
 * Serialization
 *
 *
 * @date 2020/3/8
 */
public interface Serialization {

    /**
     * JSON
     */
    byte JSON_ID = 1;

    /**
     * PROTOSTUFF
     */
    byte PROTOSTUFF_ID = 2;


    Byte getContentCode();

    /**
     * 获取内容格式
     *
     * @return
     */
    List<String> getContentType();

    /**
     * 构建序列化器
     *
     * @return
     */
    Serializer getSerializer();

}
