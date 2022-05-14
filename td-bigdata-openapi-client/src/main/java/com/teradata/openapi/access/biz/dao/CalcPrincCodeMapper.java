package com.teradata.openapi.access.biz.dao;

import java.util.List;

import com.teradata.openapi.access.biz.bean.CalcPrincCode;

public interface CalcPrincCodeMapper {

	/**
	 * 查询计算法则
	 * 
	 * @return
	 * @date 2016年4月29日
	 * @author houbl
	 */
	public List<CalcPrincCode> queryCalcPrincInfo();
}