package com.teradata.openapi.framework.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

public class LoadProperties {

	public static String getDataBaseScheme(String key) {

		String dataBaseSchemeName = "";
		InputStream inputStream = null;
		Properties p = new Properties();
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream("system.properties");
			p.load(inputStream);
			dataBaseSchemeName = p.getProperty(key);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return dataBaseSchemeName;

	}

	public static String getApplicationProp(String key) {
		String value = "";
		InputStream inputStream = null;
		Properties p = new Properties();
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream("application.properties");
			p.load(inputStream);
			value = p.getProperty(key);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return value;
	}

	public static String getProp(String key) {
		String value = "";
		InputStream inputStream = null;
		Properties p = new Properties();
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream("sysConfig.properties");
			p.load(inputStream);
			value = p.getProperty(key);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return value;
	}

	public static String getProp(String key, String resourceName) {
		String value = "";
		InputStream inputStream = null;
		Properties p = new Properties();
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream(resourceName);
			p.load(inputStream);
			value = p.getProperty(key);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return value;
	}

	public static String getDbconfig(String key) {
		String value = "";
		InputStream inputStream = null;
		Properties p = new Properties();
		try {
			inputStream = LoadProperties.class.getClassLoader().getResourceAsStream("dbconfig.properties");
			p.load(inputStream);
			value = p.getProperty(key);
		}
		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return value;
	}

	/**
	 * 获得请求路径
	 * 
	 * @param request
	 * @return
	 */
	public static String getRequestPath(HttpServletRequest request) {
		String requestPath = request.getRequestURI() + "?" + request.getQueryString();
		if (requestPath.indexOf("&") > -1) {// 去掉其他参数
			requestPath = requestPath.substring(0, requestPath.indexOf("&"));
		}
		requestPath = requestPath.substring(request.getContextPath().length() + 1);// 去掉项目路径
		return requestPath;
	}

}
