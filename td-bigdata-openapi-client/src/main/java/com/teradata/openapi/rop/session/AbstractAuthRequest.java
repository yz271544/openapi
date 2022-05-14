package com.teradata.openapi.rop.session;

/**
 * @author : chenxh
 * @date: 13-8-13
 */
public abstract class AbstractAuthRequest implements AuthRequest {

	private Object detail;

	@Override
	public Object getDetail() {
		return detail;
	}

	public void setDetail(Object detail) {
		this.detail = detail;
	}

}
