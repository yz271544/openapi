package com.teradata.portal.admin.subscribe.controller;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.teradata.openapi.framework.util.DateUtils;
import com.teradata.openapi.framework.util.FastJSONUtil;
import com.teradata.openapi.framework.util.HtmlUtil;
import com.teradata.openapi.framework.util.JSONUtil;
import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.rop.Constants;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.subscribe.pojo.ApiRssInfo;
import com.teradata.portal.admin.subscribe.pojo.FtpUseInfo;
import com.teradata.portal.admin.subscribe.service.RssService;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * 订阅管理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年7月18日 上午10:00:30
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/admin/rss")
public class RssController {

	private final static Log logger = StaticLog.get();

	@Autowired
	RssService rssService;

	/**
	 * 订阅管理首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/main.htm")
	public ModelAndView rssMain() {
		Map<String, String> paramMap = new HashMap<String, String>();
		return new ModelAndView("admin/rss/rssMain", paramMap);
	}

	@RequestMapping(value = "/queryRssInfoList.json", method = { RequestMethod.POST })
	public void queryApiRssInfoList(HttpServletResponse response, HttpSession session, ApiRssInfo rssInfo) {
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		rssInfo.setAppkey(user.getLoginAcct());
		PageList<ApiRssInfo> rssInfoList = rssService.queryRssInfosList(rssInfo);
		JSONArray jsonArray = JSONUtil.formatJSONDateList(rssInfoList);
		JSONObject rtnJson = new JSONObject();
		// 分页固定格式
		rtnJson.put("iTotalRecords", rssInfoList.getPaginator().getTotalCount());
		rtnJson.put("iTotalDisplayRecords", rssInfoList.getPaginator().getTotalCount());
		rtnJson.put("aaData", jsonArray);
		HtmlUtil.writerJson(response, rtnJson);
	}

	@RequestMapping(value = "/rssOperate.htm")
	public ModelAndView rssOperate(Integer rssId, String method, HttpSession session) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("method", method);
		if (!"add".equals(method)) {
			ApiRssInfo rssInfo = rssService.queryRssInfoById(rssId);
			FtpUseInfo ftpInfo = FastJSONUtil.deserialize(rssInfo.getPushArg(), FtpUseInfo.class);
			rssInfo.setFtpUseInfo(ftpInfo);
			paramMap.put("startTime", new Date(rssInfo.getRssStartTime()));
			paramMap.put("endTime", new Date(rssInfo.getRssEndTime()));
			paramMap.put("rssInfo", rssInfo);
		}
		// 从session中获取用户信息
		UserInfo user = (UserInfo) session.getAttribute("userInfo");
		paramMap.put("appKey", user.getLoginAcct());
		return new ModelAndView("admin/rss/rssEdit", paramMap);
	}

	@RequestMapping(value = "/testIsConnection.json", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> testIsConnection(HttpServletRequest request, FtpUseInfo ftpInfo) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String appKey = StringUtils.defaultIfEmpty(LoadProperties.getProp("appKey"), "root");
		String appSecret = StringUtils.defaultIfEmpty(LoadProperties.getProp("appSecret"), "123");
		String invokeMethod = Constants.REQ_WAY_POST;
		ApiFormArg formArg = new ApiFormArg();
		formArg.setAppKey(appKey);
		formArg.setMethod("FtpConnTest");
		formArg.setFormat("json");
		formArg.setVersion("1");
		formArg.setCodeType("UTF-8");
		formArg.setFields("reqStat,retMsg,retCode,reqID");
		formArg.setReqType("syn");
		boolean isCon = rssService.callFtpApiData(ftpInfo, formArg, invokeMethod, appSecret);
		paramMap.put("isCon", isCon);
		return paramMap;
	}

	@RequestMapping(value = "/saveRssInfo.json", method = { RequestMethod.POST })
	@ResponseBody
	public Map<String, Object> saveRssInfo(HttpServletRequest request, ApiRssInfo rssInfo, FtpUseInfo ftpInfo, String method) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		String retCode = "";
		try {
			if (!"operate".equals(method)) {
				String startTime = request.getParameter("startTime");
				String endTime = request.getParameter("endTime");
				rssInfo.setRssStartTime(DateUtils.parseDate(startTime).getTime());
				rssInfo.setRssEndTime(DateUtils.parseDate(endTime).getTime());
				rssInfo.setFtpUseInfo(ftpInfo);
			}
			rssService.saveRssInfo(rssInfo, method);
			retCode = "00";
		}
		catch (Exception e) {
			logger.error(e.getMessage());
			retCode = "-1";
		}
		paramMap.put("retCode", retCode);
		return paramMap;
	}
}
