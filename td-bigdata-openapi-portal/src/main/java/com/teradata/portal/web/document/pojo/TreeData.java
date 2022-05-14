package com.teradata.portal.web.document.pojo;

import java.io.Serializable;
import java.util.List;

public class TreeData implements Serializable {

	private static final long serialVersionUID = 7342233346551709634L;
	private Integer id = -1;
	private boolean root = true;
	private String text = ".";
	private List<Document> children;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public boolean isRoot() {
		return root;
	}
	public void setRoot(boolean root) {
		this.root = root;
	}
	public String getText() {
		return text;
	}
	public void setText(String text) {
		this.text = text;
	}
	public List<Document> getChildren() {
		return children;
	}
	public void setChildren(List<Document> children) {
		this.children = children;
	}
	
}
