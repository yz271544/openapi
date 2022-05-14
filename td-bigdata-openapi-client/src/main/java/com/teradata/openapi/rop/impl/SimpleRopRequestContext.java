package com.teradata.openapi.rop.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.MessageFormat;
import com.teradata.openapi.rop.RequestTypeFormat;
import com.teradata.openapi.rop.RopContext;
import com.teradata.openapi.rop.RopRequest;
import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.ServiceMethodDefinition;
import com.teradata.openapi.rop.ServiceMethodHandler;
import com.teradata.openapi.rop.annotation.HttpAction;
import com.teradata.openapi.rop.security.MainError;
import com.teradata.openapi.rop.session.Session;
import com.teradata.openapi.rop.utils.RopUtils;

/**
 * 
 * 请求实体 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月1日 下午3:06:32
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class SimpleRopRequestContext implements RopRequestContext {

	public static ThreadLocal<MessageFormat> messageFormat = new ThreadLocal<MessageFormat>();

	public static ThreadLocal<RequestTypeFormat> requestTypeFormat = new ThreadLocal<RequestTypeFormat>();

	public static final String SPRING_VALIDATE_ERROR_ATTRNAME = "$SPRING_VALIDATE_ERROR_ATTRNAME";

	private Map<String, String> allParams;

	private String appKey;

	private Map<String, Object> attributes = new HashMap<String, Object>();

	private String format;

	private HttpAction httpAction;

	private String ip;

	private Locale locale;

	private MainError mainError;

	private String method;

	private Object rawRequestObject;

	private Object rawResponseObject;

	private String requestId = RopUtils.getUUID();

	private RopContext ropContext;

	private RopRequest ropRequest;

	private Object ropResponse;

	private long serviceBeginTime = -1;

	private long serviceEndTime = -1;

	private ServiceMethodHandler serviceMethodHandler;

	private Session session;

	private String sessionId;

	private String sign;

	private String version;

	private String reqType;// 请求类型

	private String codeType;// 编码格式 UTF-8(默认) GBK

	private String fields;// 业务级响应参数

	private Map<String, String> reqParams;// 业务级请求参数

	public SimpleRopRequestContext(RopContext ropContext) {
		this.ropContext = ropContext;
		this.serviceBeginTime = System.currentTimeMillis();
	}

	@Override
	public void addSession(String sessionId, Session session) {
		this.sessionId = sessionId;
		this.session = session;
		if (ropContext != null && ropContext.getSessionManager() != null) {
			ropContext.getSessionManager().addSession(sessionId, session);
		}
	}

	@Override
	public Map<String, String> getAllParams() {
		return this.allParams;
	}

	@Override
	public String getAppKey() {
		return this.appKey;
	}

	@Override
	public Object getAttribute(String name) {
		return this.attributes.get(name);
	}

	public Map<String, Object> getAttributes() {
		return attributes;
	}

	@Override
	public String getFormat() {
		return this.format;
	}

	@Override
	public HttpAction getHttpAction() {
		return this.httpAction;
	}

	@Override
	public String getIp() {
		return this.ip;
	}

	@Override
	public Locale getLocale() {
		return this.locale;
	}

	public MainError getMainError() {
		return this.mainError;
	}

	@Override
	public MessageFormat getMessageFormat() {
		return messageFormat.get();
	}

	@Override
	public String getMethod() {
		return this.method;
	}

	@Override
	public String getParamValue(String paramName) {
		if (allParams != null) {
			return allParams.get(paramName);
		} else {
			return null;
		}
	}

	@Override
	public Object getRawRequestObject() {
		return this.rawRequestObject;
	}

	@Override
	public Object getRawResponseObject() {
		return this.rawResponseObject;
	}

	@Override
	public String getRequestId() {
		return this.requestId;
	}

	@Override
	public RopContext getRopContext() {
		return ropContext;
	}

	@Override
	public Object getRopResponse() {
		return this.ropResponse;
	}

	@Override
	public long getServiceBeginTime() {
		return this.serviceBeginTime;
	}

	@Override
	public long getServiceEndTime() {
		return this.serviceEndTime;
	}

	@Override
	public ServiceMethodDefinition getServiceMethodDefinition() {
		return serviceMethodHandler.getServiceMethodDefinition();
	}

	@Override
	public ServiceMethodHandler getServiceMethodHandler() {
		return this.serviceMethodHandler;
	}

	@Override
	public Session getSession() {
		if (session == null && ropContext != null && ropContext.getSessionManager() != null && getSessionId() != null) {
			session = ropContext.getSessionManager().getSession(getSessionId());
		}
		return session;
	}

	@Override
	public String getSessionId() {
		return this.sessionId;
	}

	@Override
	public String getSign() {
		return sign;
	}

	@Override
	public String getVersion() {
		return version;
	}

	@Override
	public boolean isSignEnable() {
		return ropContext.isSignEnable();
	}

	@Override
	public void removeSession() {
		if (ropContext != null && ropContext.getSessionManager() != null) {
			ropContext.getSessionManager().removeSession(getSessionId());
		}
	}

	@Override
	public String getFields() {
		return fields;
	}

	public void setAllParams(Map<String, String> allParams) {
		this.allParams = allParams;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	@Override
	public void setAttribute(String name, Object value) {
		this.attributes.put(name, value);
	}

	public void setAttributes(Map<String, Object> attributes) {
		this.attributes = attributes;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public void setHttpAction(HttpAction httpAction) {
		this.httpAction = httpAction;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public void setMainError(MainError mainError) {
		this.mainError = mainError;
	}

	public void setMessageFormat(MessageFormat messageFormat) {
		this.messageFormat.set(messageFormat);
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setRawRequestObject(Object rawRequestObject) {
		this.rawRequestObject = rawRequestObject;
	}

	public void setRawResponseObject(Object rawResponseObject) {
		this.rawResponseObject = rawResponseObject;
	}

	@Override
	public void setRopResponse(Object ropResponse) {
		this.ropResponse = ropResponse;
	}

	@Override
	public void setServiceBeginTime(long serviceBeginTime) {
		this.serviceBeginTime = serviceBeginTime;
	}

	@Override
	public void setServiceEndTime(long serviceEndTime) {
		this.serviceEndTime = serviceEndTime;
	}

	public void setServiceMethodHandler(ServiceMethodHandler serviceMethodHandler) {
		this.serviceMethodHandler = serviceMethodHandler;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public void setSign(String sign) {
		this.sign = sign;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	@Override
	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	@Override
	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	@Override
	public Map<String, String> getReqParams() {
		return reqParams;
	}

	public void setReqParams(Map<String, String> reqParams) {
		this.reqParams = reqParams;
	}

	@Override
	public RequestTypeFormat getRequestTypeFormat() {
		return requestTypeFormat.get();
	}

	public void setRequestTypeFormat(RequestTypeFormat requestTypeFormat) {
		this.requestTypeFormat.set(requestTypeFormat);
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	@Override
	public String toString() {
		StringBuffer buf = new StringBuffer(500);
		buf.append("restserver/router?");
		Map<String, String> allParams = getAllParams();
		int i = 0;
		for (String key : allParams.keySet()) {
			buf.append(key + "=" + allParams.get(key));
			if (i != allParams.size() - 1) {
				buf.append("&");
			}
			i++;
		}
		return buf.toString();
	}

	@Override
	public boolean isSysAppKey() {
		if (Constants.SYS_DEFAULT_APP_KEY.equals(appKey)) {
			return true;
		} else {
			return false;
		}

	}

}
