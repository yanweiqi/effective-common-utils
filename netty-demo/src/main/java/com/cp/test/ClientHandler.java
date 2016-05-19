package com.cp.test;



import com.cp.netty.domain.*;
import com.cp.utils.StreamUtils;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.*;

import java.io.*;
import java.util.logging.*;

/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 下午12:27:04  
* 	类说明  
*/ 
public class ClientHandler extends SimpleChannelUpstreamHandler {
	private static final Logger logger = Logger.getLogger(ClientHandler.class.getName());
 
 
    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    	Channel c = e.getChannel();
    	StreamUtils s = new StreamUtils();
    	//协议号1000
    	s.writeInt(1000);
    	//玩家ID 1
    	s.writeInt(1);
    	//协议类型 
    	s.writeInt(1);
    	//协议生成时间
    	s.writeLong(System.currentTimeMillis());

    	GameRequest gameRequest=new GameRequest(c,s.getBytesM());
    	GameResponse g=new GameResponse(gameRequest);
    	//协议体
    	g.putLong(1L);
    	c.write(g);
    
    }
 
    @Override
    public void messageReceived(
            ChannelHandlerContext ctx, MessageEvent e) {
    	
    	ChannelBuffer buffs=(ChannelBuffer)e.getMessage();
		buffs.skipBytes(4);// 越过dataLength的字节
		byte[] decoded = new byte[buffs.readableBytes()];
		buffs.readBytes(decoded);
		InputStream in = new ByteArrayInputStream(decoded);
		try {
			int commandId=StreamUtils.readInt(in);
			int playerId=StreamUtils.readInt(in);
			int commandType=StreamUtils.readInt(in);
			long f=StreamUtils.readLong(in);
			
			long serverTime=StreamUtils.readLong(in);
			System.out.println("client received message:"+commandId+"\t"+playerId+"\t"+commandType+"\t"+f+"\t"+serverTime);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
    	
    }
 
    @Override
    public void exceptionCaught(
            ChannelHandlerContext ctx, ExceptionEvent e) {
        // Close the connection when an exception is raised.
        logger.log(
                Level.WARNING,
                "Unexpected exception from downstream.",
                e.getCause());
        e.getChannel().close();
    }

}
