package com.company.game.dispatcher;

import io.netty.channel.Channel;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.company.game.dispatcher.exec.BusinessLogicExecutorBase;
import com.company.game.dispatcher.msg.RequestMsgBase;
import com.company.game.dispatcher.util.ClassUtil;
import com.company.game.dispatcher.util.HandlerReflectInfo;

/**
 * 抽象了分发器
 * 多线程执行
 * 某个msgType指定了某个业务逻辑的反射数据
 * submit到线程池中
 * @author xingchencheng
 *
 */

public class Dispatcher {
	
	private static final int MAX_THREAD_NUM = 50;
	
	private static ExecutorService executorService =
			Executors.newFixedThreadPool(MAX_THREAD_NUM);
	
	public static void submit(Channel channel, Object msgObject) {
		
		RequestMsgBase msg = (RequestMsgBase) msgObject;
		
		HandlerReflectInfo handlerReflectInfo = 
				ClassUtil.getExecutorClassByType(msg.getType());
		
		BusinessLogicExecutorBase executor = new BusinessLogicExecutorBase();
		executor.setChannel(channel);
		executor.setMsgObject(msg);
		executor.setClazz(handlerReflectInfo.getBelongClazz());
		executor.setMethod(handlerReflectInfo.getMethod());
		
		executorService.submit(executor);
	}
}