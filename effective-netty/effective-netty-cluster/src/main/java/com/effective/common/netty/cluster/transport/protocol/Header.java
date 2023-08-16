package com.effective.common.netty.cluster.transport.protocol;

import java.io.Serializable;


public interface Header extends Cloneable, Serializable {

    /**
     * 获取数据长度
     *
     * @return
     */
    default Integer getLength() {
        return null;
    }

    /**
     * 设置数据包长度，去掉魔法头
     *
     * @param length
     */
    default void setLength(Integer length) {
    }

    /**
     * 获取数据头长度
     *
     * @return
     */
    Short getHeaderLength();

    /**
     * 设置消息头长度
     *
     * @param headerLength
     */
    default void setHeaderLength(Short headerLength) {
    }

}
