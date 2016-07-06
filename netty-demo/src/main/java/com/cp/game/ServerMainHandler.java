package com.cp.game;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;


/** 
 * 	类说明  
 */ 

@Service
public abstract class ServerMainHandler implements GameHandler {
	public Logger log = Logger.getLogger(this.getClass());


	protected abstract void execute(GameRequest request, GameResponse response,Map<String, Object> model) throws GameRunTimeException;

	public void execute(GameRequest request, GameResponse response)	throws GameRunTimeException {
		Map<String, Object> model = new HashMap<String, Object>();
		execute(request, response, model);
	}
}
