package com.cp.game;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;


/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 下午12:10:33  
* 	类说明  
*/ 
public abstract class ServerMainHandler implements GameHandler {
	public Logger log = Logger.getLogger(this.getClass());


	public abstract void execute(GameRequest request, GameResponse response,
			Map<String, Object> model) throws GameRunTimeException;

	public void execute(GameRequest request, GameResponse response)
			throws GameRunTimeException {
		Map<String, Object> model = new HashMap<String, Object>();
		execute(request, response, model);
	}
}
