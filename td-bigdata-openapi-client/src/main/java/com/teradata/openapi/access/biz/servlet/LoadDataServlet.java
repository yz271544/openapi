package com.teradata.openapi.access.biz.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import scala.concurrent.Await;
import scala.concurrent.Promise;
import scala.concurrent.duration.Duration;
import scala.concurrent.impl.Promise.DefaultPromise;
import akka.actor.ActorRef;
import akka.util.Timeout;

import com.teradata.openapi.access.biz.bean.ResponseResult;
import com.teradata.openapi.access.core.ext.PromiseIdReal;
import com.teradata.openapi.framework.deploy.MetaTableInfoCollectResult;
import com.teradata.openapi.framework.deploy.RefreshApiMetaInfo;
import com.teradata.openapi.framework.deploy.RefreshApiMetaInfoResult;
import com.teradata.openapi.framework.deploy.WebSearchMetaTableInfo;
import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.framework.util.SpringContextUtil;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.RopMarshaller;
import com.teradata.openapi.rop.marshaller.JacksonJsonRopMarshaller;
import com.teradata.openapi.rop.utils.RopUtils;
import com.xiaoleilu.hutool.http.HttpUtil;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

public class LoadDataServlet extends HttpServlet {

	/** serialVersionUID */
	private static final long serialVersionUID = -2318352516012144575L;

	public static final String ACCESS_CONTROL_ALLOW_ORIGIN = "Access-Control-Allow-Origin";

	public static final String ACCESS_CONTROL_ALLOW_METHODS = "Access-Control-Allow-Methods";

	private final static Log logger = StaticLog.get();

	@Override
	public void init(ServletConfig servletConfig) throws ServletException {

	}

	@Override
	@SuppressWarnings(value = { "unchecked", "rawtypes" })
	protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		String refreshFlag = req.getParameter("refFlag");
		ResponseResult collectResult = new ResponseResult();
		if (refreshFlag == null) {
			collectResult.setRetCode("1");
			collectResult.setRetMsg("缺少refFlag请求【value:1 表示刷新appSecret;value:2表示刷新apiService和API后台元数据;value:3表示刷新accessCache;value:4表示刷新表的元数据】");
		} else {
			ActorRef dataRef = (ActorRef) SpringContextUtil.getBean("refreshDataActor");
			Timeout timeout = new Timeout(Duration.create(Constants.AKKA_TIME_OUT, TimeUnit.SECONDS));
			if ("4".equals(refreshFlag)) {
				String sourceId = req.getParameter("sourceId");
				String schemaName = req.getParameter("schemaName");
				String tabName = req.getParameter("tabName");
				logger.info("调用akka WebSearchMetaTableInfo");
				String reqId = RopUtils.getUUID();
				WebSearchMetaTableInfo metaInfo = new WebSearchMetaTableInfo(reqId, Integer.parseInt(sourceId), schemaName, tabName);
				dataRef.tell(metaInfo, ActorRef.noSender());
				Promise pro = new DefaultPromise();
				Map<String, Promise> proMap = PromiseIdReal.getInstance();
				proMap.put(reqId, pro);
				try {
					MetaTableInfoCollectResult metaResult = (MetaTableInfoCollectResult) Await.result(pro.future(), timeout.duration());
					collectResult.setRetCode(metaResult.result() + "");
				}
				catch (Exception e) {
					collectResult.setRetCode("1");
					collectResult.setRetMsg("Error:" + e.getMessage());
				}
			} else {
				// 针对多台部署的应用缓存处理
				String clientIps = LoadProperties.getProp("clientIps", "configure.properties");
				String[] ipArry = clientIps.split(Constants.SPLIT_SIGN);
				Map<String, Object> paramMap = new HashMap<String, Object>();
				paramMap.put("refFlag", refreshFlag);
				collectResult.setRetCode("0");
				String tempResult = "";
				for (String ip : ipArry) {
					try {
						tempResult += HttpUtil.post("http://" + ip + "/restserver/refresh", paramMap);

					}
					catch (Exception e) {
						tempResult += ip + ":Error:" + e.getMessage();
					}
				}
				if ("2".equals(refreshFlag)) {
					String apiId = req.getParameter("apiId");
					String apiVersion = req.getParameter("apiVersion");
					logger.info("调用akka RefreshApiMetaInfo");
					String reqId = RopUtils.getUUID();
					RefreshApiMetaInfo metaInfo = new RefreshApiMetaInfo(reqId, Integer.parseInt(apiId), Integer.parseInt(apiVersion));
					dataRef.tell(metaInfo, ActorRef.noSender());
					Promise pro = new DefaultPromise();
					Map<String, Promise> proMap = PromiseIdReal.getInstance();
					proMap.put(reqId, pro);
					try {
						RefreshApiMetaInfoResult metaResult = (RefreshApiMetaInfoResult) Await.result(pro.future(), timeout.duration());
						tempResult += "RefreshApiMetaInfo result:" + metaResult.result();
					}
					catch (Exception e) {
						tempResult += "RefreshApiMetaInfo result :Error:" + e.getMessage();
					}
				}
				collectResult.setSubRetMsg(tempResult);
			}
		}
		resp.addHeader(ACCESS_CONTROL_ALLOW_ORIGIN, "*");
		resp.addHeader(ACCESS_CONTROL_ALLOW_METHODS, "*");
		resp.setCharacterEncoding("utf-8");
		resp.setContentType("application/json");
		RopMarshaller ropMarshaller = new JacksonJsonRopMarshaller();

		ropMarshaller.marshaller(collectResult, resp.getOutputStream());
		resp.getOutputStream().flush();
		resp.getOutputStream().close();
	}
}
