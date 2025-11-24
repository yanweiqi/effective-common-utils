package com.effective.common.netty.cluster.channel.common;


import com.effective.common.netty.cluster.command.api.AbstractCommand;
import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.command.factory.CommandFactory;
import com.effective.common.netty.cluster.handler.CommandHandler;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;

@Slf4j
@ChannelHandler.Sharable
public class FactoryCommandHandler extends SimpleChannelInboundHandler<AbstractCommand<Command>> {
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, AbstractCommand<Command> command) throws Exception {
        try {
            if (log.isInfoEnabled()) {
                log.info("【服务端】接收命令, commandName:{},Command:{}", command.getCommandName(), JSONUtil.bean2Json(command));
            }
            CommandFactory commandFactory = CommandFactory.match(command.getCommandName());
            CommandHandler<Command> commandHandler = commandFactory.getHandler();
            if (Objects.nonNull(commandHandler)) {
                command.setChannelHandlerContext(channelHandlerContext);
                commandHandler.execute(command);
//                CommandExecutor executor = new CommandExecutor(commandHandler, command);
////                if (commandHandler.asyncRunning()) {
////                    ((ExecutorsProvider) commandHandler).getExecutorService().submit(executor);
////                } else {
////                    executor.run();
////                }
            } else {
                log.error("【服务端】无法处理命令 commandName={}, command={}", command.getCommandName(), JSONUtil.bean2Json(command));
            }
        } catch (Exception e) {
            log.error("【服务端】接收命令异常 message={}", e.getMessage(), e);
        }
    }
}
