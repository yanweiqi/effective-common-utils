package com.effective.common.netty.cluster.broker.client;

import com.effective.common.netty.cluster.command.Command;
import com.effective.common.netty.cluster.utils.JSONUtil;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;


@Slf4j
@Data
public abstract class AbstractBrokerClient implements BrokerClient {

    private Channel channel;

    private String brokerId;

    private boolean automaticReconnect = true;

    protected volatile boolean running = false;

    @Override
    public CompletableFuture<Boolean> publish(Command command) {
        CompletableFuture<Boolean> completableFuture = new CompletableFuture<>();
        if (Objects.isNull(channel)) {
            log.error("Message sync failure, channel is null! brokerId={}, command={}", brokerId, JSONUtil.bean2Json(command));
            completableFuture.complete(false);
            return completableFuture;
        }
        //判断当前是否可写，水位问题
        if (this.channel.isWritable()) {
            this.channel.writeAndFlush(command).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    if (log.isDebugEnabled()) {
                        log.debug("Message sync end! brokerId={}, result={}, done={}, command={}", brokerId, channelFuture.isSuccess(), channelFuture.isDone(), JSONUtil.bean2Json(command));
                    }
                    if (channelFuture.isSuccess()) {
                        completableFuture.complete(true);
                    }
                }
            });
        } else {
            try {
                if (log.isErrorEnabled()) {
                    log.error("[!!!Publishing!!!]The current channel state is unwritable, send in sync!");
                }
                this.channel.writeAndFlush(command).sync();
            } catch (Exception e) {
                log.error("Message sync failure, channel is null or unwritable! brokerId={}, command={}", brokerId, JSONUtil.bean2Json(command));
                completableFuture.complete(false);
            }
        }
        return completableFuture;
    }
}
