package com.teradata.openapi.httpserver.core.server;


public interface RpcServer {

	/**
	 * 停止
	 */
	public void stop() throws Exception;

	/**
	 * 
	 * @param port
	 * @param timeout
	 * @throws Exception
	 */
	public void start(int port, int timeout) throws Exception;
}
