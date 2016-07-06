package com.cp.game;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.cp.game.domain.MessageQueue;
import com.cp.netty.domain.GameRequest;
import com.cp.netty.domain.GameResponse;

/** 
 * 	游戏逻辑逻辑处理器  
 */ 
@Service("HandlerDispatcher")
public class HandlerDispatcher implements Runnable {

	private static final  Logger logger = Logger.getLogger(HandlerDispatcher.class);
	
	@Autowired
	private Executor messageExecutor;
	
	private Map<Integer, MessageQueue> sessionMsgQ;
	
	private Map<Integer, GameHandler> handlerMap;
	
	private volatile boolean isRunning;
	
	private long sleepTime;

	public void setHandlerMap(Map<Integer, GameHandler> handlerMap) {
		this.handlerMap = handlerMap;
	}

	public void init() {
		if (!isRunning) {
			isRunning = true;
			sessionMsgQ = new ConcurrentHashMap<Integer, MessageQueue>();
		}
	}

	public synchronized void stop() {
		isRunning = false;
	}

	public void run() {
		while (isRunning) {
			Set<Integer> keySet = sessionMsgQ.keySet();
			for (Integer key : keySet) {
				MessageQueue messageQueue = sessionMsgQ.get(key);
				if (messageQueue == null || messageQueue.size() <= 0 || messageQueue.isRunning())
					continue;
				MessageWorker messageWorker = new MessageWorker(messageQueue);
				messageExecutor.execute(messageWorker);
			}
			try {
				Thread.sleep(sleepTime);
			} catch (InterruptedException e) {
				logger.error("", e);
			}
		}
	}

	public void addMessageQueue(Integer  channelId, MessageQueue messageQueue) {
		sessionMsgQ.put(channelId, messageQueue);
	}

	/**
	 * 为当前session添加消息
	 * 
	 * @param session
	 * @param request
	 * @return
	 */
	public boolean addMessage(GameRequest request) {
		boolean added = false;
		int channelId= request.getChannel().getId();
		MessageQueue messageQueue = sessionMsgQ.get(channelId);
		if (messageQueue == null) {
			request.getChannel().close();
			logger.error("", new IllegalStateException());
		} else {
			added = messageQueue.add(request);
		}
		return added;
	}
	
	public boolean checkMessageQueue(String key){
		return sessionMsgQ.containsKey(key);
	}

	/**
	 * @param session
	 */
	public void removeMessageQueue(String key) {
		MessageQueue queue = sessionMsgQ.remove(key);
		if (queue != null) {
			queue.clear();
		}
	}

	/**
	 * 消息队列处理线程实现
	 * 
	 */
	private final class MessageWorker implements Runnable {
		private MessageQueue messageQueue;
		private GameRequest request;

		private MessageWorker(MessageQueue messageQueue) {
			messageQueue.setRunning(true);
			request = messageQueue.getRequestQueue().poll();
			this.messageQueue = messageQueue;
		}

		public void run() {
			try {
				handMessageQueue();
			} finally {
				messageQueue.setRunning(false);
			}
		}

		/**
		 * 处理消息队列
		 */
		private void handMessageQueue() {
			int messageId = request.getCommandId();

			GameHandler handler = handlerMap.get(messageId);
			if (handler != null) {
				// 封装response对象
				GameResponse response = new GameResponse(request);
				// 分派游戏逻辑处理器
				long start = System.currentTimeMillis();
				logger.info("协议  "+messageId + "处理开始!");
				try {
					handler.execute(request, response);
				} catch (Exception e) {
					logger.error(e.getMessage());
				}
				logger.info("协议  "+messageId + "处理完成!");
				long end = System.currentTimeMillis();
				long diff = (end - start);
				if (diff >= 200 && logger.isDebugEnabled()) {
					logger.warn(messageId + "逻辑处理时间过长！处理时间(s)：" + diff);
				}
				//私有协议

				response.getChannel().write(response);
			
				request = null;
				response = null;
			} else {
				if (logger.isEnabledFor(Level.WARN)) {
					logger.warn("指令[" + messageId + "]未找到");
				}
			}
		}
	}

	public void setMessageExecutor(Executor messageExecutor) {
		this.messageExecutor = messageExecutor;
	}

	@Value("${app.dispatcher.sleepTime}")
	public void setSleepTime(long sleepTime) {
		this.sleepTime = sleepTime;
		System.out.println(sleepTime);
	}
}
