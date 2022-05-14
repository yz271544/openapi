package com.teradata.openapi.rop;

import org.springframework.util.StringUtils;

/**
 * 
 * 支持请求的格式类型 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月1日 下午4:46:42
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum RequestTypeFormat {

	syn, asyn;

	public static RequestTypeFormat getFormat(String value) {
		if (!StringUtils.hasText(value)) {
			return syn;
		} else {
			try {
				return RequestTypeFormat.valueOf(value.toLowerCase());
			}
			catch (IllegalArgumentException e) {
				return syn;
			}
		}
	}

	public static boolean isValidFormat(String value) {
		if (!StringUtils.hasText(value)) {
			return true;
		} else {
			try {
				RequestTypeFormat.valueOf(value.toLowerCase());
				return true;
			}
			catch (IllegalArgumentException e) {
				return false;
			}
		}
	}

}
