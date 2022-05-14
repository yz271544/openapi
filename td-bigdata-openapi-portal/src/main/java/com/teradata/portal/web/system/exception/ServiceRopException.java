package com.teradata.portal.web.system.exception;

/**
 * 
 * 外部服务异常. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2015年9月22日 下午4:51:19
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class ServiceRopException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private String statusCode;

	private String statusDesc;

	public ServiceRopException(String statusCode, String statusDesc) {
		super(statusDesc);
		this.statusCode = statusCode;
		this.statusDesc = statusDesc;
	}

	public String getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(String statusCode) {
		this.statusCode = statusCode;
	}

	public String getStatusDesc() {
		return statusDesc;
	}

	public void setStatusDesc(String statusDesc) {
		this.statusDesc = statusDesc;
	}

	public ServiceRopException(Throwable cause) {
		super(cause);
	}

	public ServiceRopException(String message, Throwable cause) {
		super(message, cause);
	}
}
