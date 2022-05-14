package com.teradata.portal.web.apiDisplay.service;

import com.teradata.portal.admin.document.pojo.BaseRet;
import com.teradata.portal.web.document.pojo.Params;

public interface ApiDisService {
	
	public BaseRet getApisBySortId(Params param, BaseRet br);
	
	public BaseRet getPubInfo(BaseRet br);
	
	public BaseRet searchApisByText(Params param, BaseRet br);

}
