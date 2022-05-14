package com.teradata.portal.web.sandbox.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import com.teradata.portal.web.sandbox.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiInfoModel;
import com.teradata.portal.web.sandbox.pojo.ApiSortCode;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;
import com.teradata.portal.web.sandbox.pojo.StructApiArg;

@Component
public interface SandBoxMapper {

	/**
	 * 查询所有API分类（一级分类 父级分类为0）
	 * 
	 * @return
	 * @author houbl
	 */
	List<ApiSortCode> queryAllApiSort();

	/**
	 * 根据api分类和reqMethod查询其分类下的所有API
	 * 
	 * @param apiSort
	 * @param reqMethod
	 * @return
	 * @author houbl
	 */
	List<ApiInfo> queryApiBySort(@Param("apiSort") Integer apiSort, @Param("reqMethod") String reqMethod);

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
	List<ApiInfo> queryApiBySortPagination(@Param("apiSort") Integer apiSort, @Param("reqMethod") String reqMethod, @Param("expName") String expName, @Param("start") Integer start, @Param("end") Integer end);

	Long loadExpApiNameCount(@Param("apiSort") Integer apiSort, @Param("reqMethod") String reqMethod, @Param("expName") String expName);
	/**
	 * 根据apiId查询其下所有api信息
	 * 
	 * @param apiId
	 * @return
	 * @author houbl
	 */
	List<ApiInfoModel> queryApiInfoVWById(@Param("apiId") Integer apiId);

	/**
	 * 根据apiId,apiVersion查询其api信息
	 * 
	 * @param apiId
	 * @param apiVersion
	 * @return
	 * @author houbl
	 */
	ApiInfo queryApiInfoByKey(@Param("apiId") Integer apiId, @Param("apiVersion") Integer apiVersion);

	/**
	 * 根据apiId、apiVersion查询其下所有的参数列表
	 * 
	 * @param apiId
	 * @param apiVersion
	 * @return
	 * @author houbl
	 */
	List<StructApiArg> queryStructArgByKey(@Param("apiId") Integer apiId, @Param("apiVersion") Integer apiVersion);

	/**
	 * 添加测试沙箱记录
	 * 
	 * @param record
	 * @return
	 * @author houbl
	 */
	int addTestBox(ApiTestBox record);

}