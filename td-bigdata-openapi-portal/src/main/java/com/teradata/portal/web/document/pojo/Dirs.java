package com.teradata.portal.web.document.pojo;

import java.io.Serializable;
import java.util.List;

public class Dirs implements Serializable {

	private static final long serialVersionUID = -9147807386230831147L;
	private List<Document> doc;
	private List<ApiSort> sort;
	public List<Document> getDoc() {
		return doc;
	}
	public void setDoc(List<Document> doc) {
		this.doc = doc;
	}
	public List<ApiSort> getSort() {
		return sort;
	}
	public void setSort(List<ApiSort> sort) {
		this.sort = sort;
	}

}
