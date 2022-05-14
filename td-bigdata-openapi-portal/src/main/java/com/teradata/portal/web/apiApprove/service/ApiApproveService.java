package com.teradata.portal.web.apiApprove.service;

import java.util.List;

import net.sf.json.JSONObject;

import com.teradata.portal.web.apiApprove.pojo.ApiExamInfo;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-10 上午10:08:31
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public interface ApiApproveService {

	/*
	 * api审批
	 */
	public JSONObject doApiApprove(ApiExamInfo apiExamInfo);

	/*
	 * 查看待审批api的测试沙箱信息
	 */
	public List<ApiTestBox> getApiTestBox(ApiInfo apiItem);
}
