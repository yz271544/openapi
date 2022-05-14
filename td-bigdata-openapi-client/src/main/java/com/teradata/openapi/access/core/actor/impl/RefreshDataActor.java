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
import com.teradata.openapi.framework.deploy.MetaTableInfoCollectResult;
import com.teradata.openapi.framework.deploy.RefreshApiMetaInfo;
import com.teradata.openapi.framework.deploy.RefreshApiMetaInfoResult;
import com.teradata.openapi.framework.deploy.WebSearchMetaTableInfo;
import com.teradata.openapi.rop.Constants;
import com.typesafe.config.ConfigFactory;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 
 * 刷新元数据信息. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年8月10日 下午4:48:55
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Actor
public class RefreshDataActor extends UntypedActor {

	private final static Log logger = StaticLog.get();

	Timeout timeout = new Timeout(Duration.create(Constants.AKKA_TIME_OUT, TimeUnit.SECONDS));

	private ActorSelection remoteMaster;

	@Override
	public void preStart() throws Exception {
		remoteMaster = getContext().system().actorSelection(ConfigFactory.load().getString("remoteActor.masterActorUrl"));
	}

	@Override
	public void onReceive(Object message) {
		if (message instanceof WebSearchMetaTableInfo || message instanceof RefreshApiMetaInfo) {
			remoteMaster.tell(message, getSelf());
		} else if (message instanceof MetaTableInfoCollectResult) {
			Map<String, Promise> proMap = PromiseIdReal.getInstance();
			Promise pro = proMap.get(((MetaTableInfoCollectResult) message).reqId());
			logger.info("返回采集元数据的结果：" + message.toString());
			pro.success(message);
		} else if (message instanceof RefreshApiMetaInfoResult) {
			Map<String, Promise> proMap = PromiseIdReal.getInstance();
			Promise pro = proMap.get(((RefreshApiMetaInfoResult) message).reqId());
			logger.info("返回API后台元数据刷新结果：" + message.toString());
			pro.success(message);
		} else {
			unhandled(message);
		}
	}
}
