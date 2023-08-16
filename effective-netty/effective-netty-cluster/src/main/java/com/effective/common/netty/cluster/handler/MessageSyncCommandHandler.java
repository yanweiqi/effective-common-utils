package com.effective.common.netty.cluster.handler;


import com.effective.common.netty.cluster.command.CommandExecutor;
import com.effective.common.netty.cluster.command.CommandFactory;
import com.effective.common.netty.cluster.command.CommandHandler;
import com.effective.common.netty.cluster.command.PublishMessageCommand;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;
import java.util.Objects;

/**
 * Message sync command handler for netty
 *
 * @date 26/1/2020
 */
@Slf4j
@ChannelHandler.Sharable
public class MessageSyncCommandHandler extends SimpleChannelInboundHandler<PublishMessageCommand> {

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, PublishMessageCommand publishMessageCommand) {
        if (log.isInfoEnabled()) {
            log.info("[MessageSyncCommandHandler]Received a command from other brokers, command={}", JSONUtil.bean2Json(publishMessageCommand));
        }
        try {
            CommandHandler commandHandler = CommandFactory.getHandler(publishMessageCommand.getCommandName());
            if (Objects.nonNull(commandHandler)) {
                publishMessageCommand.setChannelHandlerContext(channelHandlerContext);
                CommandExecutor executor = new CommandExecutor(commandHandler, publishMessageCommand);
                if (commandHandler.asyncRunning()) {
                    ((ExecutorsProvider) commandHandler).getExecutorService().submit(executor);
                } else {
                    executor.execute();
                }
            } else {
                log.error("[###MessageSyncCommandHandler###]Can't find handler to hand this command! commandName={}, command={}",
                        publishMessageCommand.getCommandName(), JSONUtil.bean2Json(publishMessageCommand));
            }
        } catch (Exception e) {
            log.error("[MessageSyncCommandHandler]Handler publishMessage command error! message={}", e.getMessage(), e);
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
            log.info("关闭远程客户端 {},{}",handlerContext.channel().remoteAddress(),eventType);
        }
    }
}
