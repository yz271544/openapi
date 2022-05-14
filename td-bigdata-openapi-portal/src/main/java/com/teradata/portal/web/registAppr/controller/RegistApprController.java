package com.teradata.portal.web.registAppr.controller;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.registAppr.service.RegistApprService;

/**
 * 注册审批控制器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-7-5 上午10:29:13
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Lyndon.Hu@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/registAppr")
public class RegistApprController {

	public static Map getParameterMap(HttpServletRequest request) {
		// 参数Map
		Map properties = request.getParameterMap();
		// 返回值Map
		Map returnMap = new HashMap();
		Iterator entries = properties.entrySet().iterator();
		Map.Entry entry;
		String name = "";
		String value = "";
		while (entries.hasNext()) {
			entry = (Map.Entry) entries.next();
			name = (String) entry.getKey();
			Object valueObj = entry.getValue();
			if (null == valueObj) {
				value = "";
			} else if (valueObj instanceof String[]) {
				String[] values = (String[]) valueObj;
				for (int i = 0; i < values.length; i++) {
					value = values[i] + ",";
				}
				value = value.substring(0, value.length() - 1);
			} else {
				value = valueObj.toString();
			}
			returnMap.put(name, value);
		}
		return returnMap;
	}

	@Autowired
	// ApiReleaseService apiReleaseService;
	RegistApprService registApprService;

	/*
	 * api发布主页面
	 */
	@RequestMapping(value = "/registAppr.htm")
	public ModelAndView apiDeployIndex(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("admin/right/registAppr");
	}

	/*
	 * 生成唯一id
	 */
	@RequestMapping(value = "/generateId.json")
	@ResponseBody
	public JSONObject generateId(HttpServletRequest request) throws Exception {
		Integer num = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("num"), "8"));
		JSONObject json = new JSONObject();
		json.put("id", UUIDUtils.getRandomNumNoZero(num));
		return json;
	}

	/*
	 * 获取Reg信息列表
	 */
	@RequestMapping(value = "/getRegInfo.json")
	@ResponseBody
	public PageList<?> getRegInfo(HttpServletRequest request, HttpSession httpSession) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		String login_acct = StringUtils.defaultIfBlank(request.getParameter("loginAcct"), "");
		System.out.println("-------->" + login_acct);
		Integer userStat = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("userStatCode"), "-2")); // 默认获取未审批记录
		System.out.println("userStat:" + userStat);
		Date beginDate = StringUtils.isBlank(request.getParameter("beginDate")) ? null : DateUtils.parseDate(request.getParameter("beginDate"),
		                                                                                                     "yyyy-MM-dd");
		Date endDate = StringUtils.isBlank(request.getParameter("endDate")) ? null : DateUtils.parseDate(request.getParameter("endDate"),
		                                                                                                 "yyyy-MM-dd");
		// String apiRange = request.getParameter("apiRange"); // 1:我 2:所有
		Integer pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

		param.put("loginAcct", login_acct);
		param.put("userStatCode", userStat);
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);
		return (PageList)registApprService.getRegInfo(param);
	}

	/*
	 * 获取下拉框
	 */
	@RequestMapping(value = "/getSelect.json")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSelect(HttpServletRequest request) {
		HashMap<String, Object> param = (HashMap<String, Object>) RegistApprController.getParameterMap(request);
		List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
		try {
			Class<?> ownerClass = registApprService.getClass();
			Object[] args = new Object[1];
			args[0] = param;
			int paramSize = param.keySet().size();
			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = param.getClass();
			String methodName = request.getParameter("method");
			System.out.println(paramSize);

			Method method;
			if (paramSize == 1) {
				method = ownerClass.getMethod(methodName);
				rstList = (List<Map<String, Object>>) method.invoke(registApprService);
			} else {
				method = ownerClass.getMethod(methodName, argsClass);
				rstList = (List<Map<String, Object>>) method.invoke(registApprService, args);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rstList;
	}

	/*
	 * 注册用户审批-审批
	 */
	@RequestMapping(value = "/doRegApprove.action")
	@ResponseBody
	public JSONObject doRegApprove(HttpServletRequest request, UserInfo user, HttpSession httpSession) {
		System.out.println("---> userId: " + user.getUserId());
		System.out.println("---> userStat: " + user.getUserStat());
		System.out.println("---> auditAdvc: " + user.getAuditAdvc());
		UserInfo audit_persn = (UserInfo) httpSession.getAttribute("userInfo");
		user.setAuditPersn(audit_persn.getLoginAcct());
		return registApprService.doRegApprove(user);
	}
}
