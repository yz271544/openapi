package com.teradata.portal.web.apiRelease.pojo;

import java.util.List;

public class SorcFieldRst {

	private String fieldName;

	private List<FieldTypeInfo> fieldTypeList; // 一个字段在选择的数据源中有多少种类型

	private List<SourceFieldInfo> sourceFieldList;// 选择的数据源中有多少个包含某个字段

	/**
	 * 获取 fieldName
	 * 
	 * @return fieldName
	 */
	public String getFieldName() {
		return fieldName;
	}

	/**
	 * 获取 fieldTypeList
	 * 
	 * @return fieldTypeList
	 */
	public List<FieldTypeInfo> getFieldTypeList() {
		return fieldTypeList;
	}

	/**
	 * 获取 sourceFieldList
	 * 
	 * @return sourceFieldList
	 */
	public List<SourceFieldInfo> getSourceFieldList() {
		return sourceFieldList;
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
	 * 设置 fieldTypeList
	 * 
	 * @param fieldTypeList
	 *            fieldTypeList
	 */
	public void setFieldTypeList(List<FieldTypeInfo> fieldTypeList) {
		this.fieldTypeList = fieldTypeList;
	}

	/**
	 * 设置 sourceFieldList
	 * 
	 * @param sourceFieldList
	 *            sourceFieldList
	 */
	public void setSourceFieldList(List<SourceFieldInfo> sourceFieldList) {
		this.sourceFieldList = sourceFieldList;
	}

}