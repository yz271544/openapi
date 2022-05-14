package com.teradata.portal.web.apiRelease.pojo;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-9-21 下午7:06:53
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class SourceFieldInfo {

	private String fieldName;

	private Integer sourceId;

	/**
	 * 获取 fieldName
	 * 
	 * @return fieldName
	 */
	public String getFieldName() {
		return fieldName;
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
	 * 设置 fieldName
	 * 
	 * @param fieldName
	 *            fieldName
	 */
	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
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

}
