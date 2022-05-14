package com.teradata.openapi.framework.message.request;

import java.io.Serializable;

/**
 * 
 * 向master发送信息的实体 进行注册认证. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月21日 上午10:19:16
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class RegisterApplication implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7247820763285070382L;

	// client akka地址
	private String clientUrl;

	public String getClientUrl() {
		return clientUrl;
	}

	public void setClientUrl(String clientUrl) {
		this.clientUrl = clientUrl;
	}

}
