package com.teradata.openapi.rop.annotation;

/**
 * 
 * 请求类型的方法 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午5:36:31
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum HttpAction {

	GET, POST;

	public static HttpAction fromValue(String value) {
		if (GET.name().equalsIgnoreCase(value)) {
			return GET;
		} else if (POST.name().equalsIgnoreCase(value)) {
			return POST;
		} else {
			return POST;
		}
	}
}
