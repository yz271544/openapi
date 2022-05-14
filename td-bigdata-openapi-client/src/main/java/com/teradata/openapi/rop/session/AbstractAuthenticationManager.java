package com.teradata.openapi.rop.session;

/**
 * @author : chenxh
 * @date: 13-8-13
 */
public abstract class AbstractAuthenticationManager implements AuthenticationManager {

	private boolean _default = false;

	private String[] appKeys = null;

	@Override
	public String[] appkeys() {
		return appKeys;
	}

	@Override
	public boolean isDefault() {
		return _default;
	}

	public void setAppKeys(String[] appKeys) {
		this.appKeys = appKeys;
	}

	public void setDefault(boolean _default) {
		this._default = _default;
	}
}
