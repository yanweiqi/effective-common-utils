package com.effective.common.netty.cluster.command.api;


import com.effective.common.netty.cluster.feeback.Feedback;
import com.effective.common.netty.cluster.transport.protocol.Header;
import io.netty.channel.ChannelHandlerContext;

/**
 *
 */
public interface Command {

    /**
     * 获取命令名称
     *
     * @return String
     */
    String getCommandName();

    /**
     * 设置回调
     *
     * @param feedback 回调接口
     */
    void setFeedback(Feedback<?> feedback);

    /**
     * 获取回调接口
     *
     * @return Feedback
     */
    Feedback<?> getFeedback();

    /**
     * 获取头部
     *
     * @return Header
     */
    Header getHeader();

    /**
     * 命令的内容
     *
     * @return Object
     */
    Object getCommandBody();

    /**
     * 获取通道上线文
     *
     * @return ChannelHandlerContext
     */
    ChannelHandlerContext getChannelHandlerContext();

    /**
     * 设置通道上下文
     *
     * @param channelHandlerContext 通道上线文
     */
    void setChannelHandlerContext(ChannelHandlerContext channelHandlerContext);

}
