package com.cp.netty.domain;


/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:26:59  
* 	类说明  
*/ 
public interface IGameResponse {
	
	void put(byte data);

	void putShort(short length);
	
	void putInt(int data);
	
	void putFloat(float data);

	void putLong(long data);

	void putString(String data);

	void clear();
}
