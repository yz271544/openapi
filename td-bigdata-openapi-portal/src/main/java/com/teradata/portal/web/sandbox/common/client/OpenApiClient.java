package com.teradata.portal.web.sandbox.common.client;

import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.rop.client.ClientRequest;
import com.teradata.openapi.rop.client.DefaultRopClient;

/**
 * 
 * 调用opanapi client客户端. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年6月27日 上午10:19:47
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class OpenApiClient {

	public static final String SERVER_URL = LoadProperties.getProp("serviceUrl") + "/router";

	private DefaultRopClient ropClient;

	/**
	 * 创建客户端对象
	 * 
	 * @param appKey
	 * @param secret
	 */
	public OpenApiClient(String appKey, String secret) {
		ropClient = new DefaultRopClient(SERVER_URL, appKey, secret);

	}

	public ClientRequest buildClientRequest() {
		return ropClient.buildClientRequest();
	}
}
