package com.effective.common.netty.cluster.broker.client;


import com.effective.common.netty.cluster.command.Command;

import java.util.concurrent.CompletableFuture;


public interface BrokerClient {

    void connect() throws InterruptedException;

    void disconnect();

    boolean isRunning();

    CompletableFuture<Boolean> publish(Command command);
}
