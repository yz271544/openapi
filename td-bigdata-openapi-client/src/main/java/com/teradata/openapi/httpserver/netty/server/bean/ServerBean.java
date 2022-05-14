package com.teradata.openapi.httpserver.netty.server.bean;

import io.netty.handler.codec.http.DefaultHttpContent;
import io.netty.handler.codec.http.DefaultHttpResponse;

import java.io.Serializable;

/**
 * 
 * 服务bean. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月30日 上午10:57:18
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class ServerBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1066483530993789009L;

	private DefaultHttpResponse response;

	private DefaultHttpContent defaultHttpContent;

	private boolean keepAlive;

	/**
	 * @return the response
	 */
	public DefaultHttpResponse getResponse() {
		return response;
	}

	public ServerBean(DefaultHttpResponse response, DefaultHttpContent defaultHttpContent, boolean keepAlive) {
		super();
		this.response = response;
		this.defaultHttpContent = defaultHttpContent;
		this.keepAlive = keepAlive;
	}

	/**
	 * @param response the response to set
	 */
	public void setResponse(DefaultHttpResponse response) {
		this.response = response;
	}

	/**
	 * @return the defaultHttpContent
	 */
	public DefaultHttpContent getDefaultHttpContent() {
		return defaultHttpContent;
	}

	/**
	 * @param defaultHttpContent the defaultHttpContent to set
	 */
	public void setDefaultHttpContent(DefaultHttpContent defaultHttpContent) {
		this.defaultHttpContent = defaultHttpContent;
	}

	/**
	 * @return the keepAlive
	 */
	public boolean isKeepAlive() {
		return keepAlive;
	}

	/**
	 * @param keepAlive the keepAlive to set
	 */
	public void setKeepAlive(boolean keepAlive) {
		this.keepAlive = keepAlive;
	}

}
