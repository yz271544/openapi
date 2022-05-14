package com.teradata.openapi.rop.config;

/**
 * 
 * 系统级参数(公共)的名称. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月1日 下午2:20:26
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class SystemParameterNames {

	// 方法的默认参数名
	private static final String METHOD = "method";

	// 格式化默认参数名
	private static final String FORMAT = "format";

	// 本地化默认参数名
	private static final String LOCALE = "locale";

	// 会话id默认参数名
	private static final String SESSION_ID = "sessionId";

	// 应用键的默认参数名 ;
	private static final String APP_KEY = "appKey";

	// 服务版本号的默认参数名
	private static final String VERSION = "version";

	// 签名的默认参数名
	private static final String SIGN = "sign";

	// 请求类型的默认参数名
	private static final String REQ_TYPE = "reqType";

	// 返回字符集默认参数名
	private static final String CODE_TYPE = "codeType";

	// 业务级响应参数
	private static final String FIELDS = "fields";

	private static final String JSONP = "callback";

	private static String method = METHOD;

	private static String format = FORMAT;

	private static String locale = LOCALE;

	private static String sessionId = SESSION_ID;

	private static String appKey = APP_KEY;

	private static String version = VERSION;

	private static String sign = SIGN;

	private static String reqType = REQ_TYPE;

	private static String codeType = CODE_TYPE;

	private static String fields = FIELDS;

	private static String jsonp = JSONP;

	public static String getMethod() {
		return method;
	}

	public static void setMethod(String method) {
		SystemParameterNames.method = method;
	}

	public static String getFormat() {
		return format;
	}

	public static void setFormat(String format) {
		SystemParameterNames.format = format;
	}

	public static String getLocale() {
		return locale;
	}

	public static void setLocale(String locale) {
		SystemParameterNames.locale = locale;
	}

	public static String getSessionId() {
		return sessionId;
	}

	public static void setSessionId(String sessionId) {
		SystemParameterNames.sessionId = sessionId;
	}

	public static String getAppKey() {
		return appKey;
	}

	public static void setAppKey(String appKey) {
		SystemParameterNames.appKey = appKey;
	}

	public static String getVersion() {
		return version;
	}

	public static void setVersion(String version) {
		SystemParameterNames.version = version;
	}

	public static String getSign() {
		return sign;
	}

	public static void setSign(String sign) {
		SystemParameterNames.sign = sign;
	}

	public static String getReqType() {
		return reqType;
	}

	public static void setReqType(String reqType) {
		SystemParameterNames.reqType = reqType;
	}

	public static String getCodeType() {
		return codeType;
	}

	public static void setCodeType(String codeType) {
		SystemParameterNames.codeType = codeType;
	}

	public static String getJsonp() {
		return jsonp;
	}

	public static void setJsonp(String jsonp) {
		SystemParameterNames.jsonp = jsonp;
	}

	public static String getFields() {
		return fields;
	}

	public static void setFields(String fields) {
		SystemParameterNames.fields = fields;
	}

	public static String getSystemParamsName() {
		return method + "," + format + "," + locale + "," + appKey + "," + version + "," + sign + "," + reqType + "," + codeType + "," + fields;
	}

}
