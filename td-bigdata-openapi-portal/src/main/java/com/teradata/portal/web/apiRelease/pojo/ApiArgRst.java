package com.teradata.portal.web.apiRelease.pojo;

import java.util.List;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-7-31 下午11:51:05
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class ApiArgRst {

	private List<StructApiArg> fieldList;

	private String schemaName;

	private String sourceDesc;

	private Integer sourceId;

	private String tabName;

	/**
	 * 获取 fieldList
	 * 
	 * @return fieldList
	 */
	public List<StructApiArg> getFieldList() {
		return fieldList;
	}

	/**
	 * 获取 schemaName
	 * 
	 * @return schemaName
	 */
	public String getSchemaName() {
		return schemaName;
	}

	/**
	 * 获取 sourceDesc
	 * 
	 * @return sourceDesc
	 */
	public String getSourceDesc() {
		return sourceDesc;
	}

	/**
	 * 获取 sourceId
	 * 
	 * @return sourceId
	 */
	public Integer getSourceId() {
		return sourceId;
	}

	/**
	 * 获取 tabName
	 * 
	 * @return tabName
	 */
	public String getTabName() {
		return tabName;
	}

	/**
	 * 设置 fieldList
	 * 
	 * @param fieldList
	 *            fieldList
	 */
	public void setFieldList(List<StructApiArg> fieldList) {
		this.fieldList = fieldList;
	}

	/**
	 * 设置 schemaName
	 * 
	 * @param schemaName
	 *            schemaName
	 */
	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	/**
	 * 设置 sourceDesc
	 * 
	 * @param sourceDesc
	 *            sourceDesc
	 */
	public void setSourceDesc(String sourceDesc) {
		this.sourceDesc = sourceDesc;
	}

	/**
	 * 设置 sourceId
	 * 
	 * @param sourceId
	 *            sourceId
	 */
	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	/**
	 * 设置 tabName
	 * 
	 * @param tabName
	 *            tabName
	 */
	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

}
