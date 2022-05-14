package com.teradata.portal.web.home.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.home.service.HomeService;

/**
 * 首页. <br>
 * 1：运维视图 2：开发视图 3：APP视图.
 * <p>
 * Copyright: Copyright (c) 2016-8-18 下午4:16:00
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/home")
public class HomeController {

	@Autowired
	HomeService homeService;

	/*
	 * 运维视图|APP视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	@RequestMapping(value = "/getApiChart.json")
	@ResponseBody
	public List<?> getApiChart(HttpServletRequest request) {
		Map<String, Object> param = new HashMap<String, Object>();
		String itemId = request.getParameter("itemId");
		param.put("itemId", itemId);
		return homeService.getApiChart(param);
	}

	/*
	 * 获取api状态运行情况
	 */
	@RequestMapping(value = "/getApiStatus.json")
	@ResponseBody
	public PageList<?> getApiStatus(HttpServletRequest request, HttpSession httpSession) {
		Map<String, Object> param = new HashMap<String, Object>();
		Integer apiStat = Integer.parseInt(request.getParameter("apiStat"));
		Integer apiSort = Integer.parseInt(request.getParameter("apiSort"));
		Integer apiVisitMethd = Integer.parseInt(request.getParameter("apiVisitMethd"));
		String apiName = request.getParameter("apiName");
		Integer pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));
		param.put("apiStat", apiStat);
		param.put("apiSort", apiSort);
		param.put("apiVisitMethd", apiVisitMethd);
		param.put("apiName", apiName);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);

		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		Integer userId = user.getUserId();
		Integer userGrpId = Integer.parseInt(httpSession.getAttribute("userGrpId").toString());
		String viewType = request.getParameter("viewType");
		String ifGroup = request.getParameter("ifGroup");
		param.put("userId", userId);
		param.put("userGrpId", userGrpId);
		param.put("viewType", viewType);
		param.put("ifGroup", ifGroup);
		return (PageList)homeService.getApiStatus(param);
	}

	/*
	 * 获取api数量（总数、活跃数、非活跃数、状态异常数）
	 */
	@RequestMapping(value = "/getApiTotal.json")
	@ResponseBody
	public List<?> getApiTotal(HttpServletRequest request, HttpSession httpSession) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String loginAcct = user.getLoginAcct();
		String viewType = request.getParameter("viewType");

		if (viewType.equals("monitorView")) {
			loginAcct = "system";
		}

		param.put("viewType", viewType);
		param.put("loginAcct", loginAcct);
		return homeService.getApiTotal(param);
	}

	/*
	 * 获取待审批数量（注册工号、权限、api发布）
	 */
	@RequestMapping(value = "/getExamCount.json")
	@ResponseBody
	public List<?> getExamCount(HttpServletRequest request) {
		return homeService.getExamCount();
	}

	/*
	 * 开发者视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	@RequestMapping(value = "/getOwnApiChart.json")
	@ResponseBody
	public List<?> getOwnApiChart(HttpServletRequest request, HttpSession httpSession) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String loginAcct = user.getLoginAcct();
		Integer userGrpId = Integer.parseInt(httpSession.getAttribute("userGrpId").toString());
		String ifGroup = request.getParameter("ifGroup"); // 是否选择开发组（0：个人 1：开发组）
		String itemId = request.getParameter("itemId");
		param.put("loginAcct", loginAcct);
		param.put("userGrpId", userGrpId);
		param.put("ifGroup", ifGroup);
		param.put("itemId", itemId);
		return homeService.getOwnApiChart(param);
	}

	/*
	 * 开发者视图-展示文字描述信息
	 */
	@RequestMapping(value = "/getStateCount.json")
	@ResponseBody
	public List<?> getStateCount(HttpServletRequest request, HttpSession httpSession) {
		Map<String, Object> param = new HashMap<String, Object>();
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String loginAcct = user.getLoginAcct();
		Integer userGrpId = Integer.parseInt(httpSession.getAttribute("userGrpId").toString());
		String ifGroup = request.getParameter("ifGroup"); // 是否选择开发组（0：个人 1：开发组）

		param.put("loginAcct", loginAcct);
		param.put("userGrpId", userGrpId);
		param.put("ifGroup", ifGroup);
		return homeService.getStateCount(param);
	}

	/*
	 * 跳转首页
	 */
	@RequestMapping(value = "/goHome.htm")
	public ModelAndView goHome(HttpSession httpSession) {
		ModelAndView mv = new ModelAndView();

		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		Integer userType = user.getUserType();

		String viewType = "";
		String viewName = "";
		String viewTitle = "";
		switch (userType) {
			case 1:// 应用人员
				viewType = "appView";
				viewName = "monitorAndAppView";
				viewTitle = "首页-APP视图";
				break;
			case 2:// 开发人员
				viewType = "developView";
				viewName = "developView";
				viewTitle = "首页-开发视图";
				break;
			case 3:// 运维人员
				viewType = "monitorView";
				viewName = "monitorAndAppView";
				viewTitle = "首页-运维视图";
				break;
			default:
				;
		}

		mv.addObject("viewType", viewType);
		mv.addObject("viewTitle", viewTitle);
		mv.setViewName("portal/home/" + viewName);
		return mv;
	}
}
