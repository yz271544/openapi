package com.teradata.portal.web.system.exception.code;

public enum RetCodeForSystem implements StatusCode {

	RET_CODE_SUCCESS("0000", "成功"),

	USER_ACCOUNT_IS_NULL("0001", "登录用户帐号为空!"),

	HAS_REPEAT_DATA("0002", "登录帐号信息重复!"),

	INSIDE_ERROR("0003", "系统内部异常!"),

	SESSION_INVALID("0004", "有效会话过期,请重新登录!"),

	USER_INFO_IS_NULL("0005", "登录帐号不存在,请联系管理员"),

	FILE_NOT_IS_EXIST("0006", "下载文件不存在"),

	DEPART_NOT_IS_EXIST("0007", "部门组织结构树不存在"),

	RET_CODE_FAILURE("0010", "失败");

	private String code;

	private String description;

	private RetCodeForSystem(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

}
