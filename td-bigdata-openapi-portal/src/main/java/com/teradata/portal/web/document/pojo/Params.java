package com.teradata.portal.web.document.pojo;

import java.io.Serializable;

import com.google.gson.Gson;

public class Params implements Serializable {

	private static final long serialVersionUID = -1718233004315528446L;
	private Integer nodeId;
	private Integer start;
	private Integer limit;
	private Integer apiSort;
	private Integer apiId;
	private Integer apiVersion;
	private String searchText;
	public String getSearchText() {
		Gson gson = new Gson();
		return gson.fromJson(searchText, String.class);
	}
	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}
	public Integer getApiId() {
		return apiId;
	}
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}
	public Integer getApiVersion() {
		return apiVersion;
	}
	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}
	public Integer getStart() {
		return start;
	}
	public void setStart(Integer start) {
		this.start = start;
	}
	public Integer getLimit() {
		return limit;
	}
	public void setLimit(Integer limit) {
		this.limit = limit;
	}
	public Integer getApiSort() {
		return apiSort;
	}
	public void setApiSort(Integer apiSort) {
		this.apiSort = apiSort;
	}
	public Integer getNodeId() {
		return nodeId;
	}
	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

}
