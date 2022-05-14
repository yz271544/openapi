package com.teradata.portal.web.home.pojo;

import java.math.BigDecimal;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-24 下午6:05:20
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public class ApiChart {

	private String dealDate;

	private BigDecimal valAsync;

	private BigDecimal valBook;

	private BigDecimal valSync;

	/**
	 * 获取 dealDate
	 * 
	 * @return dealDate
	 */
	public String getDealDate() {
		return dealDate;
	}

	/**
	 * 获取 valAsync
	 * 
	 * @return valAsync
	 */
	public BigDecimal getValAsync() {
		return valAsync;
	}

	/**
	 * 获取 valBook
	 * 
	 * @return valBook
	 */
	public BigDecimal getValBook() {
		return valBook;
	}

	/**
	 * 获取 valSync
	 * 
	 * @return valSync
	 */
	public BigDecimal getValSync() {
		return valSync;
	}

	/**
	 * 设置 dealDate
	 * 
	 * @param dealDate
	 *            dealDate
	 */
	public void setDealDate(String dealDate) {
		this.dealDate = dealDate;
	}

	/**
	 * 设置 valAsync
	 * 
	 * @param valAsync
	 *            valAsync
	 */
	public void setValAsync(BigDecimal valAsync) {
		this.valAsync = valAsync;
	}

	/**
	 * 设置 valBook
	 * 
	 * @param valBook
	 *            valBook
	 */
	public void setValBook(BigDecimal valBook) {
		this.valBook = valBook;
	}

	/**
	 * 设置 valSync
	 * 
	 * @param valSync
	 *            valSync
	 */
	public void setValSync(BigDecimal valSync) {
		this.valSync = valSync;
	}

}
