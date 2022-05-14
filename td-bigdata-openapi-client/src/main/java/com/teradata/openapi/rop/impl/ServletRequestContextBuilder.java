package com.teradata.openapi.rop.impl;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.propertyeditors.LocaleEditor;
import org.springframework.format.support.FormattingConversionService;
import org.springframework.util.StringUtils;

import com.teradata.openapi.rop.AbstractRopRequest;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.MessageFormat;
import com.teradata.openapi.rop.RequestContextBuilder;
import com.teradata.openapi.rop.RequestTypeFormat;
import com.teradata.openapi.rop.RopContext;
import com.teradata.openapi.rop.RopRequest;
import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.annotation.HttpAction;
import com.teradata.openapi.rop.config.SystemParameterNames;
import com.teradata.openapi.rop.security.MainErrorType;
import com.teradata.openapi.rop.security.MainErrors;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 
 * 构建{RopRequestContext}实例 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年5月24日 上午10:03:47
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class ServletRequestContextBuilder implements RequestContextBuilder {

	private final static Log logger = StaticLog.get();

	// 默认的{@link RopRequest}实现类
	private class DefaultRopRequest extends AbstractRopRequest {}

	public static final String X_FORWARDED_FOR = "X-Forwarded-For";

	// 通过前端的负载均衡服务器时，请求对象中的IP会变成负载均衡服务器的IP，因此需要特殊处理，下同。
	public static final String X_REAL_IP = "X-Real-IP";

	private FormattingConversionService conversionService;

	public static Locale getLocale(HttpServletRequest webRequest) {
		if (webRequest.getParameter(SystemParameterNames.getLocale()) != null) {
			try {
				LocaleEditor localeEditor = new LocaleEditor();
				localeEditor.setAsText(webRequest.getParameter(SystemParameterNames.getLocale()));
				Locale locale = (Locale) localeEditor.getValue();
				if (isValidLocale(locale)) {
					return locale;
				}
			}
			catch (Exception e) {
				return Locale.SIMPLIFIED_CHINESE;
			}
		}
		return Locale.SIMPLIFIED_CHINESE;
	}

	public static MessageFormat getResponseFormat(HttpServletRequest servletRequest) {
		String messageFormat = servletRequest.getParameter(SystemParameterNames.getFormat());
		if (MessageFormat.isValidFormat(messageFormat)) {
			return MessageFormat.getFormat(messageFormat);
		} else {
			return MessageFormat.json;
		}
	}

	public static RequestTypeFormat getReqTypeFormat(HttpServletRequest servletRequest) {
		String reqTypeFormat = servletRequest.getParameter(SystemParameterNames.getReqType());
		if (RequestTypeFormat.isValidFormat(reqTypeFormat)) {
			return RequestTypeFormat.getFormat(reqTypeFormat);
		} else {
			return RequestTypeFormat.syn;
		}
	}

	private static boolean isValidLocale(Locale locale) {
		if (Locale.SIMPLIFIED_CHINESE.equals(locale) || Locale.ENGLISH.equals(locale)) {
			return true;
		} else {
			try {
				// check error resource file exists
				MainErrors.getError(MainErrorType.INVALID_APP_KEY, locale);
			}
			catch (Exception e) {
				return false;
			}
			return true;
		}
	}

	public ServletRequestContextBuilder(FormattingConversionService conversionService) {
		this.conversionService = conversionService;
	}

	@Override
	public SimpleRopRequestContext buildBySysParams(RopContext ropContext, Object request, Object response) {
		if (!(request instanceof HttpServletRequest)) {
			throw new IllegalArgumentException("请求对象必须是HttpServletRequest的类型");
		}
		if (response != null && !(response instanceof HttpServletResponse)) {
			throw new IllegalArgumentException("请求对象必须是HttpServletResponse的类型");
		}

		HttpServletRequest servletRequest = (HttpServletRequest) request;
		SimpleRopRequestContext requestContext = new SimpleRopRequestContext(ropContext);

		// 设置请求对象及参数列表
		requestContext.setRawRequestObject(servletRequest);
		if (response != null) {
			requestContext.setRawResponseObject(response);
		}
		requestContext.setAllParams(getRequestParams(servletRequest));
		requestContext.setIp(getRemoteAddr(servletRequest));

		// 设置服务的系统级参数
		requestContext.setAppKey(servletRequest.getParameter(SystemParameterNames.getAppKey()));
		requestContext.setSessionId(servletRequest.getParameter(SystemParameterNames.getSessionId()));
		requestContext.setMethod(servletRequest.getParameter(SystemParameterNames.getMethod()));
		requestContext.setVersion(servletRequest.getParameter(SystemParameterNames.getVersion()));
		requestContext.setLocale(getLocale(servletRequest));
		requestContext.setFormat(getFormat(servletRequest));
		requestContext.setMessageFormat(getResponseFormat(servletRequest));
		requestContext.setSign(servletRequest.getParameter(SystemParameterNames.getSign()));
		requestContext.setReqType(servletRequest.getParameter(SystemParameterNames.getReqType()));
		requestContext.setCodeType(getCodeTypeFormat(servletRequest));
		requestContext.setRequestTypeFormat(getReqTypeFormat(servletRequest));
		requestContext.setHttpAction(HttpAction.fromValue(servletRequest.getMethod()));

		// 设置服务的业务级响应参数
		requestContext.setFields(servletRequest.getParameter(SystemParameterNames.getFields()));
		// 设置服务的业务级请求参数
		requestContext.setReqParams(getBusiReqParams(servletRequest));

		logger.info("{}|http://" + requestContext.getIp() + ":" + servletRequest.getLocalPort() + "/" + requestContext.toString(),
		            requestContext.getRequestId());
		return requestContext;
	}

	/**
	 * 将{@link HttpServletRequest}的数据绑定到{@link com.rop.RopRequestContext}的 {@link com.rop.RopRequest}中，同时使用 JSR 303对请求数据进行校验，将错误信息设置到
	 * {@link com.rop.RopRequestContext}的属性列表中。
	 * 
	 * @param ropRequestContext
	 */

	@Override
	public RopRequest buildRopRequest(RopRequestContext ropRequestContext) {
		AbstractRopRequest ropRequest = new DefaultRopRequest();
		ropRequest.setRopRequestContext(ropRequestContext);
		return ropRequest;
	}

	private String getFormat(HttpServletRequest servletRequest) {
		String messageFormat = servletRequest.getParameter(SystemParameterNames.getFormat());
		// 默认 返回json类型
		if (messageFormat == null) {
			return MessageFormat.json.name();
		} else {
			return messageFormat;
		}
	}

	private String getCodeTypeFormat(HttpServletRequest servletRequest) {
		String codeTypeFormat = servletRequest.getParameter(SystemParameterNames.getCodeType());
		// 默认 类型是UTF-8
		if (codeTypeFormat == null) {
			return Constants.UTF8;
		} else {
			return codeTypeFormat;
		}
	}

	public FormattingConversionService getFormattingConversionService() {
		return conversionService;
	}

	private String getRemoteAddr(HttpServletRequest request) {
		String remoteIp = request.getHeader(X_REAL_IP); // nginx反向代理
		if (StringUtils.hasText(remoteIp)) {
			return remoteIp;
		} else {
			remoteIp = request.getHeader(X_FORWARDED_FOR);// apache反射代理
			if (StringUtils.hasText(remoteIp)) {
				String[] ips = remoteIp.split(",");
				for (String ip : ips) {
					if (!"null".equalsIgnoreCase(ip)) {
						return ip;
					}
				}
			}
			return request.getRemoteAddr();
		}
	}

	private Map<String, String> getRequestParams(HttpServletRequest request) {
		Map srcParamMap = request.getParameterMap();
		Map<String, String> destParamMap = new HashMap<String, String>(srcParamMap.size());
		for (Object obj : srcParamMap.keySet()) {
			String[] values = (String[]) srcParamMap.get(obj);
			if (values != null && values.length > 0) {
				destParamMap.put((String) obj, values[0]);
			} else {
				destParamMap.put((String) obj, null);
			}
		}
		return destParamMap;
	}

	/**
	 * 获取业务级请求参数
	 * 
	 * @param request
	 * @return
	 * @date 2016年5月16日
	 * @author houbl
	 */
	private Map<String, String> getBusiReqParams(HttpServletRequest request) {
		Map<String, String> allParams = getRequestParams(request);
		String[] removeParams = SystemParameterNames.getSystemParamsName().split(Constants.SPLIT_SIGN);
		for (String removeParam : removeParams) {
			allParams.remove(removeParam);
		}
		return allParams;
	}
}
