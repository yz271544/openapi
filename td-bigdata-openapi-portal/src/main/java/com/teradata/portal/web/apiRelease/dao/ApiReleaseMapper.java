package com.teradata.portal.web.apiRelease.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.portal.web.apiRelease.pojo.ApiArgRst;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.apiRelease.pojo.SorcFieldRst;
import com.teradata.portal.web.apiRelease.pojo.StructApiArg;

public interface ApiReleaseMapper {

	/*
	 * 克隆api（参数信息）
	 */
	public int cloneApiArg(ApiInfo apiItem);

	/*
	 * 克隆api（基本信息）
	 */
	public int cloneApiItem(ApiInfo apiItem);

	/*
	 * 克隆api（来源信息）
	 */
	public int cloneApiTabInfo(ApiInfo apiItem);

	/*
	 * 清除api配置相关信息（struct_api_arg、api_tab_info）
	 */
	public int deleteApiArg(@Param("apiId") Integer apiId, @Param("apiVersion") Integer apiVersion);

	/*
	 * 删除api
	 */
	public int deleteApiItem(ApiInfo apiItem);

	/*
	 * 新增api详情
	 */
	public int insertApiArg(StructApiArg structApiArg);

	/*
	 * 新增api
	 */
	public int insertApiItem(ApiInfo apiItem);

	/*
	 * 添加api配置信息给lzf
	 */
	public int insertApiTabInfo(List<Map<String, String>> param1, @Param("apiId") Integer apiId, @Param("apiVersion") Integer apiVersion,
	        @Param("fieldName") String fieldName);

	/*
	 * 插入错误日志表
	 */
	public int insertErrAbnorm(@Param("errAbnormId") Integer errAbnormId, @Param("errAbnormSurc") String errAbnormSurc,
	        @Param("reqUrl") String reqUrl, @Param("respnErrVal") String respnErrVal);

	/*
	 * 获取api的配置信息
	 */
	public List<ApiArgRst> queryApiArg(@Param("apiId") Integer apiId, @Param("apiVersion") Integer apiVersion);

	/*
	 * 获取api大类信息
	 */
	public List<Map<String, Object>> queryApiCls();

	/*
	 * 获取api数据周期类型信息
	 */
	public List<Map<String, Object>> queryApiDataCycle();

	/*
	 * 获取api状态信息
	 */
	public List<Map<String, Object>> queryApiExamStat();

	/*
	 * 获取api列表信息
	 */
	public List<?> queryApiInfo(Map<String, Object> param, PageBounds pageBounds);

	/*
	 * 获取api分类信息
	 */
	public List<Map<String, Object>> queryApiSort();

	/*
	 * 获取api状态信息
	 */
	public List<Map<String, Object>> queryApiStat();

	/*
	 * 获取api数据规模类型信息
	 */
	public List<Map<String, Object>> queryApiTabScale();

	/*
	 * 获取必选类型信息
	 */
	public List<Map<String, Object>> queryArgMustCode();

	/*
	 * 获取计算法则类型信息
	 */
	public List<Map<String, Object>> queryCalcPrincCode();

	/*
	 * 获取目标字段类型信息
	 */
	public List<Map<String, Object>> queryDataTargtTypeCode();

	/*
	 * 获取来源字段详细信息
	 */
	public List<Map<String, Object>> queryFieldSorcType(List<Map<String, String>> param1, @Param("fieldName") String fieldName);

	/*
	 * 克隆api（获取当前最大版本）
	 */
	public Integer queryLatestVersion(@Param("apiId") Integer apiId);

	/*
	 * 获取库信息
	 */
	public List<Map<String, Object>> querySchema(@Param("sourceId") Integer sourceId);

	/*
	 * 获取数据源信息
	 */
	public List<Map<String, Object>> querySource();

	/*
	 * 获取不同数据源的字段（取交集）
	 */
	public List<SorcFieldRst> querySourceField(@Param("param") List<Map<String, String>> param);

	/*
	 * 获取表信息
	 */
	public List<Map<String, Object>> queryTable(HashMap<String, Object> param);

	/*
	 * 获取api触发方式信息
	 */
	public List<Map<String, Object>> queryTriggerMethd();

	/*
	 * 发布api
	 */
	public int releaseApiItem(ApiInfo apiItem);

	/*
	 * 修改api
	 */
	public int updateApiItem(ApiInfo apiItem);

}