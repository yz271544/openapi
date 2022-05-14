package com.teradata.openapi.httpserver.jetty.server;

/**
 * 
 * jetty Server. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月31日 上午10:26:24
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface JettyServer {

	/**
	 * <pre>
	 * @throws Exception
	 * 运行 jetty server
	 * </pre>
	 */
	public void runJettyServer() throws Exception;
}
