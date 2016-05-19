package com.cp.test;

import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;

import com.cp.netty.coder.Decoder;
import com.cp.netty.coder.Encoder;


/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 下午12:25:03  
* 	类说明  
*/ 
public class ClientPipelineFactory implements ChannelPipelineFactory {

	public ChannelPipeline getPipeline() throws Exception {
		ChannelPipeline pipeline = Channels.pipeline();
		pipeline.addLast("decoder", new Decoder(Integer.MAX_VALUE, 0, 4));
		pipeline.addLast("encoder", new Encoder(4));
        pipeline.addLast("handler", new ClientHandler());

        return pipeline;

	}

}
