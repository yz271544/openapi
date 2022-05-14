package com.teradata.openapi.access.biz.dao;

import java.util.List;

import com.teradata.openapi.access.biz.bean.ApiInfo;

public interface ApiInfoMapper {

	/**
	 * 查询所有有效的api信息
	 * 
	 * @return
	 * @date 2016年4月25日
	 * @author houbl
	 */
	public List<ApiInfo> queryAllApiInfo();
}