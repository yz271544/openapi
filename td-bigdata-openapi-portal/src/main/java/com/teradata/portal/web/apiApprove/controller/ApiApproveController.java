package com.teradata.portal.web.apiApprove.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.apiApprove.pojo.ApiExamInfo;
import com.teradata.portal.web.apiApprove.service.ApiApproveService;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;

/**
 * api审批控制器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-10 上午10:08:01
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/apiApprove")
public class ApiApproveController {

	@Autowired
	ApiApproveService apiApproveService;

	/*
	 * api审批主页面
	 */
	@RequestMapping(value = "/apiApprove.htm")
	public ModelAndView apiApproveIndex(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("portal/apiApprove/apiApprove");
	}

	/*
	 * api审批-审批
	 */
	@RequestMapping(value = "/doApiApprove.action")
	@ResponseBody
	public JSONObject doApiApprove(HttpServletRequest request, ApiExamInfo apiExamInfo, HttpSession httpSession) {
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String userId = user.getUserId() + "";
		apiExamInfo.setAuditPersn(userId);
		Integer auditId = Integer.parseInt(UUIDUtils.getRandomNumNoZero(8));
		apiExamInfo.setAuditId(auditId);
		return apiApproveService.doApiApprove(apiExamInfo);
	}

	/*
	 * api审批-获取api沙箱测试信息
	 */
	@RequestMapping(value = "/getApiTestBox.action")
	@ResponseBody
	public List<ApiTestBox> getApiTestBox(HttpServletRequest request, ApiInfo apiItem, HttpSession httpSession) {
		return apiApproveService.getApiTestBox(apiItem);
	}
}
