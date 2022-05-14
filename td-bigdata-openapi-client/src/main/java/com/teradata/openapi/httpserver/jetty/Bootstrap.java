package com.teradata.openapi.httpserver.jetty;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.teradata.openapi.httpserver.jetty.server.JettyServer;

/**
 * 
 * httpServer入口. <br>
 * 利用Jetty实现的嵌入式Httpserver，利用HttpServlet进行编程
 * <p>
 * Copyright: Copyright (c) 2016年3月31日 上午10:35:43
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class Bootstrap {

	private static final Logger LOGGER = LoggerFactory.getLogger(Bootstrap.class);

	private static ApplicationContext applicationContext;

	private final static String[] configLocations = { "openapi-server.xml" };

	public static void main(String[] args) {
		initResource();
		initJettyServer();
	}

	/**
	 * 初始化资源
	 * 
	 * @date 2016年3月31日 上午11:01:56
	 * @author houbl
	 */
	private static void initResource() {
		applicationContext = new ClassPathXmlApplicationContext(configLocations);
	}

	/**
	 * 运行 jetty server
	 * 
	 * @date 2016年3月31日 上午11:02:38
	 * @author houbl
	 */
	private static void initJettyServer() {
		try {
			JettyServer server = applicationContext.getBean("jettyServer", JettyServer.class);
			LOGGER.info("start to run jetty server.");
			server.runJettyServer();
		}
		catch (Exception e) {
			LOGGER.error("Error:" + e.getMessage(), e);
		}
	}

}
