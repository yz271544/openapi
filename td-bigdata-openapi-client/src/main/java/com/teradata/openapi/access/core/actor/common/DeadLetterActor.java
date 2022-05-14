package com.teradata.openapi.access.core.actor.common;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import akka.actor.DeadLetter;
import akka.actor.UntypedActor;

import com.teradata.openapi.access.core.ext.Actor;

/**
 * 
 * 监控 DeadLetter 方便akka调试 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年5月13日 下午2:46:43
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Actor
public class DeadLetterActor extends UntypedActor {

	protected Logger logger = LoggerFactory.getLogger("deadletter");

	@Override
	public void onReceive(Object message) throws Exception {

		if (message instanceof DeadLetter) {
			DeadLetter deadLetter = (DeadLetter) message;
			logger.error(deadLetter.toString());
		}
	}

	@Override
	public void preStart() throws Exception {
		super.preStart();
		getContext().system().eventStream().subscribe(self(), DeadLetter.class);
	}

	@Override
	public void postStop() throws Exception {
		super.postStop();
		getContext().system().eventStream().unsubscribe(self());
	}
}
