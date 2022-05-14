package com.teradata.portal.web.apiApprove.service.impl;

import java.util.List;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teradata.portal.web.apiApprove.dao.ApiApproveMapper;
import com.teradata.portal.web.apiApprove.pojo.ApiExamInfo;
import com.teradata.portal.web.apiApprove.service.ApiApproveService;
import com.teradata.portal.web.apiRelease.dao.ApiReleaseMapper;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-10 上午10:09:01
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Service
public class ApiApproveServiceImpl implements ApiApproveService {

	@Autowired
	ApiApproveMapper apiApproveMapper;

	@Autowired
	ApiReleaseMapper apiReleaseMapper;

	JSONObject json = new JSONObject();

	/*
	 * api审批
	 */
	@Override
	public JSONObject doApiApprove(ApiExamInfo apiExamInfo) {
		// 修改apiinfo主表状态信息
		ApiInfo apiItem = new ApiInfo();
		apiItem.setApiId(apiExamInfo.getApiId());
		apiItem.setApiVersion(apiExamInfo.getApiVersion());
		apiItem.setExamStat(apiExamInfo.getAuditStat());
		apiReleaseMapper.releaseApiItem(apiItem);

		// 修改examinfo表中有效状态为0
		apiApproveMapper.updateApiExamInfo(apiExamInfo);

		// 插入examinfo表最新的审批意见
		apiApproveMapper.insertApiExamInfo(apiExamInfo);

		json.put("data", 1);
		json.put("success", true);
		json.put("msg", "成功");
		return json;
	}

	/*
	 * 查看待审批api的测试沙箱信息
	 */
	@Override
	public List<ApiTestBox> getApiTestBox(ApiInfo apiItem) {
		return apiApproveMapper.queryApiTestBox(apiItem);
	}

}
