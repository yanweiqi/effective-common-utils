package com.effective.common.netty.cluster.transport.serialization;

import java.util.List;

/**
 * Serialization
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

    /**
     * 获取code
     *
     * @return code
     */
    Byte getContentCode();

    /**
     * 获取内容格式
     *
     * @return 类型
     */
    List<String> getContentType();

    /**
     * 构建序列化器
     *
     * @return 序列化接口
     */
    Serializer getSerializer();

}
