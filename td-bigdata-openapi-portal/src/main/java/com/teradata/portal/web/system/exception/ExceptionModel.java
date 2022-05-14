package com.teradata.portal.web.system.exception;

/**
 * 异常 model 用于向前端页面展示错误信息
 * 
 * @author houbl
 * 
 */
public class ExceptionModel {

	private final String __model = "error";// 仅用于前端JS识别model类型

	public String get__model() {
		return __model;
	}

	private String code;// 4位异常编码

	private String desc;// 异常编码描述

	private String message;// （抛出时）自定义的异常描述

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
