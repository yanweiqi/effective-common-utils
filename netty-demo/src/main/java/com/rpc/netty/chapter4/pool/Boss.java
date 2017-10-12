package com.rpc.netty.chapter4.pool;

import java.nio.channels.ServerSocketChannel;
/**
 * boss接口
 * @author -琴兽-
 *
 */
public interface Boss {
	
	/**
	 * 加入一个新的ServerSocket
	 * @param serverChannel
	 */
	void registerAcceptChannelTask(ServerSocketChannel serverChannel);
}
