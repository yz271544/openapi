package com.teradata.openapi.framework.message.request;

import java.io.Serializable;

public class RepArg implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 7702954533839644647L;

	private String fieldAlias;

	private String fieldName;

	private String fieldTargtType;

	private String fieldTitle;

	private String schemaName;

	private String tabName;

	private String tabAlias;

	public String getFieldAlias() {
		return fieldAlias;
	}

	public void setFieldAlias(String fieldAlias) {
		this.fieldAlias = fieldAlias;
	}

	public String getFieldName() {
		return fieldName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}

	public String getFieldTargtType() {
		return fieldTargtType;
	}

	public void setFieldTargtType(String fieldTargtType) {
		this.fieldTargtType = fieldTargtType;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName;
	}

	public String getTabName() {
		return tabName;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName;
	}

	public String getTabAlias() {
		return tabAlias;
	}

	public void setTabAlias(String tabAlias) {
		this.tabAlias = tabAlias;
	}

}
