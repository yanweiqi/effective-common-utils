package com.effective.common.netty.cluster.handler;


import com.effective.common.netty.cluster.command.BaseCommand;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BaseCommandHandler extends SimpleChannelInboundHandler<BaseCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, BaseCommand baseCommand) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("[BaseCommand]Received a command from other brokers, command={}", JSONUtil.bean2Json(baseCommand));
        }
    }
}
