package com.rpc.netty.chapter4;

import com.rpc.netty.chapter4.pool.NioSelectorRunnablePool;

import java.net.InetSocketAddress;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 启动函数
 *
 */
public class Start {

	private static ExecutorService boss = Executors.newCachedThreadPool();

	private static ExecutorService worker  = Executors.newCachedThreadPool();


	public static void main(String[] args) {

		//初始化线程
		NioSelectorRunnablePool nioSelectorRunnablePool = new NioSelectorRunnablePool(boss,worker);
		
		//获取服务类
		ServerBootstrap bootstrap = new ServerBootstrap(nioSelectorRunnablePool);
		
		//绑定端口
		bootstrap.bind(new InetSocketAddress(10101));
		
		System.out.println("start");
	}

}
