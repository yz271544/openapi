package com.teradata.portal.web.apiDisplay.pojo;

import java.io.Serializable;

public class StructApiArg implements Serializable {
	
	private static final long serialVersionUID = -1155296087815407451L;
	private String fieldName;
	private String fieldTargtType;
	private String syncMustDesc;
	private String asynMustDesc;
	private String rssMustDesc;
	private String reqArgId; 
	private String reqArgDefltVal;
	private String respnArgId;
	private String respnArgSmpVal;
	private String fieldFileDesc;
	public String getReqArgId() {
		return reqArgId;
	}
	public void setReqArgId(String reqArgId) {
		this.reqArgId = reqArgId;
	}
	public String getRespnArgId() {
		return respnArgId;
	}
	public void setRespnArgId(String respnArgId) {
		this.respnArgId = respnArgId;
	}
	public String getReqArgDefltVal() {
		return reqArgDefltVal;
	}
	public void setReqArgDefltVal(String reqArgDefltVal) {
		this.reqArgDefltVal = reqArgDefltVal;
	}
	public String getRespnArgSmpVal() {
		return respnArgSmpVal;
	}
	public void setRespnArgSmpVal(String respnArgSmpVal) {
		this.respnArgSmpVal = respnArgSmpVal;
	}
	public String getFieldName() {
		return fieldName;
	}
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}
	public String getFieldTargtType() {
		return fieldTargtType;
	}
	public void setFieldTargtType(String fieldTargtType) {
		this.fieldTargtType = fieldTargtType;
	}
	public String getSyncMustDesc() { return syncMustDesc; }
	public void setSyncMustDesc(String syncMustDesc) { this.syncMustDesc = syncMustDesc; }
	public String getAsynMustDesc() { return asynMustDesc; }
	public void setAsynMustDesc(String asynMustDesc) { this.asynMustDesc = asynMustDesc; }
	public String getRssMustDesc() { return rssMustDesc; }
	public void setRssMustDesc(String rssMustDesc) { this.rssMustDesc = rssMustDesc; }
	public String getFieldFileDesc() {
		return fieldFileDesc;
	}
	public void setFieldFileDesc(String fieldFileDesc) {
		this.fieldFileDesc = fieldFileDesc;
	}

}
