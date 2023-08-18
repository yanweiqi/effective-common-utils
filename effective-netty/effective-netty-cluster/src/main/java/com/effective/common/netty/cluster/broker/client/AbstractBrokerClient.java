package com.effective.common.netty.cluster.broker.client;

import com.effective.common.netty.cluster.command.api.Command;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Data
public abstract class AbstractBrokerClient implements BrokerClient, Closeable {

    /**
     * io通道
     */
    private Channel channel;

    /**
     * 机器id
     */
    private String brokerId;

    /**
     * 断开重连
     */
    private boolean automaticReconnect = true;

    /**
     * 运行状态
     */
    protected volatile boolean running = false;

    @Override
    public CompletableFuture<Boolean> publish(Command command) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        if (Objects.isNull(channel)) {
            log.error("【客户端】通道IO为空，不可通信 BrokerId:{}, command:{}", brokerId, JSONUtil.bean2Json(command));
            completableFuture.complete(false);
            return completableFuture;
        }
        if (this.channel.isWritable()) {
            this.channel.writeAndFlush(command).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (channelFuture.isSuccess()) {
                        if (log.isInfoEnabled()) {
                            log.info("【客户端】同步命令完成 brokerId:{}, result:{}, done:{}, command:{}", brokerId, channelFuture.isSuccess(), channelFuture.isDone(), JSONUtil.bean2Json(command));
                        }
                        completableFuture.complete(true);
                    }
                }
            });
        } else {
            try {
                log.error("【客户端】同步命令失败，通道不可写,brokerId={}, command={}",brokerId,JSONUtil.bean2Json(command));
                this.channel.writeAndFlush(command).sync();
            } catch (Exception e) {
                log.error("【客户端】同步命令失败，通道不可写,brokerId={}, command={}",brokerId,JSONUtil.bean2Json(command));
                completableFuture.complete(false);
            }
        }
        return completableFuture;
    }

    @Override
    public void close() throws IOException {
        disconnect();
    }
}
