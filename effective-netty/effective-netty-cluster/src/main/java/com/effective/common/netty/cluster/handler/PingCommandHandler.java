package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.command.domain.Ping;
import com.effective.common.netty.cluster.command.domain.Pong;
import com.effective.common.netty.cluster.utils.IpUtil;
import com.effective.common.netty.cluster.utils.ObjectUtils;
import io.netty.channel.ChannelHandlerContext;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Slf4j
@Data
public class PingCommandHandler extends AbstractCommandHandler<Ping> {

    @Override
    public void handle(Ping ping) {
        Pong pong = new Pong();
        pong.setBrokerId(IpUtil.getLocalIp());
        pong.setCommandBody(1);
        log.info("【服务端】PingCommandHandler 回复 {}","pong");
        ChannelHandlerContext ctx = ping.getChannelHandlerContext();
        ctx.writeAndFlush(pong);
    }

    @Override
    public boolean supportsType(Class<? extends Command> ping) {
        return Ping.class.isAssignableFrom(ping);
    }

    @Override
    public List<String> getCommands() {
        return Stream.of(Ping.COMMAND_NAME).collect(Collectors.toList());
    }
}
