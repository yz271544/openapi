package com.teradata.portal.web.apiDisplay.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.teradata.portal.admin.document.pojo.BaseRet;
import com.teradata.portal.web.apiDisplay.dao.ApiDisMapper;
import com.teradata.portal.web.apiDisplay.pojo.Api;
import com.teradata.portal.web.apiDisplay.service.ApiDisService;
import com.teradata.portal.web.document.pojo.Params;

@Service
public class ApiDisServiceImpl implements ApiDisService {

	@Autowired
	ApiDisMapper apiDisMapper;
	
	@Override
	public BaseRet getApisBySortId(Params param, BaseRet br) {
		List<Api> apis = new ArrayList<Api>();
		Integer count = apiDisMapper.getApisBySortIdCount(param);
		apis = apiDisMapper.getApisBySortId(param);
		System.out.println("count:" + count + " start:" + param.getStart() + " limit:" + param.getLimit() + " apiSort:" + param.getApiSort());
		br.setTotalCount(count);
		br.setResult(
				apis.subList(
						param.getStart(), 
						((param.getStart() + param.getLimit() > count) ? count : (param.getStart() + param.getLimit()))
				)
		);
		return br;
	}

	@Override
	public BaseRet getPubInfo(BaseRet br) {
		br.setAddData(apiDisMapper.getApiAddr());
		br.setAddData1(apiDisMapper.getPubArgs());
		return br;
	}

	@Override
	public BaseRet searchApisByText(Params param, BaseRet br) {
		List<Api> resultList = apiDisMapper.searchApisByText(param);
		List<Api> resultList1 = apiDisMapper.searchApisInResult(resultList);
		br.setResult(resultList);
		br.setAddData(resultList1);
		return br;
	}

}
