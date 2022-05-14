package com.teradata.portal.web.apiDisplay.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.teradata.portal.web.apiDisplay.pojo.Api;
import com.teradata.portal.web.apiDisplay.pojo.ApiReqAddr;
import com.teradata.portal.web.apiDisplay.pojo.PublicArgInfo;
import com.teradata.portal.web.document.pojo.Params;

@Component
public interface ApiDisMapper {
	
	public Integer getApisBySortIdCount(Params param);
	
	public List<Api> getApisBySortId(Params param);
	
	public List<ApiReqAddr> getApiAddr();
	
	public List<PublicArgInfo> getPubArgs();
	
	public List<Api> searchApisByText(Params param);
	
	public List<Api> searchApisInResult(List<Api> resultList);

}
