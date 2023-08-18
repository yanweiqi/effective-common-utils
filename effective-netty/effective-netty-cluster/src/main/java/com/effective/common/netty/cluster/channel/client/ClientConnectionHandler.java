package com.effective.common.netty.cluster.channel.client;

import com.effective.common.netty.cluster.constants.NettyAttrManager;
import com.effective.common.netty.cluster.utils.ObjectUtils;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

/**
 * ClientConnection Handler
 */
@Slf4j
public class ClientConnectionHandler extends ChannelDuplexHandler {

    private Runnable closeRunnable;

    public ClientConnectionHandler(Runnable closeRunnable) {
        this.closeRunnable = closeRunnable;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx, x->log.info("逻辑处理器被添加：handlerAdded()"));
        super.handlerAdded(ctx);
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        ObjectUtils.isTrue(log.isInfoEnabled(),ctx,x->log.info("channel 绑定到线程(NioEventLoop)：channelRegistered()"));
        super.channelRegistered(ctx);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String address = NettyAttrManager.getRemoteAdd(ctx.channel());
        log.info("【客户端】Broker组网成功，远程地址：{}", address);
        super.channelActive(ctx);
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
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
        String address = NettyAttrManager.getRemoteAdd(ctx.channel());
        log.error("【客户端】Broker组网断开，远程地址：{}", address);
        super.channelInactive(ctx);
        if(null != closeRunnable)  closeRunnable.run();
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

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String address = NettyAttrManager.getRemoteAdd(ctx.channel());
        log.error("【客户端】Broker组网异常，远程Broker地址：{}, message={}", address, cause.getMessage(), cause);
        super.exceptionCaught(ctx, cause);
        if(null != closeRunnable)  closeRunnable.run();
    }
}
