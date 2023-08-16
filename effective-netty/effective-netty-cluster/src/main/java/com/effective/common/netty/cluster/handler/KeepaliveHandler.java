package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class KeepaliveHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("[Client]UserEventTriggered！event type={}", JSONUtil.bean2Json(evt));
        }
        super.userEventTriggered(ctx, evt);
    }
}
