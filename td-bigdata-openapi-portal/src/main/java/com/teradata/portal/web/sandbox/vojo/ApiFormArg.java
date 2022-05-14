package com.teradata.portal.web.sandbox.vojo;

import com.teradata.openapi.rop.AbstractRopRequest;
import com.teradata.openapi.rop.Constants;

public class ApiFormArg extends AbstractRopRequest {

	private String appKey;

	private String version;

	private String method;

	private String format;

	private String codeType;

	private String reqType;

	private String fields;

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		if (appKey.contains("default")) {
			this.appKey = Constants.SYS_DEFAULT_APP_KEY;
		} else {
			this.appKey = appKey;
		}
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getCodeType() {
		return codeType;
	}

	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}

	public String getReqType() {
		return reqType;
	}

	public void setReqType(String reqType) {
		this.reqType = reqType;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

}
