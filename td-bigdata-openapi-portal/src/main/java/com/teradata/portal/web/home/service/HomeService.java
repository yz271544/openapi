package com.teradata.portal.web.home.service;

import java.util.List;
import java.util.Map;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-24 上午11:36:49
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public interface HomeService {

	/*
	 * 运维视图|APP视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	public List<?> getApiChart(Map<String, Object> param);

	/*
	 * 获取api状态运行情况
	 */
	public List<?> getApiStatus(Map<String, Object> param);

	/*
	 * 获取api数量（总数、活跃数、非活跃数、状态异常数）
	 */
	public List<?> getApiTotal(Map<String, Object> param);

	/*
	 * 获取待审批数量（注册工号、权限、api发布）
	 */
	public List<?> getExamCount();

	/*
	 * 开发者视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	public List<?> getOwnApiChart(Map<String, Object> param);

	/*
	 * 开发者视图-展示文字描述信息（累计开发api总数量、被访问api数量、被访问次数、平均访问时长）
	 */
	public List<?> getStateCount(Map<String, Object> param);

}
