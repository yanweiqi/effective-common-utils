package com.cp.game;

import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;


/** 
* 	作者:chenpeng  
*	E-mail:46731706@qq.com  
* 	创建时间：2012-7-12 上午11:31:48  
* 	类说明  
*/ 
public interface GameHandler {
    void execute(GameRequest request, GameResponse response) throws GameRunTimeException;
}
