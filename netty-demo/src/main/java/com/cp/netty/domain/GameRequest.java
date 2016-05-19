package com.cp.netty.domain;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import org.jboss.netty.channel.Channel;

/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:36:31  
* 	类说明 游戏请求封装 
*/ 
public class GameRequest implements IGameRequest {
	private Command command;
	private Channel channel;
	
	public GameRequest(Channel channel,byte[] data) {
		InputStream inCommandData = new ByteArrayInputStream(data);
		try {
			this.channel=channel;
			this.command=new Command(inCommandData);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public GameRequest(Channel channel,Command command) {
		this.channel=channel;
		this.command=command;
	}
	
	public GameRequest(Command command) {
		this.channel=null;
		this.command=command;
	}
	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public void setCommand(Command command) {
		this.command = command;
	}


	public int getCommandId() {
		if (command != null) {
			return command.getId();
		} else {
			return -1;
		}
	}

	/**
	 * @return the command
	 */
	public Command getCommand() {
		return command;
	}

	public String readString() {
		return command.readString();
	}

	public byte read() {
		return command.readByte();
	}

	public short readShort() {
		return command.readShort();
	}
	
	public long readLong() {
		return command.readLong();
	}

	public int readInt() {
		return command.readInt();
	}
	
	public int readUnsignedInt() {
		int rt=command.readInt();
		return rt<0?0:rt;
		
	}
	
	public float readFloat(){
		return command.readFloat();
	}


}
