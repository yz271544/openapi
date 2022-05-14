package com.teradata.openapi.access.biz.servlet;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.teradata.openapi.access.core.AccessCacheFactory;
import com.teradata.openapi.access.core.ApiAppSecretManager;
import com.teradata.openapi.access.core.ApiServiceAccessController;
import com.teradata.openapi.rop.RopMarshaller;
import com.teradata.openapi.rop.cache.CacheManager;
import com.teradata.openapi.rop.marshaller.JacksonJsonRopMarshaller;

public class RefreshCacheServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = -2318352516012144575L;

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

	private CacheManager cacheManager;

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {

	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String refreshFlag = req.getParameter("refFlag");
		String retMsg = "";
		if (refreshFlag == null) {
			retMsg = "缺少refFlag请求【value:1 表示刷新appSecret;value:2表示清除apiService;value:3表示清除accessCache】";
		} else {
			if ("1".equals(refreshFlag)) {
				cacheManager = new ApiAppSecretManager();
				retMsg = "刷新appSecret成功！";
			} else if ("2".equals(refreshFlag)) {
				cacheManager = new ApiServiceAccessController();
				retMsg = "刷新apiService成功！";
			} else if ("3".equals(refreshFlag)) {
				cacheManager = new AccessCacheFactory();
				retMsg = "刷新accessCache成功！";
			}
			cacheManager.refreshCache();
		}
		resp.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		resp.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		RopMarshaller ropMarshaller = new JacksonJsonRopMarshaller();

		ropMarshaller.marshaller(retMsg, resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
	}

}
