package com.teradata.portal.web.system.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.teradata.portal.admin.auth.entity.UserInfo;

public class LoginAuthInterceptor extends HandlerInterceptorAdapter {

	private static Logger log = LoggerFactory.getLogger(LoginAuthInterceptor.class);

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		String requestPath = request.getContextPath();// 用户访问的资源地址
		log.debug("请求路径：" + requestPath);
		System.out.println("请求路径：" + requestPath);
		HttpSession session = request.getSession();
		UserInfo userInfo = (UserInfo) session.getAttribute("userInfo");
		if (userInfo == null) {
			log.debug("无用户信息");
			request.getRequestDispatcher("/login.htm").forward(
                    request, response);
		}
		return super.preHandle(request, response, handler);
	}
}
