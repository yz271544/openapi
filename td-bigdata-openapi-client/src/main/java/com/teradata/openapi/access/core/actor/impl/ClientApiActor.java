package com.teradata.openapi.access.core.actor.impl;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import akka.actor.ActorSelection;
import akka.actor.UntypedActor;
import akka.util.Timeout;

import com.teradata.openapi.access.core.ext.Actor;
import com.teradata.openapi.access.core.ext.PromiseIdReal;
import com.teradata.openapi.framework.deploy.SyncExecuteException;
import com.teradata.openapi.framework.deploy.SyncExecuteResult;
import com.teradata.openapi.framework.message.request.ReqToFindBody;
import com.teradata.openapi.framework.message.response.FindToReqAsynYIK;
import com.teradata.openapi.rop.Constants;
import com.typesafe.config.ConfigFactory;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

@Actor
public class ClientApiActor extends UntypedActor {

	private final static Log logger = StaticLog.get();

	Timeout timeout = new Timeout(Duration.create(Constants.AKKA_TIME_OUT, TimeUnit.SECONDS));

	private ActorSelection remoteMaster;

	@Override
	public void preStart() throws Exception {
		remoteMaster = getContext().system().actorSelection(ConfigFactory.load().getString("remoteActor.masterActorUrl"));
	}

	@Override
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public void onReceive(Object message) {
		Map<String, Promise> proMap = PromiseIdReal.getInstance();
		if (message instanceof ReqToFindBody) {
			remoteMaster.tell(message, getSelf());
		} else if (message instanceof FindToReqAsynYIK) {
			Promise pro = proMap.get(((FindToReqAsynYIK) message).getReqID());
			logger.info("接收FindToReqAsynYIK数据 reqId:{}" + ((FindToReqAsynYIK) message).getReqID());
			pro.success(message);
		} else if (message instanceof SyncExecuteResult) {
			Promise pro = proMap.get(((SyncExecuteResult) message).reqID());
			logger.info("接收SyncExecuteResult数据 reqId:{}" + ((SyncExecuteResult) message).reqID());
			pro.success(message);
		} else if (message instanceof SyncExecuteException) {
			Promise pro = proMap.get(((SyncExecuteException) message).reqID());
			logger.info("接收SyncExecuteException数据reqId:{}" + ((SyncExecuteException) message).reqID());
			pro.success(message);
		} else {
			unhandled(message);
		}
	}
}
