package com.teradata.portal.web.apiRelease.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONObject;

import com.teradata.portal.web.apiRelease.pojo.ApiArgRst;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.apiRelease.pojo.SorcFieldRst;

/**
 * API发布业务层接口. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-7-6 下午4:06:41
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public interface ApiReleaseService {

	/*
	 * 克隆api（基本信息+详情）
	 */
	public JSONObject cloneApi(ApiInfo apiItem);

	/*
	 * 删除api
	 */
	public JSONObject delApi(ApiInfo apiItem);

	/*
	 * 获取api的配置信息
	 */
	public List<ApiArgRst> getApiArg(Integer apiId, Integer apiVersion);

	/*
	 * 获取api大类信息
	 */
	public List<Map<String, Object>> getApiCls();

	/*
	 * 获取api数据周期类型信息
	 */
	public List<Map<String, Object>> getApiDataCycle();

	/*
	 * 获取api状态信息
	 */
	public List<Map<String, Object>> getApiExamStat();

	/*
	 * 获取api列表信息
	 */
	public List<?> getApiInfo(Map<String, Object> param);

	/*
	 * 获取api分类信息
	 */
	public List<Map<String, Object>> getApiSort();

	/*
	 * 获取api状态信息
	 */
	public List<Map<String, Object>> getApiStat();

	/*
	 * 获取api数据规模类型信息
	 */
	public List<Map<String, Object>> getApiTabScale();

	/*
	 * 获取必选类型信息
	 */
	public List<Map<String, Object>> getArgMustCode();

	/*
	 * 获取计算法则类型信息
	 */
	public List<Map<String, Object>> getCalcPrincCode();

	/*
	 * 获取目标字段类型信息
	 */
	public List<Map<String, Object>> getDataTargtTypeCode();

	/*
	 * 获取组信息
	 */
	public List<Map<String, Object>> getMustOneGrpId();

	/*
	 * 获取库信息
	 */
	public List<Map<String, Object>> getSchema(HashMap<String, Object> param);

	/*
	 * 获取数据源信息
	 */
	public List<Map<String, Object>> getSource();

	/*
	 * 获取不同数据源的字段（取交集）
	 */
	public List<SorcFieldRst> getSourceField(List<Map<String, String>> param);

	/*
	 * 获取表信息
	 */
	public List<Map<String, Object>> getTable(HashMap<String, Object> param);

	/*
	 * 获取api触发方式信息
	 */
	public List<Map<String, Object>> getTriggerMethd();

	/*
	 * 修改api
	 */
	public JSONObject modifyApiItem(ApiInfo apiItem);

	/*
	 * 保存api参数后，调用http请求，更新actor
	 */
	public void modifyRemoteActor(Integer apiId, Integer apiVersion);

	/*
	 * 发布api
	 */
	public JSONObject releaseApiItem(ApiInfo apiItem);

	/*
	 * 配置api详情
	 */
	public JSONObject saveApiArg(List<Map<String, String>> param1, List<Map<String, String>> param2, Integer apiId, Integer apiVersion);

	/*
	 * 新增api
	 */
	public JSONObject saveApiItem(ApiInfo apiItem);
}
