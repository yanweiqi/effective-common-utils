package com.cp.netty.domain;

import java.io.IOException;
import java.io.InputStream;

import com.cp.utils.StreamUtils;



/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:25:02  
* 	协议体信息 
*/ 
public class Command {
	
	private int id;
		
	private int playerId=-1;

	/**
	 * 协议请求时间戳
	 */
	private long time;
	
	/**
	 * 0  私有协议 1 广播协议
	 */
	private int commandType=0;

	private InputStream inCommandData;
	
	public  Command(InputStream inCommandData) {
		this.inCommandData =inCommandData;
		try {
			
			this.id=StreamUtils.readInt(inCommandData);
			this.playerId=StreamUtils.readInt(inCommandData);
			this.commandType=StreamUtils.readInt(inCommandData);
			this.time=StreamUtils.readLong(inCommandData);
		} catch (IOException e) {
			this.id = -1;
			this.playerId = -1;
			e.printStackTrace();
		}
	}
	
	
	public  Command(int commandId) {
		this.id=commandId;
		this.playerId = -1;
	}
	
	public  Command(int commandId,int cityId,int playerId) {
		this.id=commandId;
		this.playerId = playerId;
	}
	
	public int readInt() {
		try {
			return StreamUtils.readInt(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public short readShort() {
		try {
			return StreamUtils.readShort(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	public long readLong() {
		try {
			return StreamUtils.readLong(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public byte readByte() {
		try {
			return StreamUtils.readByte(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return -1;
		}
	}

	public String readString() {
		try {
			return StreamUtils.readString(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	
	public float readFloat() {
		try {
			return StreamUtils.readFloat(inCommandData);
		} catch (IOException e) {
			e.printStackTrace();
			return 0;
		}
	}
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}


	public InputStream getInCommandData() {
		return inCommandData;
	}

	public void setInCommandData(InputStream inCommandData) {
		this.inCommandData = inCommandData;
	}


	public int getPlayerId() {
		return playerId;
	}


	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	public long getTime() {
		return time;
	}


	public void setTime(long time) {
		this.time = time;
	}


	public int getCommandType() {
		return commandType;
	}


	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}
}
