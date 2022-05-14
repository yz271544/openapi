package com.teradata.openapi.rop.config;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.xml.BeanDefinitionParser;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * 
 * 指定自定义的系统参数名 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月1日 下午2:27:56
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class SystemParameterNamesBeanDefinitionParser implements BeanDefinitionParser {

	@Override
	public BeanDefinition parse(Element element, ParserContext parserContext) {
		String appKey = element.getAttribute("appkey-param-name");
		String sessionId = element.getAttribute("sessionid-param-name");
		String method = element.getAttribute("method-param-name");
		String version = element.getAttribute("version-param-name");
		String format = element.getAttribute("format-param-name");
		String locale = element.getAttribute("locale-param-name");
		String sign = element.getAttribute("sign-param-name");
		String reqType = element.getAttribute("reqType-param-name");
		String jsonp = element.getAttribute("jsonp-param-name");

		if (StringUtils.hasText(appKey)) {
			SystemParameterNames.setAppKey(appKey);
		}
		if (StringUtils.hasText(sessionId)) {
			SystemParameterNames.setSessionId(sessionId);
		}
		if (StringUtils.hasText(method)) {
			SystemParameterNames.setMethod(method);
		}
		if (StringUtils.hasText(version)) {
			SystemParameterNames.setVersion(version);
		}
		if (StringUtils.hasText(format)) {
			SystemParameterNames.setFormat(format);
		}
		if (StringUtils.hasText(locale)) {
			SystemParameterNames.setLocale(locale);
		}
		if (StringUtils.hasText(sessionId)) {
			SystemParameterNames.setSign(sign);
		}
		if (StringUtils.hasText(reqType)) {
			SystemParameterNames.setReqType(reqType);
		}
		if (StringUtils.hasText(jsonp)) {
			SystemParameterNames.setJsonp(jsonp);
		}
		return null;
	}
}
