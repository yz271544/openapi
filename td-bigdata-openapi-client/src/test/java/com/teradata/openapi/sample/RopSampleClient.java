package com.teradata.openapi.sample;

import com.teradata.openapi.rop.client.ClientRequest;
import com.teradata.openapi.rop.client.CompositeResponse;
import com.teradata.openapi.rop.client.DefaultRopClient;
import com.teradata.openapi.sample.request.LogonRequest;
import com.teradata.openapi.sample.response.LogonResponse;

public class RopSampleClient {

	public static final String SERVER_URL = "http://localhost:9080/restserver/router";

	private DefaultRopClient ropClient;

	/**
	 * 创建客户端对象
	 * 
	 * @param appKey
	 * @param secret
	 */
	public RopSampleClient(String appKey, String secret) {
		ropClient = new DefaultRopClient(SERVER_URL, appKey, secret);
	}

	/**
	 * 登录系统
	 * 
	 * @return
	 */
	public String logon() {
		LogonRequest ropRequest = new LogonRequest();
		ropRequest.setDEAL_DATE("2016-04-30");
		ropRequest.setFields("DEAL_DATE,REGION_CODE,CITY_CODE,BARGAIN_NUM,SHOULD_PAY,SHOULD_PAY_AMT");
		ropRequest.setREGION_CODE("00");
		ropRequest.setCITY_CODE("01");
		CompositeResponse response = ropClient.buildClientRequest().get(ropRequest, LogonResponse.class, "tb_rpt_bo_mon", "1", "asyn");
		String sessionId = ((LogonResponse) response.getSuccessResponse()).getSessionId();
		ropClient.setSessionId(sessionId);
		return sessionId;
	}

	public void logout() {
		ropClient.buildClientRequest().get(LogonResponse.class, "user.logout", "1.0", "syn");
	}

	public ClientRequest buildClientRequest() {
		return ropClient.buildClientRequest();
	}
}
