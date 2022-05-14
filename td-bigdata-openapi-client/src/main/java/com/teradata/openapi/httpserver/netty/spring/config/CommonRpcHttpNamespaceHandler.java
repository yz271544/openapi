package com.teradata.openapi.httpserver.netty.spring.config;

import org.springframework.beans.factory.xml.NamespaceHandlerSupport;

/**
 * 
 * spring句柄. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月30日 上午11:12:04
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class CommonRpcHttpNamespaceHandler extends NamespaceHandlerSupport {

	/*
	 * (non-Javadoc)
	 * @see org.springframework.beans.factory.xml.NamespaceHandler#init()
	 */
	@Override
	public void init() {
		registerBeanDefinitionParser("registry", new CommonRpcHttpRegisteryParser());
	}

}
