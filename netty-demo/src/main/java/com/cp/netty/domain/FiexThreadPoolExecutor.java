package com.cp.netty.domain;

import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class FiexThreadPoolExecutor extends ThreadPoolExecutor {
	
	public FiexThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveSecond, String poolName) {
		super(corePoolSize, maximumPoolSize, keepAliveSecond, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>(), Executors.defaultThreadFactory());
	}
	
	public FiexThreadPoolExecutor(int corePoolSize, int maximumPoolSize,
			long keepAliveSecond) {
		super(corePoolSize, maximumPoolSize, keepAliveSecond, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
	}

	
}
