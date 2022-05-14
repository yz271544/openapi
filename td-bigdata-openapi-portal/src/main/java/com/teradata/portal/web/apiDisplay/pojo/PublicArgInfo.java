package com.teradata.portal.web.apiDisplay.pojo;

import java.io.Serializable;

public class PublicArgInfo implements Serializable {
	
	private static final long serialVersionUID = 7586639680505952829L;
	private Integer publicArgId;
	private String publicArgName;
	private String publicArgDataType;
	private String codeDesc;
	private String publicArgDesc;
	public Integer getPublicArgId() {
		return publicArgId;
	}
	public void setPublicArgId(Integer publicArgId) {
		this.publicArgId = publicArgId;
	}
	public String getPublicArgName() {
		return publicArgName;
	}
	public void setPublicArgName(String publicArgName) {
		this.publicArgName = publicArgName;
	}
	public String getPublicArgDataType() {
		return publicArgDataType;
	}
	public void setPublicArgDataType(String publicArgDataType) {
		this.publicArgDataType = publicArgDataType;
	}
	public String getCodeDesc() {
		return codeDesc;
	}
	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc;
	}
	public String getPublicArgDesc() {
		return publicArgDesc;
	}
	public void setPublicArgDesc(String publicArgDesc) {
		this.publicArgDesc = publicArgDesc;
	}

}
