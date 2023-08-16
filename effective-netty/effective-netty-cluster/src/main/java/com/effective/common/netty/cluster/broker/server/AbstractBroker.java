package com.effective.common.netty.cluster.broker.server;

import lombok.extern.slf4j.Slf4j;

import java.io.Closeable;
import java.io.IOException;
import java.util.concurrent.Semaphore;


@Slf4j
public abstract class AbstractBroker implements Closeable {
    /**
     * Semaphore to limit maximum number of on-going asynchronous requests, which protects system memory footprint.
     */
    private Semaphore semaphore;

    public AbstractBroker() {
        this.semaphore = new Semaphore(65536, true);
    }

    public AbstractBroker(int semaphore) {
        this.semaphore = new Semaphore(semaphore, true);
    }

    @Override
    public void close() throws IOException {
        //this.stop();
    }
}
