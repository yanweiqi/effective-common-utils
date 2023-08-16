package com.effective.common.netty.cluster.feeback;


import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * 异步确认，回调接口
 */
public interface Feedback<T> extends Cloneable {

    default ChannelHandlerContext getChannelHandlerContext() {
        return null;
    }

    default void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext) {

    }

    /**
     * Acknowledge message 告知收到消息
     *
     * @param t mqttMessage传输对象
     */
    void acknowledge(T t);

    /**
     * Negative acknowledge message（离线消息通知）
     *
     * @param t 消息体
     */
    void negativeAcknowledge(T t);

    /**
     * Default feedback
     */
    @Slf4j
    class NullFeedback implements Feedback<String> {
        @Override
        public void acknowledge(String o) {
            log.info("Acknowledge but do nothing!");
        }

        @Override
        public void negativeAcknowledge(String o) {
            log.info("Negative acknowledge but do nothing!");
        }

    }
}
