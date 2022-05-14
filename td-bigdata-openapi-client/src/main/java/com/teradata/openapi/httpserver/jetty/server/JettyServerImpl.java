package com.teradata.openapi.httpserver.jetty.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;

/**
 * 
 * jetty server 实现 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月31日 上午10:26:47
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class JettyServerImpl implements JettyServer {

	private Server server;

	private WebAppContext webAppContextHandler;

	/*
	 * (non-Javadoc)
	 * @see com.baidu.EmbeddingREST.util.JettyServer#runJettyServer()
	 */
	@Override
	public void runJettyServer() throws Exception {
		server.setHandler(webAppContextHandler);
		server.start();
		server.join();

	}

	public void setServer(Server server) {
		this.server = server;
	}

	public void setWebAppContextHandler(WebAppContext webAppContextHandler) {
		this.webAppContextHandler = webAppContextHandler;
	}

}
