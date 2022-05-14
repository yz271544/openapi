package com.teradata.portal.web.apiRelease.pojo;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-9-22 上午9:40:03
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class FieldTypeInfo {

	private String fieldName;

	private String fieldTargtType;

	/**
	 * 获取 fieldName
	 * 
	 * @return fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 获取 fieldTargtType
	 * 
	 * @return fieldTargtType
	 */
	public String getFieldTargtType() {
		return fieldTargtType;
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
	 * 设置 fieldTargtType
	 * 
	 * @param fieldTargtType
	 *            fieldTargtType
	 */
	public void setFieldTargtType(String fieldTargtType) {
		this.fieldTargtType = fieldTargtType;
	}

}
