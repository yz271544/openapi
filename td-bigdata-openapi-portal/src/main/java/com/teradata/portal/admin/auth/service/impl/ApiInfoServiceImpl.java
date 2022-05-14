package com.teradata.portal.admin.auth.service.impl;

import com.teradata.portal.admin.auth.dao.ApiInfoMapper;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.ApiInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/22.
 */
@Transactional
@Service("apiInfoService")
public class ApiInfoServiceImpl implements ApiInfoService{

    @Autowired
    private ApiInfoMapper apiInfoMapper;

    @Override
    public List<ApiInfo> findApiInfoAuthByGrpId(Integer grpId) {
        return apiInfoMapper.findApiInfoAuthByGrpId(grpId);
    }

    @Override
    public List<ApiInfo> findApiInfoAuthByUserId(Integer userId) {
        return apiInfoMapper.findApiInfoAuthByUserId(userId);
    }

    @Override
    public List<ApiInfo> findApiAuthByIdMaps(Map<String, Object> map) {
        return apiInfoMapper.findApiAuthByIdMaps(map);
    }

    @Override
    public PageView query(PageView pageView, ApiInfo apiInfo) {
        return null;
    }

    @Override
    public long count(ApiInfo apiInfo) {
        return 0;
    }

    @Override
    public List<ApiInfo> queryAll(ApiInfo apiInfo) {
        return null;
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(ApiInfo apiInfo) throws Exception {
        return null;
    }

    @Override
    public ApiInfo getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(ApiInfo apiInfo) throws Exception {
        return null;
    }
}
