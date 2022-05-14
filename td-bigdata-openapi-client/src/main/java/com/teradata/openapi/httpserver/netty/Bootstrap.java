package com.teradata.openapi.httpserver.netty;

import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * 
 * httpServer netty 入口 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月30日 上午11:35:49
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class Bootstrap {

	@SuppressWarnings("resource")
	public static void main(String[] args) {
		String[] resource = { "com/teradata/openapi/httpserver/openApiHttpServer.xml" };
		new ClassPathXmlApplicationContext(resource);
	}
}
