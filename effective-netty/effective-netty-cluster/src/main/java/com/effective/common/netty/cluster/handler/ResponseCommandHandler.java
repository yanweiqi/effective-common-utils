package com.effective.common.netty.cluster.handler;


import com.effective.common.netty.cluster.command.CommandExecutor;
import com.effective.common.netty.cluster.command.CommandFactory;
import com.effective.common.netty.cluster.command.CommandHandler;
import com.effective.common.netty.cluster.command.ResponseCommand;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;


@Slf4j
@ChannelHandler.Sharable
public class ResponseCommandHandler extends SimpleChannelInboundHandler<ResponseCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                ResponseCommand responseCommand) throws Exception {
        if (log.isInfoEnabled()) {
            log.info("[###ResponseCommandHandler###]Received a command = {}", JSONUtil.bean2Json(responseCommand));
        }
        CommandHandler commandHandler = CommandFactory.getHandler(responseCommand.getCommandName());
        if (Objects.nonNull(commandHandler)) {
            CommandExecutor executor = new CommandExecutor(commandHandler, responseCommand);
            if (commandHandler.asyncRunning()) {
                ((ExecutorsProvider) commandHandler).getExecutorService().submit(executor);
            } else {
                executor.execute();
            }
        } else {
            log.error("[###ResponseCommandHandler###]Can't find handler to hand this command! commandName={}, command={}",
                    responseCommand.getCommandName(),
                    JSONUtil.bean2Json(responseCommand)
            );
        }
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext handlerContext,Object evt) throws Exception{
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            String eventType = null;
            switch (event.state()){
                case READER_IDLE:
                    eventType = "读空闲";
                    break;
                case WRITER_IDLE:
                    eventType = "写空闲";
                    break;
                case ALL_IDLE:
                    eventType = "读写空间";
                    handlerContext.channel().close();
                    break;
            }
            log.info("服务端 {},{}",handlerContext.channel().remoteAddress(),eventType);
        }
    }
}
