package com.teradata.portal.web.apiRelease.controller;

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
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.apiRelease.pojo.ApiArgRst;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.apiRelease.pojo.ResponseResult;
import com.teradata.portal.web.apiRelease.pojo.SorcFieldRst;
import com.teradata.portal.web.apiRelease.service.ApiReleaseService;
import com.xiaoleilu.hutool.http.HttpUtil;

/**
 * API发布控制器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-7-5 上午10:29:13
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/apiRelease")
public class ApiReleaseController {

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
	ApiReleaseService apiReleaseService;

	/*
	 * api发布主页面
	 */
	@RequestMapping(value = "/apiRelease.htm")
	public ModelAndView apiDeployIndex(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("portal/apiRelease/apiRelease");
	}

	/*
	 * 克隆api（基本信息+详情）
	 */
	@RequestMapping(value = "/cloneApi.action")
	@ResponseBody
	public JSONObject cloneApi(HttpServletRequest request, ApiInfo apiItem, HttpSession httpSession) throws Exception {
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String userId = user.getUserId() + "";
		apiItem.setRelsePersn(userId);
		return apiReleaseService.cloneApi(apiItem);
	}

	/*
	 * api参数配置主页面
	 */
	@RequestMapping(value = "/configApiArg.htm")
	public ModelAndView configApiArg(HttpServletRequest request, HttpSession httpSession) {
		ModelAndView mv = new ModelAndView();
		Integer apiId = Integer.parseInt(request.getParameter("apiId"));
		Integer apiVersion = Integer.parseInt(request.getParameter("apiVersion"));
		String apiName = request.getParameter("apiName");
		String apiVisitMethd = request.getParameter("apiVisitMethd");

		List<ApiArgRst> list = apiReleaseService.getApiArg(apiId, apiVersion);
		JSONArray json = JSONArray.fromObject(list);
		String result = json.toString().replaceAll("\"", "'");
		boolean updateFlag = list.size() != 0;
		mv.setViewName("portal/apiRelease/configApiArg");
		mv.addObject("apiId", apiId);
		mv.addObject("apiVersion", apiVersion);
		mv.addObject("apiName", apiName);
		mv.addObject("apiArg", result);
		mv.addObject("updateFlag", updateFlag);
		mv.addObject("apiVisitMethd", apiVisitMethd);
		return mv;
	}

	/*
	 * 删除api
	 */
	@RequestMapping(value = "/delApi.action")
	@ResponseBody
	public JSONObject delApi(HttpServletRequest request, ApiInfo apiItem) throws Exception {
		return apiReleaseService.delApi(apiItem);
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
	 * api发布主页面
	 */
	@RequestMapping(value = "/getApiDetail.json")
	@ResponseBody
	public Map<String, Object> getApiDetail(HttpServletRequest request, HttpSession httpSession) {
		Map<String, Object> apiDetail = new HashMap<String, Object>();

		Integer apiId = Integer.parseInt(request.getParameter("apiId"));
		Integer apiVersion = Integer.parseInt(request.getParameter("apiVersion"));
		String apiName = request.getParameter("apiName");
		List<ApiArgRst> apiArgRst = apiReleaseService.getApiArg(apiId, apiVersion);

		apiDetail.put("apiId", apiId);
		apiDetail.put("apiVersion", apiVersion);
		apiDetail.put("apiName", apiName);
		apiDetail.put("apiArg", apiArgRst);

		return apiDetail;
	}

	/*
	 * 获取api信息列表
	 */
	@RequestMapping(value = "/getApiInfo.json")
	@ResponseBody
	public PageList<?> getApiInfo(HttpServletRequest request, HttpSession httpSession) throws Exception {
		Map<String, Object> param = new HashMap<String, Object>();
		Integer apiSort = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("apiSort"), "-1"));
		Integer apiId = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("apiId"), "-1"));
		String apiName = StringUtils.defaultIfBlank(request.getParameter("apiName"), "");
		Integer examStat = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("examStat"), "-1"));
		Date beginDate = StringUtils.isBlank(request.getParameter("beginDate")) ? null : DateUtils.parseDate(request.getParameter("beginDate"),
		                                                                                                     "yyyy-MM-dd");
		Date endDate = StringUtils.isBlank(request.getParameter("endDate")) ? null : DateUtils.parseDate(request.getParameter("endDate"),
		                                                                                                 "yyyy-MM-dd");
		String apiRange = request.getParameter("apiRange"); // 1:我 2:所有
		Integer pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
		Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

		param.put("apiSort", apiSort);
		param.put("apiId", apiId);
		param.put("apiName", apiName);
		param.put("examStat", examStat);
		param.put("beginDate", beginDate);
		param.put("endDate", endDate);
		param.put("pageIndex", pageIndex);
		param.put("pageSize", pageSize);

		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String userId = user.getUserId() + "";
		param.put("apiRange", apiRange);
		param.put("userId", userId);
		return (PageList)apiReleaseService.getApiInfo(param);
	}

	/*
	 * 获取下拉框
	 */
	@RequestMapping(value = "/getSelect.json")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getSelect(HttpServletRequest request) {
		HashMap<String, Object> param = (HashMap<String, Object>) ApiReleaseController.getParameterMap(request);
		List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
		try {
			Class<?> ownerClass = apiReleaseService.getClass();
			Object[] args = new Object[1];
			args[0] = param;
			int paramSize = param.keySet().size();
			Class<?>[] argsClass = new Class[args.length];
			argsClass[0] = param.getClass();
			String methodName = request.getParameter("method");
			System.out.println("paramSize:" + paramSize);

			Method method;
			if (paramSize == 1) {
				method = ownerClass.getMethod(methodName);
				rstList = (List<Map<String, Object>>) method.invoke(apiReleaseService);
			} else {
				method = ownerClass.getMethod(methodName, argsClass);
				rstList = (List<Map<String, Object>>) method.invoke(apiReleaseService, args);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		return rstList;
	}

	/*
	 * 获取不同数据源的字段（取交集）
	 */
	@RequestMapping(value = "/getSourceField.json")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<SorcFieldRst> getSourceField(HttpServletRequest request) throws Exception {
		String jsonArray = request.getParameter("jsonArray");
		List<Map<String, String>> param = JSONArray.toList(JSONArray.fromObject(jsonArray), new HashMap<Object, Object>(), new JsonConfig());
		return apiReleaseService.getSourceField(param);
	}

	/*
	 * 获取表信息结果集
	 */
	@RequestMapping(value = "/getTable.json")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public List<Map<String, Object>> getTable(HttpServletRequest request) throws Exception {
		HashMap<String, Object> param = (HashMap<String, Object>) ApiReleaseController.getParameterMap(request);
		List<Map<String, Object>> list = apiReleaseService.getTable(param);

		// 加载用户选择的表信息到数据源结果表_begin
		if (list.size() == 0) {
			Integer sourceId = Integer.parseInt(param.get("sourceId").toString());
			String schemaName = param.get("schemaName").toString();
			String tabName = param.get("q").toString();
			try {
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("refFlag", 4);
				paramMap.put("sourceId", sourceId);
				paramMap.put("schemaName", schemaName);
				paramMap.put("tabName", tabName);
				String SERVER_URL = LoadProperties.getProp("serviceUrl");
				String response = HttpUtil.post(String.format("%s/load", SERVER_URL), paramMap);
				ResponseResult responseResult = (ResponseResult) JSONObject.toBean(JSONObject.fromObject(response), ResponseResult.class);
				String retCode = responseResult.getRetCode();
				if (retCode.equals("0")) {
					list = apiReleaseService.getTable(param);
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		// 加载用户选择的表信息到数据源结果表_end

		return list;
	}

	/*
	 * 修改api
	 */
	@RequestMapping(value = "/modifyApiItem.action")
	@ResponseBody
	public JSONObject modifyApiItem(HttpServletRequest request, ApiInfo apiItem) throws Exception {
		return apiReleaseService.modifyApiItem(apiItem);
	}

	/*
	 * 新增api
	 */
	@RequestMapping(value = "/releaseApiItem.action")
	@ResponseBody
	public JSONObject releaseApiItem(HttpServletRequest request, ApiInfo apiItem) throws Exception {
		return apiReleaseService.releaseApiItem(apiItem);
	}

	/*
	 * 配置api详情
	 */
	@RequestMapping(value = "/saveApiArg.action")
	@ResponseBody
	@SuppressWarnings("unchecked")
	public JSONObject saveApiArg(HttpServletRequest request) throws Exception {
		Integer apiId = Integer.parseInt(request.getParameter("apiId"));
		Integer apiVersion = Integer.parseInt(request.getParameter("apiVersion"));
		String jsonArray1 = request.getParameter("jsonArray1");
		List<Map<String, String>> param1 = JSONArray.toList(JSONArray.fromObject(jsonArray1), new HashMap<Object, Object>(), new JsonConfig());

		String jsonArray2 = request.getParameter("jsonArray2");
		List<Map<String, String>> param2 = JSONArray.toList(JSONArray.fromObject(jsonArray2), new HashMap<Object, Object>(), new JsonConfig());
		JSONObject jsonObject = apiReleaseService.saveApiArg(param1, param2, apiId, apiVersion);
		apiReleaseService.modifyRemoteActor(apiId, apiVersion);
		return jsonObject;
	}

	/*
	 * 新增api基本信息
	 */
	@RequestMapping(value = "/saveApiItem.action")
	@ResponseBody
	public JSONObject saveApiItem(HttpServletRequest request, ApiInfo apiItem, HttpSession httpSession) throws Exception {
		UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
		String userId = user.getUserId() + "";
		apiItem.setRelsePersn(userId);
		return apiReleaseService.saveApiItem(apiItem);
	}
}
