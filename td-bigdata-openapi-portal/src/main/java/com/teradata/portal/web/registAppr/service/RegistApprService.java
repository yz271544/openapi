package com.teradata.portal.web.registAppr.service;

import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.apiRelease.pojo.SorcFieldRst;
import net.sf.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 注册审批业务接口层. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-8-29 上午6:39:41
 * <p>
 * Company: TERADATA
 * <p>
 *
 * @author Lyndon.Hu@Teradata.com
 * @version 1.0.0
 */
public interface RegistApprService {

    List<UserInfo> getRegUserInfo();

    /*
	 * 获取reg审批状态信息
	 */
    List<Map<String, Object>> getRegExamStat();


    /*
	 * 获取reg列表信息
	 */
    List<?> getRegInfo(Map<String, Object> param);

    /*
	 * api审批
	 */
    JSONObject doRegApprove(UserInfo user);
}
