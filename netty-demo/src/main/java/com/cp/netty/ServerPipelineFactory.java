 package com.cp.netty;


import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.cp.netty.coder.Decoder;
import com.cp.netty.coder.Encoder;

/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:28:56  
* 	channelPipeline是一系列channelHandler的集合，他参照J2ee中的Intercepting Filter模式来实现的，
* 	让用户完全掌握如果在一个handler中处理事件，同时让pipeline里面的多个handler可以相互交互 
*/ 
public class ServerPipelineFactory implements ChannelPipelineFactory {
	public ServerHandler serverHandler;

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeLine = Channels.pipeline();
		pipeLine.addLast("decoder", new Decoder(Integer.MAX_VALUE, 0, 4));
		pipeLine.addLast("encoder", new Encoder(4));
		pipeLine.addLast("handler", serverHandler);
		return pipeLine;
	}

	public ServerHandler getServerHandler() {
		return serverHandler;
	}

	public void setServerHandler(ServerHandler serverHandler) {
		this.serverHandler = serverHandler;
	}

}
