package com.teradata.openapi.access.core.ext;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import akka.actor.ActorSystem;

import com.typesafe.config.Config;

/**
 * 
 * 标题、简要说明. <br>
 * Factory bean that creates actor system for given name and/or configuration. Created actor system is singleton
 * <p>
 * Copyright: Copyright (c) 2016年5月13日 下午2:43:27
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class ActorSystemFactoryBean implements FactoryBean<ActorSystem>, ApplicationContextAware, InitializingBean {

	private ApplicationContext ctx;

	private String name;

	private Config config;

	private ActorSystem actorSystem;

	@Override
	public ActorSystem getObject() throws Exception {
		return actorSystem;
	}

	@Override
	public Class<?> getObjectType() {
		return ActorSystem.class;
	}

	@Override
	public boolean isSingleton() {
		return true;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setConfig(Config config) {
		this.config = config;
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		ActorSystem system;
		if (name != null && config != null) {
			system = ActorSystem.create(name, config);
		} else if (name != null) {
			system = ActorSystem.create(name);
		} else {
			system = ActorSystem.create();
		}
		// init extensions
		SpringExtension.instance().get(system).setApplicationContext(ctx);
		actorSystem = system;
	}
}