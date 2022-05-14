package com.teradata.portal.web.home.dao;

import java.util.List;
import java.util.Map;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-24 上午11:38:34
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public interface HomeMapper {

	/*
	 * 获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	public List<?> queryApiChart(Map<String, Object> param);

	/*
	 * 获取api状态运行情况
	 */
	public List<Map<String, Object>> queryApiStatus(Map<String, Object> param, PageBounds pageBounds);

	/*
	 * 获取api数量（总数、活跃数、非活跃数、状态异常数）
	 */
	public List<Map<String, Object>> queryApiTotal(Map<String, Object> param);

	/*
	 * 获取待审批数量（注册工号、权限、api发布）
	 */
	public List<?> queryExamCount();

	/*
	 * 开发者视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	public List<?> queryOwnApiChart(Map<String, Object> param);

	/*
	 * 开发者视图-展示文字描述信息（累计开发api总数量、被访问api数量、被访问次数、平均访问时长）
	 */
	public List<?> queryStateCount(Map<String, Object> param);
}
