package com.cp.game.handler.common;

import java.util.Map;

import com.cp.game.GameRunTimeException;
import com.cp.game.ServerMainHandler;
import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;

public class InitHandler extends ServerMainHandler {
	@Override
	public void execute(GameRequest request, GameResponse response,
			Map<String, Object> model) throws GameRunTimeException {
		
		log.error("client sendMessage:"+request.readLong());
		//返回协议的协议体数据
		response.putLong(System.currentTimeMillis());
	}
}
