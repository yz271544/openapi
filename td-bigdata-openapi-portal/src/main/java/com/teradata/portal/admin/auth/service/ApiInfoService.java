package com.teradata.portal.admin.auth.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/22.
 */

public interface ApiInfoService extends BaseService<ApiInfo> {


    public List<ApiInfo> findApiInfoAuthByGrpId(Integer grpId);

    public List<ApiInfo> findApiInfoAuthByUserId(Integer userId);

    public List<ApiInfo> findApiAuthByIdMaps(Map<String, Object> map);
}
