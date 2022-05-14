package com.teradata.portal.web.home.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.portal.web.home.dao.HomeMapper;
import com.teradata.portal.web.home.service.HomeService;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-24 上午11:37:13
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Service
public class HomeServiceImpl implements HomeService {

	@Autowired
	HomeMapper homeMapper;

	/*
	 * 运维视图|APP视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	@Override
	public List<?> getApiChart(Map<String, Object> param) {
		return homeMapper.queryApiChart(param);
	}

	/*
	 * 获取api状态运行情况
	 */
	@Override
	public List<?> getApiStatus(Map<String, Object> param) {
		Integer page = Integer.parseInt(param.get("pageIndex").toString()) + 1;
		Integer limit = Integer.parseInt(param.get("pageSize").toString());
		String sortString = "most_rec_use_time.desc";
		PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sortString), true);
		return homeMapper.queryApiStatus(param, pageBounds);
	}

	/*
	 * 获取api数量（总数、活跃数、非活跃数、状态异常数）
	 */
	@Override
	public List<?> getApiTotal(Map<String, Object> param) {
		return homeMapper.queryApiTotal(param);
	}

	/*
	 * 获取待审批数量（注册工号、权限、api发布）
	 */
	@Override
	public List<?> getExamCount() {
		return homeMapper.queryExamCount();
	}

	/*
	 * 开发者视图-获取api趋势分析(API数量、访问次数、平均响应时长)
	 */
	@Override
	public List<?> getOwnApiChart(Map<String, Object> param) {
		return homeMapper.queryOwnApiChart(param);
	}

	/*
	 * 开发者视图-展示文字描述信息（累计开发api总数量、被访问api数量、被访问次数、平均访问时长）
	 */
	@Override
	public List<?> getStateCount(Map<String, Object> param) {
		return homeMapper.queryStateCount(param);
	}
}
