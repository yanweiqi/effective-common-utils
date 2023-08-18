package com.effective.common.netty.cluster.channel.server;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ChannelHandler.Sharable
public class ServerKeepaliveHandler extends ChannelDuplexHandler {

    @Override
    public void userEventTriggered(ChannelHandlerContext handlerContext,Object evt) throws Exception{
        if(evt instanceof IdleStateEvent){
            IdleStateEvent event = (IdleStateEvent) evt;
            switch (event.state()){
                case READER_IDLE:
                    log.info("【服务端】{},{}",handlerContext.channel().remoteAddress(),"读空闲");
                    break;
                case WRITER_IDLE:
                    log.info("【服务端】{},{}",handlerContext.channel().remoteAddress(),"写空闲");
                    break;
                case ALL_IDLE:
                    log.info("【服务端】{},{}",handlerContext.channel().remoteAddress(),"读写空闲");
                    //handlerContext.channel().close(); todo 是否需要关闭
                    break;
            }
        } else {
            super.userEventTriggered(handlerContext, evt);
        }
    }
}
