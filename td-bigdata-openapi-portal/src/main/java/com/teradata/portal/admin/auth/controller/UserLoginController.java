package com.teradata.portal.admin.auth.controller;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.PropertyUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.auth.entity.ResourceInfo;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.service.ApiInfoService;
import com.teradata.portal.admin.auth.service.ResourceInfoService;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import com.teradata.portal.admin.auth.service.UserInfoService;
import com.teradata.portal.admin.auth.util.TreeUtil;
import com.teradata.portal.admin.auth.vo.SysMenuInfoTreeObject;

/**
 * Created by John on 2016/7/25.
 */
@Controller
public class UserLoginController extends BaseController {

	@Inject
	private ApiInfoService apiInfoService;

	@Inject
	private ResourceInfoService resourceInfoService;

	@Inject
	private UserGrpInfoService userGrpInfoService;

	@Inject
	private UserInfoService userInfoService;

	@RequestMapping(value = "/index")
	public ModelAndView IndexPage(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("index");
	}

	@RequestMapping(value = "/loginByAcct", method = RequestMethod.POST)
	public ModelAndView loginByAcct(@RequestParam("username") String loginAcct, @RequestParam("password") String loginPwd, HttpSession httpSession) {

		ModelAndView mav = new ModelAndView();

		HttpHeaders responseHeaders = new HttpHeaders();
		MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
		responseHeaders.setContentType(mediaType);

		ResponseEntity<String> re = null;
		String returns = "";

		System.out.println("loginAcct:" + loginAcct + " loginPwd:" + loginPwd);
		UserInfo user = userInfoService.isExist(loginAcct);
		List<ResourceInfo> resourceInfoList = null;
		if (user == null) {  //用户不存在
			returns = new String("1");
			// httpSession.setAttribute("verifyInfo",returns);
			// mav.addObject("verifyInfo",returns);
			mav.setViewName("redirect:/login?verify=" + returns);
		} else {
			if (loginPwd.equals(user.getLoginPwd()) && "1".equals(user.getUserStat())) {
				//用户状态正确且登录密码正确
				resourceInfoList = resourceInfoService.findResInfoByLoginAcct(user.getLoginAcct());
				List<SysMenuInfoTreeObject> treeObjects = new ArrayList<SysMenuInfoTreeObject>();
				for (ResourceInfo resinfo : resourceInfoList) {
					SysMenuInfoTreeObject t = new SysMenuInfoTreeObject();
					try {
						PropertyUtils.copyProperties(t, resinfo);
						treeObjects.add(t);
					}
					catch (Exception e) {
						e.printStackTrace();
					}
				}
				TreeUtil<SysMenuInfoTreeObject> tu = new TreeUtil<SysMenuInfoTreeObject>();
				List<SysMenuInfoTreeObject> ns = tu.getChildResources(treeObjects, 2);
				String str = JSON.toJSONString(ns);
				String userJson = JSON.toJSONString(user);

				// 获取用户和用户组关系对象
				UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(user.getUserId());
				List<Integer> list = new ArrayList<Integer>();
				list.add(userGrpInfo.getId());
				list.add(user.getUserId());
				Map<String, Object> map = new HashMap<String, Object>();
				map.put("rightMainVals", list);
				List<ApiInfo> apiInfoList = apiInfoService.findApiAuthByIdMaps(map);

				// System.out.println("===========>"+str);

				UserGrpInfo ug = userGrpInfoService.findbyUserGrpRelat(user.getUserId());

				httpSession.setAttribute("userInfo", user);
				httpSession.setAttribute("userGrpId", ug.getId());
				httpSession.setAttribute("userJson", userJson);
				httpSession.setAttribute("resourceInfoList", resourceInfoList);
				httpSession.setAttribute("apiInfoList", apiInfoList);
				httpSession.setAttribute("resourceTree", str);
				mav.setViewName("redirect:/home/goHome.htm");
			} else {
				//用户状态异常或登录密码不正确
				returns = new String("2");
				// httpSession.setAttribute("verifyInfo",returns);
				// mav.addObject("verifyInfo",returns);
				mav.setViewName("redirect:/login?verify=" + returns);
			}
		}
		// re = new ResponseEntity<String>(returns, responseHeaders,
		// HttpStatus.OK);
		return mav;
	}

	@RequestMapping(value = "/login")
	public ModelAndView LoginPage(HttpServletRequest request, HttpSession httpSession) {
		return new ModelAndView("login");
	}

	@RequestMapping(value = "/logout")
	public ModelAndView logout(HttpSession httpSession) {
		httpSession.invalidate();
		ModelAndView mav = new ModelAndView();
		mav.setViewName("redirect:/dashboard/index.htm");
		return mav;
	}
}