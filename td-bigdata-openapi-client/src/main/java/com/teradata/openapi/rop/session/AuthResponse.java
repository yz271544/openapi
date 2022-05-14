package com.teradata.openapi.rop.session;


/**
 * 认证结果，如果认证成功，则设置{@link Session}，否则设置{@link #errorCode},{@link #errorCode}必须
 * 在ROP的国际化文件中定义。
 * 
 * @author : chenxh
 * @date: 13-8-13
 */
public class AuthResponse {

	private boolean authenticated = false;

	private String errorCode;

	private Session ropSession;

	public String getErrorCode() {
		return errorCode;
	}

	public Session getRopSession() {
		return ropSession;
	}

	public boolean isAuthenticated() {
		return authenticated;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}

	public void setRopSession(Session logonSession) {
		this.ropSession = logonSession;
		this.authenticated = true;
	}
}
