package com.effective.common.netty.cluster.handler;

import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.command.domain.Pong;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
@Slf4j
@Data
@EqualsAndHashCode(callSuper=false)
public class PongCommandHandler extends AbstractCommandHandler<Pong> {
public class PongCommandHandler extends AbstractCommandHandler<Pong> {

    @Override
    public void handle(Pong pong) {
        log.info("【客户端】command:{},brokerId:{},clientId:{}",pong.getCommandName(),pong.getBrokerId(),pong.getClientId());
    }

    @Override
    public boolean supportsType(Class<? extends Command> pong) {
        return Pong.class.isAssignableFrom(pong);
    }

    @Override
    public List<String> getCommands() {
        return Stream.of(Pong.COMMAND_NAME).collect(Collectors.toList());
    }
}
