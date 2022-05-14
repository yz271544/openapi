package com.teradata.portal.web.document.pojo;

import java.io.Serializable;

public class ApiSort implements Serializable {

	private static final long serialVersionUID = 2032371557401064300L;
	private Integer apiSort;
	private String apiSortName;
	private String apiSortDesc;
	public String getApiSortDesc() {
		return apiSortDesc;
	}
	public void setApiSortDesc(String apiSortDesc) {
		this.apiSortDesc = apiSortDesc;
	}
	public Integer getApiSort() {
		return apiSort;
	}
	public void setApiSort(Integer apiSort) {
		this.apiSort = apiSort;
	}
	public String getApiSortName() {
		return apiSortName;
	}
	public void setApiSortName(String apiSortName) {
		this.apiSortName = apiSortName;
	}

}
