package com.teradata.openapi.access.core.actor.base;

import java.util.concurrent.Callable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import scala.concurrent.ExecutionContextExecutor;
import akka.actor.ActorContext;
import akka.actor.ActorRef;
import akka.actor.UntypedActor;
import akka.dispatch.Futures;

/**
 * 
 * 标题、简要说明. <br>
 * 通过改进 akka UntypedActor 实现非阻塞的多线程 特别遇到需要长时间的操作(读数据库，网络请求，读取大文件)的，可以继承该类 注意控制并发请求量，可能造成流量过大，慎用。
 * <p>
 * Copyright: Copyright (c) 2016年5月13日 下午2:28:01
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public abstract class BaseActor extends UntypedActor {

	protected Logger logger = LoggerFactory.getLogger("actor");

	protected ExecutionContextExecutor ec = context().dispatcher();

	// 控制并发速率
	// final RateLimiter rateLimiter = RateLimiter.create(10);
	@Override
	public void preStart() throws Exception {
		super.preStart();
	}

	@Override
	public void postStop() throws Exception {
		super.postStop();
	}

	@Override
	public void onReceive(Object message) {
		// rateLimiter.acquire(); // 控制等待 防止请求过快
		Futures.future(new Caller(sender(), self(), message, context()), ec);
	}

	public abstract void parallelProcess(ActorRef sender, Object message, ActorRef recipient, ActorContext context);

	private class Caller implements Callable {

		private ActorRef sender;

		private ActorRef recipient;

		private Object message;

		private ActorContext context;

		public Caller(ActorRef sender, ActorRef recipient, Object message, ActorContext context) {
			this.sender = sender;
			this.recipient = recipient;
			this.message = message;
			this.context = context;
		}

		@Override
		public Void call() throws Exception {
			parallelProcess(sender, message, recipient, context);
			return null;
		}
	}

}
