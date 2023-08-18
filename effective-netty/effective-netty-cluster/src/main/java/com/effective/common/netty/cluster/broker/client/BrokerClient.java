package com.effective.common.netty.cluster.broker.client;


import com.effective.common.netty.cluster.command.api.Command;
import java.util.concurrent.CompletableFuture;

/**
 * 客户端接口
 */
public interface BrokerClient  {

    /**
     * 创建链接
     * @throws InterruptedException
     */
    void connect() throws InterruptedException;

    /**
     * 断开链接
     */
    void disconnect();

    /**
     * 运行状态
     * @return true/false
     */
    boolean isRunning();

    /**
     * 发布命令
     *
     * @param command 命令
     * @return 命令执行状态
     */
    CompletableFuture<Boolean> publish(Command command);


}
