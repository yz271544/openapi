package com.teradata.portal.web.apiDisplay.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.teradata.portal.admin.document.pojo.BaseRet;
import com.teradata.portal.web.apiDisplay.service.ApiDisService;
import com.teradata.portal.web.document.pojo.Params;

@Controller
@RequestMapping(value = "/apiDisplay")
public class ApiDisplayController {
	
	@Autowired
	ApiDisService apiDisService;
	BaseRet br;
	
	@RequestMapping(value = "/apiDisplay.htm")
	public ModelAndView documentPublish(HttpSession httpSession, String apiSort, String apiId, String apiName, String apiVersion) {
		Map<String, String> args = new HashMap<String, String>();
		args.put("apiSort", apiSort);
		args.put("apiId", apiId);
		args.put("apiName", apiName);
		args.put("apiVersion", apiVersion);
		return new ModelAndView("portal/apiDisplay/apiDisplay", "params", args);
	}
	
	@RequestMapping(value = "/getApiBySortId.json")
	@ResponseBody
	public BaseRet getApiBySortId(Params param) {
		br = new BaseRet();
		try {
			br = apiDisService.getApisBySortId(param, br);
			br.setRetCode(true);
			br.setRetMsg("成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}
	
	@RequestMapping(value = "/getPubInfo.json")
	@ResponseBody
	public BaseRet getPubInfo() {
		
		br = new BaseRet();
		try {
			br = apiDisService.getPubInfo(br);
			br.setRetCode(true);
			br.setRetMsg("成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}
	
	@RequestMapping(value = "/searchApisByText.json")
	@ResponseBody
	public BaseRet searchApisByText(Params param) {
		br = new BaseRet();
		System.out.println("%%%%%%%%%%%%param : " + param.getSearchText());
		try {
			br = apiDisService.searchApisByText(param, br);
			br.setRetCode(true);
			br.setRetMsg("成功！");
			return br;
		} catch (Exception e) {
			e.printStackTrace();
			br.setRetCode(false);
			br.setRetMsg("系统错误！");
			return br;
		}
	}

}
