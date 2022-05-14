package com.teradata.portal.web.system.exception;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import com.teradata.portal.web.system.exception.code.RetCodeForSystem;
import com.teradata.portal.web.system.exception.code.StatusCode;

public class BusinessException extends RuntimeException implements Serializable {

	private static final long serialVersionUID = 1L;

	private StatusCode statusCode;

	public StatusCode getStatusCode() {
		return statusCode;
	}

	public void setStatusCode(StatusCode statusCode) {
		this.statusCode = statusCode;
	}

	public BusinessException(StatusCode statusCode) {
		super();
		this.statusCode = statusCode;
	}

	public BusinessException(String message, StatusCode statusCode) {
		super(message);
		this.statusCode = statusCode;
	}

	public BusinessException(Throwable cause, String message, StatusCode statusCode) {
		super(message, cause);
		this.statusCode = statusCode;
	}

	public BusinessException(Throwable cause, StatusCode statusCode) {
		super(cause);
		this.statusCode = statusCode;
	}

	public ExceptionModel toModel() {
		// 4位异常编码
		StringBuilder code = new StringBuilder().append(this.statusCode.getCode());
		// 异常描述
		StringBuilder desc = new StringBuilder();
		desc.append(this.statusCode.getDescription());

		ExceptionModel model = new ExceptionModel();
		model.setCode(code.toString());
		model.setDesc(desc.toString());

		if (StringUtils.isNotBlank(this.getMessage())) {
			model.setMessage(this.getMessage());
		}
		return model;
	}

	/**
	 * 包装未知的系统异常
	 * 
	 * @param cause
	 * @return
	 */
	public static BusinessException wrapException(Throwable cause) {
		BusinessException se = null;
		if (cause instanceof BusinessException) {
			se = (BusinessException) cause;
		} else {
			se = new BusinessException(cause, RetCodeForSystem.INSIDE_ERROR);
		}
		return se;
	}

	/**
	 * 遍历异常堆栈信息，以单行String形式返回
	 * 
	 * @return
	 */
	public String getTraceMessage() {
		StringBuilder messages = new StringBuilder();
		Throwable cause = this;

		int i = 1;
		while (cause != null) {
			if (cause.getMessage() != null && !"".equals(cause.getMessage().trim())) {
				messages.append(i++).append("#[ ").append(cause.getMessage()).append(".] ");
			}
			cause = cause.getCause();
		}

		return "".equals(messages.toString().trim()) ? "" : messages.toString();
	}

}
