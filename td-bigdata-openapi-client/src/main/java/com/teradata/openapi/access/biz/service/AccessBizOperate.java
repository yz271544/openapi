package com.teradata.openapi.access.biz.service;

import java.util.List;
import java.util.Map;

import com.teradata.openapi.access.biz.bean.ApiInfo;
import com.teradata.openapi.access.biz.bean.CalcPrincCode;
import com.teradata.openapi.access.biz.bean.UserInfo;

/**
 * 
 * 接入引擎业务处理. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月22日 下午2:31:30
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface AccessBizOperate {

	/**
	 * 查询所有有效的用户信息
	 * 
	 * @return
	 * @date 2016年4月22日
	 * @author houbl
	 */
	public List<UserInfo> findAllUserInfo();

	/**
	 * 查询所有有效的api信息(包括元字段信息)
	 * 
	 * @return
	 * @date 2016年4月22日
	 * @author houbl
	 */
	public List<ApiInfo> findAllApiInfo();

	/**
	 * 查询用户下有权限的Api
	 * 
	 * @return
	 * @date 2016年4月25日
	 * @author houbl
	 */
	public Map<String, List<String>> findUserIncludeApi();

	/**
	 * 查询计算法则
	 * 
	 * @return
	 * @date 2016年4月29日
	 * @author houbl
	 */
	public List<CalcPrincCode> findCalcPrincInfo();
}
