package com.teradata.portal.web.apiDisplay.pojo;

import java.io.Serializable;

public class ApiReqAddr implements Serializable {
	
	private static final long serialVersionUID = -6755007140660831972L;
	private Integer addrId;
	private String addrName;
	private String httpAddr;
	private String httpsAddr; 
	public Integer getAddrId() {
		return addrId;
	}
	public void setAddrId(Integer addrId) {
		this.addrId = addrId;
	}
	public String getAddrName() {
		return addrName;
	}
	public void setAddrName(String addrName) {
		this.addrName = addrName;
	}
	public String getHttpAddr() {
		return httpAddr;
	}
	public void setHttpAddr(String httpAddr) {
		this.httpAddr = httpAddr;
	}
	public String getHttpsAddr() {
		return httpsAddr;
	}
	public void setHttpsAddr(String httpsAddr) {
		this.httpsAddr = httpsAddr;
	}

}
