package com.effective.common.netty.cluster.broker.server;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;


@Slf4j
public abstract class AbstractBroker implements Closeable {

    public AbstractBroker() {
    }

    public AbstractBroker(int semaphore) {
    }

    @Override
    public void close() throws IOException {
        //this.stop();
    }
}
