package com.teradata.portal.web.home.pojo;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-26 下午5:24:35
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class ExamCount {

	private Integer approveNum;

	private Integer authNum;

	private Integer registerNum;

	/**
	 * 获取 approveNum
	 * 
	 * @return approveNum
	 */
	public Integer getApproveNum() {
		return approveNum;
	}

	/**
	 * 获取 authNum
	 * 
	 * @return authNum
	 */
	public Integer getAuthNum() {
		return authNum;
	}

	/**
	 * 获取 registerNum
	 * 
	 * @return registerNum
	 */
	public Integer getRegisterNum() {
		return registerNum;
	}

	/**
	 * 设置 approveNum
	 * 
	 * @param approveNum
	 *            approveNum
	 */
	public void setApproveNum(Integer approveNum) {
		this.approveNum = approveNum;
	}

	/**
	 * 设置 authNum
	 * 
	 * @param authNum
	 *            authNum
	 */
	public void setAuthNum(Integer authNum) {
		this.authNum = authNum;
	}

	/**
	 * 设置 registerNum
	 * 
	 * @param registerNum
	 *            registerNum
	 */
	public void setRegisterNum(Integer registerNum) {
		this.registerNum = registerNum;
	}

}
