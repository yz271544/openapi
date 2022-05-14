package com.teradata.portal.web.apiRelease.pojo;

public class ApiCode {

	private String codeDesc;

	private Integer codeId;

	private String codeType;

	public String getCodeDesc() {
		return codeDesc;
	}

	/**
	 * 获取 codeId
	 * 
	 * @return codeId
	 */
	public Integer getCodeId() {
		return codeId;
	}

	/**
	 * 获取 codeType
	 * 
	 * @return codeType
	 */
	public String getCodeType() {
		return codeType;
	}

	public void setCodeDesc(String codeDesc) {
		this.codeDesc = codeDesc == null ? null : codeDesc.trim();
	}

	/**
	 * 设置 codeId
	 * 
	 * @param codeId
	 *            codeId
	 */
	public void setCodeId(Integer codeId) {
		this.codeId = codeId;
	}

	/**
	 * 设置 codeType
	 * 
	 * @param codeType
	 *            codeType
	 */
	public void setCodeType(String codeType) {
		this.codeType = codeType;
	}
}