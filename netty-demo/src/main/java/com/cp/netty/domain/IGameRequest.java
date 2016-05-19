package com.cp.netty.domain;

/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:26:47  
* 	类说明  
*/ 
public interface IGameRequest {
	
	int getCommandId();

	String readString();

	byte read();

	int readInt();
	
	int readUnsignedInt();

	short readShort();
	
	long readLong();
	
	float readFloat();
}
