package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.utils.io.ChannelUtil;
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

    public ClientConnectionHandler() {
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        String address = ChannelUtil.getRemoteAddr(ctx.channel());
        log.info("[Client]与远程Broker组网成功！远程Broker地址：{}", address);
        super.channelActive(ctx);
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String address = ChannelUtil.getRemoteAddr(ctx.channel());
        log.error("[Client]远程Broker连接断开，远程Broker地址：{}", address);
        super.channelInactive(ctx);
        if(null != closeRunnable)  closeRunnable.run();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        String address = ChannelUtil.getRemoteAddr(ctx.channel());
        log.error("[Client]连接出现异常，远程Broker地址：{}, message={}", address, cause.getMessage(), cause);
        super.exceptionCaught(ctx, cause);
        if(null != closeRunnable)  closeRunnable.run();
    }
}
