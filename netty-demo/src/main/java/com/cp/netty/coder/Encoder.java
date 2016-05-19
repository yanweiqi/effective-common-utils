package com.cp.netty.coder;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.LengthFieldPrepender;

import com.cp.netty.domain.GameResponse;

/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:43:11  
* 	类说明  
*/ 
public class Encoder extends LengthFieldPrepender {

	public Encoder(int lengthFieldLength) {
		super(lengthFieldLength);
	}

	@Override
	protected Object encode(ChannelHandlerContext cxt, Channel channel,
			Object msg) throws Exception {
		
		ChannelBuffer buffer = ChannelBuffers.dynamicBuffer(channel.getConfig().getBufferFactory());
		GameResponse response = (GameResponse) msg;
		buffer.writeInt(response.getRtMessage().length+20);
		buffer.writeInt(response.getCommondId());
		buffer.writeInt(response.getPlayerId());
		buffer.writeInt(response.getCommandType());
		buffer.writeLong(response.getTime());
		System.out.println("send message "+response.getCommondId());
		buffer.writeBytes(response.getRtObj().getBytesM());
		return buffer;

	}

}
