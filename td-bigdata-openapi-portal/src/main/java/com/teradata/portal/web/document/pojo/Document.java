package com.teradata.portal.web.document.pojo;

import java.io.Serializable;
import java.util.List;

public class Document implements Serializable {

	private static final long serialVersionUID = 6432843328791650583L;
	private Integer nodeId;
	private Integer fathrNode;
	private Integer isleafNode;
	private Integer nodeOrder;
	private String nodeDesc;
	private String effDate;
	private String fileTitle;
	private String fileDate;
	private String fileAuthor;
	private String fileContent;
	private String iconCls = "task-folder";
	private boolean leaf = false;
	private Integer id;
	private Integer parentId;
	private String text;
	private boolean expandable;
	private boolean expanded;
	private List<Document> children;

	public Integer getNodeId() {
		return nodeId;
	}

	public void setNodeId(Integer nodeId) {
		this.nodeId = nodeId;
	}

	public Integer getFathrNode() {
		return fathrNode;
	}

	public void setFathrNode(Integer fathrNode) {
		this.fathrNode = fathrNode;
	}

	public Integer getIsleafNode() {
		return isleafNode;
	}

	public void setIsleafNode(Integer isleafNode) {
		this.isleafNode = isleafNode;
	}

	public Integer getNodeOrder() {
		return nodeOrder;
	}

	public void setNodeOrder(Integer nodeOrder) {
		this.nodeOrder = nodeOrder;
	}

	public String getNodeDesc() {
		return nodeDesc;
	}

	public void setNodeDesc(String nodeDesc) {
		this.nodeDesc = nodeDesc;
	}

	public String getEffDate() {
		return effDate;
	}

	public void setEffDate(String effDate) {
		this.effDate = effDate;
	}

	public String getFileTitle() {
		return fileTitle;
	}

	public void setFileTitle(String fileTitle) {
		this.fileTitle = fileTitle;
	}

	public String getFileDate() {
		return fileDate;
	}

	public void setFileDate(String fileDate) {
		this.fileDate = fileDate;
	}

	public String getFileAuthor() {
		return fileAuthor;
	}

	public void setFileAuthor(String fileAuthor) {
		this.fileAuthor = fileAuthor;
	}

	public String getFileContent() {
		return fileContent;
	}

	public void setFileContent(String fileContent) {
		this.fileContent = fileContent;
	}

	public String getIconCls() {
		return iconCls;
	}

	public void setIconCls(String iconCls) {
		this.iconCls = iconCls;
	}

	public boolean isLeaf() {
		return leaf;
	}

	public void setLeaf(boolean leaf) {
		this.leaf = leaf;
	}

	public List<Document> getChildren() {
		if(children != null && children.size() == 1 && children.get(0).getNodeId() == null) {
			children = null;
		}
		return children;
	}

	public void setChildren(List<Document> children) {
		this.children = children;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Integer getParentId() {
		return parentId;
	}

	public void setParentId(Integer parentId) {
		this.parentId = parentId;
	}

	public boolean isExpandable() {
		return expandable;
	}

	public void setExpandable(boolean expandable) {
		this.expandable = expandable;
	}

	public boolean isExpanded() {
		return expanded;
	}

	public void setExpanded(boolean expanded) {
		this.expanded = expanded;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

}
