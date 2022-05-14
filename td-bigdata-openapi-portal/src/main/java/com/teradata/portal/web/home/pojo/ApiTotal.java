package com.teradata.portal.web.home.pojo;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-24 下午3:10:56
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class ApiTotal {

	private Integer activeNum;

	private Integer errorNum;

	private Integer totalNum;

	private Integer unactiveNum;

	/**
	 * 获取 activeNum
	 * 
	 * @return activeNum
	 */
	public Integer getActiveNum() {
		return activeNum;
	}

	/**
	 * 获取 errorNum
	 * 
	 * @return errorNum
	 */
	public Integer getErrorNum() {
		return errorNum;
	}

	/**
	 * 获取 totalNum
	 * 
	 * @return totalNum
	 */
	public Integer getTotalNum() {
		return totalNum;
	}

	/**
	 * 获取 unactiveNum
	 * 
	 * @return unactiveNum
	 */
	public Integer getUnactiveNum() {
		return unactiveNum;
	}

	/**
	 * 设置 activeNum
	 * 
	 * @param activeNum
	 *            activeNum
	 */
	public void setActiveNum(Integer activeNum) {
		this.activeNum = activeNum;
	}

	/**
	 * 设置 errorNum
	 * 
	 * @param errorNum
	 *            errorNum
	 */
	public void setErrorNum(Integer errorNum) {
		this.errorNum = errorNum;
	}

	/**
	 * 设置 totalNum
	 * 
	 * @param totalNum
	 *            totalNum
	 */
	public void setTotalNum(Integer totalNum) {
		this.totalNum = totalNum;
	}

	/**
	 * 设置 unactiveNum
	 * 
	 * @param unactiveNum
	 *            unactiveNum
	 */
	public void setUnactiveNum(Integer unactiveNum) {
		this.unactiveNum = unactiveNum;
	}

}
