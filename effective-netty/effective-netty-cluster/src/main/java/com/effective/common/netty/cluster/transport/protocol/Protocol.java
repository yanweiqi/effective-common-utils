package com.effective.common.netty.cluster.transport.protocol;


public interface Protocol {

    /**
     * 获取协议的魔术字节数组
     *
     * @return
     */
    byte[] getMagicCode();
}
