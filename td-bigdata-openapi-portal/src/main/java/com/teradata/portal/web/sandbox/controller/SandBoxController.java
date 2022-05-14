package com.teradata.portal.web.sandbox.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.portal.web.sandbox.pojo.DynamicApiArg;
import com.teradata.portal.web.sandbox.service.SandBoxService;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;

/**
 * 
 * 测试沙箱控制器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年6月14日 上午10:14:01
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "apitools")
public class SandBoxController {

	@Autowired
	SandBoxService sandBoxService;

	/**
	 * 测试沙箱首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/apitools.htm")
	public ModelAndView apitoolsIndex(String apiId, String apiName, String apiVersion, String sortId, String sourceFlag) {
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("apiId", apiId);
		paramMap.put("apiName", apiName);
		paramMap.put("apiVersion", apiVersion);
		paramMap.put("sortId", sortId);
		// 来源标识
		paramMap.put("sourceFlag", sourceFlag);
		String showJsp = "portal/sandbox/apiTools";
		if ("2".equals(sourceFlag)) {
			showJsp = "portal/sandbox/apiTestTools";
		}
		return new ModelAndView(showJsp, paramMap);
	}

	@RequestMapping(value = "/getSelectJson.json")
	@ResponseBody
	public List<Map<String, Object>> getSelectJson(HttpServletRequest request, String selectName) {
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		String apiSort = StringUtils.defaultIfEmpty(request.getParameter("apiSort"), "1");
		if ("apiSort".equals(selectName)) {
			retList = sandBoxService.getApiSortMap();
		}
		/*else if ("apiName".equals(selectName)) {
			String reqMethod = request.getParameter("reqMethod");
			String term = request.getParameter("term");
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = 30;
			int start = (page-1)*limit + 1;
			int end = page*limit;
			retList = sandBoxService.getApiInfoMapBySortPagination(Integer.parseInt(apiSort), reqMethod, term, start, end);
		}*/
		else if ("apiName".equals(selectName)) {
			String reqMethod = request.getParameter("reqMethod");
			retList = sandBoxService.getApiInfoMapBySort(Integer.parseInt(apiSort), reqMethod);
		}
		else if ("apiVersion".equals(selectName)) {
			String apiId = StringUtils.defaultIfEmpty(request.getParameter("apiId"), "1001");
			retList = sandBoxService.getApiVersionMapById(Integer.parseInt(apiSort), Integer.parseInt(apiId));
		} else if ("apiOther".equals(selectName)) {
			String apiId = StringUtils.defaultIfEmpty(request.getParameter("apiId"), "1001");
			String apiVersion = StringUtils.defaultIfEmpty(request.getParameter("apiVersion"), "1");
			retList = sandBoxService.getApiOtherMapById(Integer.parseInt(apiId), Integer.parseInt(apiVersion));
		}
		return retList;
	}

	@RequestMapping(value = "/getSelectApiName.json",method = RequestMethod.GET)
	@ResponseBody
	public Map<String, Object> getSelect2Json(HttpServletRequest request, String selectName) {
		Map<String, Object> resultMap = new HashMap<String, Object>();
		List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
		String apiSort = StringUtils.defaultIfEmpty(request.getParameter("apiSort"), "1");
		Long totalCount = 0L;
		if ("apiName".equals(selectName)) {
			String reqMethod = request.getParameter("reqMethod");
			String term = request.getParameter("term");
			int page = Integer.parseInt(request.getParameter("page"));
			int limit = 30;
			int start = (page - 1) * limit + 1;
			int end = page * limit;
			retList = sandBoxService.getApiInfoMapBySortPagination(Integer.parseInt(apiSort), reqMethod, term, start, end);
			totalCount = sandBoxService.loadExpApiNameCount(Integer.parseInt(apiSort), reqMethod, term);
		}
		resultMap.put("items", retList);
		resultMap.put("totalCount", totalCount);
		return resultMap;
	}

	@RequestMapping(value = "/getApiReqMethodJson.json")
	@ResponseBody
	public String getApiReqMethodJson(int apiId, int apiVersion) {
		return sandBoxService.getApiReqMethodById(apiId, apiVersion);
	}

	@RequestMapping(value = "/getDynamicArgAreaJson.json")
	@ResponseBody
	public DynamicApiArg getDynamicArgAreaJson(int apiId, int apiVersion, int reqMethod) {
		return sandBoxService.getDynamicApiArgById(apiId, apiVersion, reqMethod);
	}

	@RequestMapping(value = "/getApiData.json")
	@ResponseBody
	public Map<String, String> getApiData(HttpServletRequest request, ApiFormArg formArg, String invokeMethod, String appSecret,
	        String sourceFlag) {
		Map<String, String[]> srcParamMap = request.getParameterMap();
		return sandBoxService.callRemoteApiData(srcParamMap, formArg, invokeMethod, appSecret, sourceFlag);
	}

}
