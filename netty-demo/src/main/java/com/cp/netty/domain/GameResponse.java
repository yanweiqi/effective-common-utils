package com.cp.netty.domain;

import org.jboss.netty.channel.Channel;

import com.cp.utils.StreamUtils;


public class GameResponse implements IGameResponse {
	
	GameRequest gameRequest;

	private int playerId;
	
	/**
	 * 自己的channel
	 */
	private Channel channel;
	
	private int commondId;
	
	private int commandType;
	
	private long time;
	
	private StreamUtils rtObj=new StreamUtils();
	
	public GameResponse(GameRequest gameRequest){
		this.gameRequest=gameRequest;
		this.playerId=gameRequest.getCommand().getPlayerId();
		this.commondId=gameRequest.getCommand().getId();
		this.commandType=gameRequest.getCommand().getCommandType();
		this.time=gameRequest.getCommand().getTime();
		this.channel=gameRequest.getChannel();
		
	}

	public GameRequest getGameRequest() {
		return gameRequest;
	}

	public void setGameRequest(GameRequest gameRequest) {
		this.gameRequest = gameRequest;
	}



	public void clear() {
	}
	
	public void setMessageId(int messageId) {
		this.commondId = messageId;
	}


	public StreamUtils getRtObj() {
		return rtObj;
	}

	public void setRtObj(StreamUtils rtObj) {
		this.rtObj = rtObj;
	}
	
	public void put(byte[] data) {
		rtObj.write(data);
	}

	public void put(byte data) {
		rtObj.write(data);
	}

	public void putFloat(float data) {
		rtObj.writeFloat(data);
	}

	public void putInt(int data) {
		rtObj.writeInt(data);
	}

	public void putLong(long data) {
		rtObj.writeLong(data);
	}

	public void putShort(short data) {
		rtObj.writeShort(data);
	}

	public void putString(String data) {
		if(data == null)
			data = "";
		rtObj.writeObject(data);
	}

	public byte[] getRtMessage() {
		return rtObj.getBytesM();
	}

	public int getCommandType() {
		return commandType;
	}

	public void setCommandType(int commandType) {
		this.commandType = commandType;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public int getPlayerId() {
		return playerId;
	}

	public void setPlayerId(int playerId) {
		this.playerId = playerId;
	}

	

	public Channel getChannel() {
		return channel;
	}

	public void setChannel(Channel channel) {
		this.channel = channel;
	}

	public int getCommondId() {
		return commondId;
	}

	public void setCommondId(int commondId) {
		this.commondId = commondId;
	}


}
