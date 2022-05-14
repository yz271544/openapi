package com.teradata.openapi.rop.security;

import java.util.EnumMap;

/**
 * 
 * 错误类型 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月1日 下午2:45:25
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum MainErrorType {
	BUSINESS_LOGIC_ERROR, EXCEED_APP_INVOKE_FREQUENCY_LIMITED, EXCEED_APP_INVOKE_LIMITED, EXCEED_SESSION_INVOKE_LIMITED, EXCEED_USER_INVOKE_LIMITED, FORBIDDEN_REQUEST, HTTP_ACTION_NOT_ALLOWED, INSUFFICIENT_ISV_PERMISSIONS, INSUFFICIENT_USER_PERMISSIONS, INVALID_APP_KEY, INVALID_ARGUMENTS, INVALID_ENCODING, INVALID_FORMAT, INVALID_METHOD, INVALID_SESSION, INVALID_SIGNATURE, INVALID_VERSION, METHOD_OBSOLETED, MISSING_APP_KEY, MISSING_METHOD, MISSING_REQUIRED_ARGUMENTS, MISSING_SESSION, MISSING_REQPARAM, INVALID_REQTYPE_FORMAT, INVALID_CODETYPE_FORMAT, MISSING_SIGNATURE, MISSING_VERSION, SERVICE_CURRENTLY_UNAVAILABLE, UNSUPPORTED_VERSION, UPLOAD_FAIL, INVALID_REQPARAM_VALUE_FORMAT, MISSING_SELECT_MUSTONE, INVALID_REPPARAM_VALUE_FORMAT, INVALID_BEYOND_LIMIT, EXCEED_REQTYPE_LIMIT;

	private static EnumMap<MainErrorType, String> errorCodeMap = new EnumMap<MainErrorType, String>(MainErrorType.class);

	static {
		errorCodeMap.put(MainErrorType.SERVICE_CURRENTLY_UNAVAILABLE, "1");
		errorCodeMap.put(MainErrorType.INSUFFICIENT_ISV_PERMISSIONS, "2");
		errorCodeMap.put(MainErrorType.INSUFFICIENT_USER_PERMISSIONS, "3");
		errorCodeMap.put(MainErrorType.UPLOAD_FAIL, "4");
		errorCodeMap.put(MainErrorType.HTTP_ACTION_NOT_ALLOWED, "5");
		errorCodeMap.put(MainErrorType.INVALID_ENCODING, "6");
		errorCodeMap.put(MainErrorType.FORBIDDEN_REQUEST, "7");
		errorCodeMap.put(MainErrorType.METHOD_OBSOLETED, "8");
		errorCodeMap.put(MainErrorType.BUSINESS_LOGIC_ERROR, "9");
		errorCodeMap.put(MainErrorType.MISSING_SESSION, "20");
		errorCodeMap.put(MainErrorType.INVALID_SESSION, "21");
		errorCodeMap.put(MainErrorType.MISSING_APP_KEY, "22");
		errorCodeMap.put(MainErrorType.INVALID_APP_KEY, "23");
		errorCodeMap.put(MainErrorType.MISSING_SIGNATURE, "24");
		errorCodeMap.put(MainErrorType.INVALID_SIGNATURE, "25");
		errorCodeMap.put(MainErrorType.MISSING_METHOD, "26");
		errorCodeMap.put(MainErrorType.INVALID_METHOD, "27");
		errorCodeMap.put(MainErrorType.MISSING_VERSION, "28");
		errorCodeMap.put(MainErrorType.INVALID_VERSION, "29");
		errorCodeMap.put(MainErrorType.UNSUPPORTED_VERSION, "30");
		errorCodeMap.put(MainErrorType.INVALID_FORMAT, "31");
		errorCodeMap.put(MainErrorType.MISSING_REQUIRED_ARGUMENTS, "32");
		errorCodeMap.put(MainErrorType.INVALID_ARGUMENTS, "33");
		errorCodeMap.put(MainErrorType.EXCEED_USER_INVOKE_LIMITED, "34");
		errorCodeMap.put(MainErrorType.EXCEED_SESSION_INVOKE_LIMITED, "35");
		errorCodeMap.put(MainErrorType.EXCEED_APP_INVOKE_LIMITED, "36");
		errorCodeMap.put(MainErrorType.EXCEED_APP_INVOKE_FREQUENCY_LIMITED, "37");
		errorCodeMap.put(MainErrorType.MISSING_REQPARAM, "38");
		errorCodeMap.put(MainErrorType.INVALID_REQTYPE_FORMAT, "39");
		errorCodeMap.put(MainErrorType.INVALID_CODETYPE_FORMAT, "40");
		errorCodeMap.put(MainErrorType.INVALID_REQPARAM_VALUE_FORMAT, "41");
		errorCodeMap.put(MainErrorType.MISSING_SELECT_MUSTONE, "42");
		errorCodeMap.put(MainErrorType.INVALID_REPPARAM_VALUE_FORMAT, "43");
		errorCodeMap.put(MainErrorType.INVALID_BEYOND_LIMIT, "44");
		errorCodeMap.put(MainErrorType.EXCEED_REQTYPE_LIMIT, "45");

	}

	public String value() {
		return errorCodeMap.get(this);
	}
}
