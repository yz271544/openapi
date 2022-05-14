package com.teradata.portal.web.sandbox.service;

import java.util.List;
import java.util.Map;

import com.teradata.portal.web.sandbox.pojo.DynamicApiArg;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;

/**
 * 
 * 测试沙箱业务层. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年6月16日 上午10:55:27
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface SandBoxService {

	/**
	 * 查询所有API分类
	 * 
	 * @return
	 * @author houbl
	 */
    List<Map<String, Object>> getApiSortMap();

	/**
	 * 根据apiSort和reqMethod 查询其分类下所有有效的api
	 * 
	 * @param apiSort
	 * @param reqMethod
	 * @return
	 * @author houbl
	 */
    List<Map<String, Object>> getApiInfoMapBySort(Integer apiSort, String reqMethod);

	/**
	 * 根据apiSort、reqMethod、expName、start和end 查询其分类下所有有效的api
	 *
	 * @param apiSort
	 * @param reqMethod
	 * @param expName
	 * @param start
	 * @param end
	 * @return
	 * @author huzy
	 */
    List<Map<String, Object>> getApiInfoMapBySortPagination(Integer apiSort, String reqMethod, String expName, Integer start, Integer end);

	/**
	 * 根据apiSort、reqMethod、expName 查询计算其分类下所有有效的api的条目数
	 *
	 * @param apiSort
	 * @param reqMethod
	 * @param expName
	 * @return
	 * @author huzy
	 */
    Long loadExpApiNameCount(Integer apiSort, String reqMethod, String expName);
	/**
	 * 根据apiId和apiSort查询其下所有的版本信息
	 * 
	 * @param apiSort
	 * @param apiId
	 * @return
	 * @author houbl
	 */
    List<Map<String, Object>> getApiVersionMapById(Integer apiSort, Integer apiId);

	/**
	 * 根据apiId和apiVersion 查询其data_cycle_type 和trigger_method
	 * 
	 * @param apiId
	 * @param apiVersion
	 * @return
	 * @author houbl
	 */
    List<Map<String, Object>> getApiOtherMapById(Integer apiId, Integer apiVersion);

	/**
	 * 根据apiId和apiVersion、reqMethod 动态获取其结构化参数
	 * 
	 * @param apiId
	 * @param apiVersion
	 * @param reqMethod
	 * @return
	 * @author houbl
	 */
    DynamicApiArg getDynamicApiArgById(Integer apiId, Integer apiVersion, Integer reqMethod);

	/**
	 * 根据apiId和apiVersion获取api_visit_methd信息
	 * 
	 * @param apiId
	 * @param apiVersion
	 * @return
	 * @author houbl
	 */
    String getApiReqMethodById(Integer apiId, Integer apiVersion);

	/**
	 * 调用远程返回数据
	 * 
	 * @param srcParamMap
	 * @param formArg
	 * @param invokeMethod
	 * @param appSecret
	 * @return
	 * @author houbl
	 */
    Map<String, String> callRemoteApiData(Map<String, String[]> srcParamMap, ApiFormArg formArg, String invokeMethod, String appSecret,
                                          String sourceFlag);
}
