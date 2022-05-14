package com.teradata.portal.web.apiRelease.pojo;

public class SorcFieldInfo {

	private String fieldName;

	private String fieldTargtType;

	private String fieldTitle;

	private Integer piFlag;

	private Integer ppiFlag;

	private String ppiType;

	private String schemaName;

	private Integer sorcFieldLen;

	private String sorcFieldType;

	private String sorcForm;

	private Integer sorcTotalDigit;

	private Integer sourceId;

	private String tabName;

	public String getFieldName() {
		return fieldName;
	}

	public String getFieldTargtType() {
		return fieldTargtType;
	}

	public String getFieldTitle() {
		return fieldTitle;
	}

	public Integer getPiFlag() {
		return piFlag;
	}

	public Integer getPpiFlag() {
		return ppiFlag;
	}

	public String getPpiType() {
		return ppiType;
	}

	public String getSchemaName() {
		return schemaName;
	}

	public Integer getSorcFieldLen() {
		return sorcFieldLen;
	}

	public String getSorcFieldType() {
		return sorcFieldType;
	}

	public String getSorcForm() {
		return sorcForm;
	}

	public Integer getSorcTotalDigit() {
		return sorcTotalDigit;
	}

	public Integer getSourceId() {
		return sourceId;
	}

	public String getTabName() {
		return tabName;
	}

	public void setFieldName(String fieldName) {
		this.fieldName = fieldName == null ? null : fieldName.trim();
	}

	public void setFieldTargtType(String fieldTargtType) {
		this.fieldTargtType = fieldTargtType == null ? null : fieldTargtType.trim();
	}

	public void setFieldTitle(String fieldTitle) {
		this.fieldTitle = fieldTitle == null ? null : fieldTitle.trim();
	}

	public void setPiFlag(Integer piFlag) {
		this.piFlag = piFlag;
	}

	public void setPpiFlag(Integer ppiFlag) {
		this.ppiFlag = ppiFlag;
	}

	public void setPpiType(String ppiType) {
		this.ppiType = ppiType == null ? null : ppiType.trim();
	}

	public void setSchemaName(String schemaName) {
		this.schemaName = schemaName == null ? null : schemaName.trim();
	}

	public void setSorcFieldLen(Integer sorcFieldLen) {
		this.sorcFieldLen = sorcFieldLen;
	}

	public void setSorcFieldType(String sorcFieldType) {
		this.sorcFieldType = sorcFieldType == null ? null : sorcFieldType.trim();
	}

	public void setSorcForm(String sorcForm) {
		this.sorcForm = sorcForm == null ? null : sorcForm.trim();
	}

	public void setSorcTotalDigit(Integer sorcTotalDigit) {
		this.sorcTotalDigit = sorcTotalDigit;
	}

	public void setSourceId(Integer sourceId) {
		this.sourceId = sourceId;
	}

	public void setTabName(String tabName) {
		this.tabName = tabName == null ? null : tabName.trim();
	}
}