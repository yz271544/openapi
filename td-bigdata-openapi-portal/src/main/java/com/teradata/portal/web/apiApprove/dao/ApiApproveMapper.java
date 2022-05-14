package com.teradata.portal.web.apiApprove.dao;

import java.util.List;

import com.teradata.portal.web.apiApprove.pojo.ApiExamInfo;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;

/**
 * 标题、简要说明. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-10 上午10:09:43
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
public interface ApiApproveMapper {

	public int insertApiExamInfo(ApiExamInfo apiExamInfo);

	public List<ApiTestBox> queryApiTestBox(ApiInfo apiItem);

	public int updateApiExamInfo(ApiExamInfo apiExamInfo);
}
