package com.effective.common.netty.cluster.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

/**
 * Server Idle Check Handler
 *
 * @date 13/9/2020
 */
@Slf4j
public class ServerIdleCheckHandler extends IdleStateHandler {

    /**
     * 服务端心跳检测
     */
    public ServerIdleCheckHandler() {
        /**
         * 读超时
         * 写超时
         * 全部超时
         * 单位(秒)
         */
        super(0, 0, 10, TimeUnit.SECONDS);
    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        super.channelIdle(ctx, evt);
    }
}
