package com.teradata.portal.web.dashboard.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/**
 * 
 * 首页控制器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年5月31日 下午4:53:53
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
@Controller
@RequestMapping(value = "/dashboard")
public class DashBoardController {

	/**
	 * 首页
	 * 
	 * @return
	 */
	@RequestMapping(value = "/index.htm")
	public ModelAndView dashboardIndex(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("index");
	}

}
