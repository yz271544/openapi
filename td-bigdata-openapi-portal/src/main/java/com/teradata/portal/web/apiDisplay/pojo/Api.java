package com.teradata.portal.web.apiDisplay.pojo;

import java.io.Serializable;
import java.util.List;

public class Api implements Serializable {

	private static final long serialVersionUID = -9025248404745454968L;
	private Integer apiId;
	private Integer apiSort;
	private Integer apiVersion;
	private String apiName;
	private String apiDesc;
	private List<StructApiArg> structArgs;
	public List<StructApiArg> getStructArgs() {
		return structArgs;
	}
	public void setStructArgs(List<StructApiArg> structArgs) {
		this.structArgs = structArgs;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	public Integer getApiSort() {
		return apiSort;
	}
	public void setApiSort(Integer apiSort) {
		this.apiSort = apiSort;
	}
	public Integer getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}
	public String getApiName() {
		return apiName;
	}
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}
	public String getApiDesc() {
		return apiDesc;
	}
	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}
	
}
