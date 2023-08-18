package com.effective.common.netty.cluster.channel.server;

import com.effective.common.netty.cluster.constants.NettyAttrManager;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ServerConnectionHandler extends ChannelDuplexHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
         String address = NettyAttrManager.getRemoteAdd(ctx.channel());
         if (log.isInfoEnabled()) {
             log.info("【服务端】Broker组网成功，远程地址:{}", address);
         }
        super.channelActive(ctx);
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         super.exceptionCaught(ctx, cause);
    }
}
