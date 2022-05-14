package com.teradata.portal.web.home.pojo;

import java.math.BigDecimal;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-9-5 下午2:24:35
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class StateCount {

	private Integer apiTotal;

	private BigDecimal avgTime;

	private Integer visitedNum;

	private Integer visitedTime;

	/**
	 * 获取 apiTotal
	 * 
	 * @return apiTotal
	 */
	public Integer getApiTotal() {
		return apiTotal;
	}

	/**
	 * 获取 avgTime
	 * 
	 * @return avgTime
	 */
	public BigDecimal getAvgTime() {
		return avgTime;
	}

	/**
	 * 获取 visitedNum
	 * 
	 * @return visitedNum
	 */
	public Integer getVisitedNum() {
		return visitedNum;
	}

	/**
	 * 获取 visitedTime
	 * 
	 * @return visitedTime
	 */
	public Integer getVisitedTime() {
		return visitedTime;
	}

	/**
	 * 设置 apiTotal
	 * 
	 * @param apiTotal
	 *            apiTotal
	 */
	public void setApiTotal(Integer apiTotal) {
		this.apiTotal = apiTotal;
	}

	/**
	 * 设置 avgTime
	 * 
	 * @param avgTime
	 *            avgTime
	 */
	public void setAvgTime(BigDecimal avgTime) {
		this.avgTime = avgTime;
	}

	/**
	 * 设置 visitedNum
	 * 
	 * @param visitedNum
	 *            visitedNum
	 */
	public void setVisitedNum(Integer visitedNum) {
		this.visitedNum = visitedNum;
	}

	/**
	 * 设置 visitedTime
	 * 
	 * @param visitedTime
	 *            visitedTime
	 */
	public void setVisitedTime(Integer visitedTime) {
		this.visitedTime = visitedTime;
	}

}
