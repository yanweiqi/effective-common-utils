package com.effective.common.netty.cluster.handler;

import java.util.concurrent.ExecutorService;

/**
 * Executors provider interface
 *
 *
 */
public interface ExecutorsProvider {

    /**
     * Get executor service pool
     *
     * @return {@link ExecutorService}
     */
    ExecutorService getExecutorService();
}
