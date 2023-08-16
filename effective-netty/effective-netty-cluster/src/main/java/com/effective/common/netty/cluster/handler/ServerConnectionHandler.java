package com.effective.common.netty.cluster.handler;


import com.effective.common.netty.cluster.utils.JSONUtil;
import com.effective.common.netty.cluster.utils.io.ChannelUtil;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.concurrent.EventExecutor;
import io.netty.util.concurrent.SingleThreadEventExecutor;
import lombok.extern.slf4j.Slf4j;

/**
 * Server Connection Handler
 *
 * @date 2020/2/27
 */
@Slf4j
@ChannelHandler.Sharable
public class ServerConnectionHandler extends ChannelDuplexHandler {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
         String address = ChannelUtil.getRemoteAddr(ctx.channel());
         if (log.isInfoEnabled()) {
             log.info("[Server]远程Broker组网成功！ClientAddress={}", address);
         }
        super.channelActive(ctx);
        ctx.channel().config().setWriteBufferHighWaterMark(10 * 1024 * 1024);
    }

    /**
     * 监控任务队列堆积任务数，任务队列中的任务包括io读写任务，业务程序提交任务
     */
    public void monitorPendingTaskCount(ChannelHandlerContext ctx) {
        int totalPendingSize = 0;
        for (EventExecutor eventExecutor : ctx.executor().parent()) {
            SingleThreadEventExecutor executor = (SingleThreadEventExecutor) eventExecutor;
            // 注意，Netty4.1.29以下版本本pendingTasks()方法存在bug，导致线程阻塞问题
            // 参考 https://github.com/netty/netty/issues/8196
            totalPendingSize += executor.pendingTasks();
        }
        if (log.isInfoEnabled()) {
            log.info("任务队列中总任务数 = " + totalPendingSize);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        String address = ChannelUtil.getRemoteAddr(ctx.channel());
         if (log.isInfoEnabled()) {
             log.info("[Server]BrokerClient连接已断开！ClientAddress={}", address);
         }
        super.channelInactive(ctx);
    }

    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
         if (log.isInfoEnabled()) {
             log.info("[Server]UserEventTriggered！event type={}", JSONUtil.bean2Json(evt));
         }
         super.userEventTriggered(ctx, evt);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
         String address = ChannelUtil.getRemoteAddr(ctx.channel());
         if (log.isInfoEnabled()) {
             log.error("[Server]exceptionCaught！address={}, message={}", address, cause.getMessage(), cause);
         }
         super.exceptionCaught(ctx, cause);
    }

    /**
     * 监控各个堆积的任务队列中第一个任务的类信息
     */
//    public void monitorQueueFirstTask(ChannelHandlerContext ctx) throws NoSuchFieldException, IllegalAccessException {
//        Field singleThreadField = SingleThreadEventExecutor.class.getDeclaredField("taskQueue");
//        singleThreadField.setAccessible(true);
//        for (EventExecutor eventExecutor : ctx.executor().parent()) {
//            SingleThreadEventExecutor executor = (SingleThreadEventExecutor) eventExecutor;
//            Runnable task = ((Queue<Runnable>) singleThreadField.get(executor)).peek();
//            if (null != task) {
//                if (log.isInfoEnabled()) {
//                    log.info("任务队列中第一个任务信息：" + task.getClass().getName());
//                }
//            }
//        }
//    }

    /**
     * 监控出站消息的队列积压的byteBuf大小
     */
//    public void monitorOutboundBufSize(ChannelHandlerContext ctx) {
//        long outBoundBufSize = ctx.channel().unsafe().outboundBuffer().totalPendingWriteBytes();
//        if (log.isInfoEnabled()) {
//            log.info("出站消息队列中积压的buf大小：" + outBoundBufSize);
//        }
//    }
}
