package com.effective.common.netty.cluster.channel.server;

import com.effective.common.netty.cluster.utils.ObjectUtils;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class LifeCycleHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("逻辑处理器被添加：handlerAdded()"));
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 绑定到线程(NioEventLoop)：channelRegistered()"));
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 准备就绪：channelActive()"));
        super.channelActive(ctx);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 有数据可读：channelRead()"));
        super.channelRead(ctx, msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 某次数据读完：channelReadComplete()"));
        super.channelReadComplete(ctx);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 被关闭：channelInactive()"));
        super.channelInactive(ctx);
    }

    @Override
    public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 取消线程(NioEventLoop) 的绑定: channelUnregistered()"));
        super.channelUnregistered(ctx);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("逻辑处理器被移除：handlerRemoved()"));
        super.handlerRemoved(ctx);
    }

}
