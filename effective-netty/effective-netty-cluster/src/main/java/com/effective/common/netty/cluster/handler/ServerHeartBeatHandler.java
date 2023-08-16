package com.effective.common.netty.cluster.handler;

//import com.effective.common.netty.cluster.utils.JSONUtil;
//import io.netty.channel.ChannelHandlerAdapter;
//import io.netty.channel.ChannelHandlerContext;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//
//@Slf4j
//public class ServerHeartBeatHandler extends ChannelHandlerAdapter {
//
//    private static final String successKey = "auth";
//
//    private boolean auth(ChannelHandlerContext ctx, Object obj) throws Exception {
//        String msg = (String) obj;
//        if (!StringUtils.isEmpty(msg)) {
//            ctx.writeAndFlush(successKey);
//        } else {
//            return false;
//        }
//        return true;
//    }
//
//    public void channelRead(ChannelHandlerContext ctx, Object obj) throws Exception {
//        if (obj instanceof String) {
//            auth(ctx,obj);
//        }
//        if(obj instanceof HeartInfo) {
//            HeartInfo info = (HeartInfo) obj;
//            log.info("Server收到心跳信息 {}", JSONUtil.bean2Json(info));
//            ctx.writeAndFlush("Server收到心跳信息:pong");
//        }
//    }
//}
