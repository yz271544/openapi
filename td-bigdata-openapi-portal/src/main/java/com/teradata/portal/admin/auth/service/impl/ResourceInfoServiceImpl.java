package com.teradata.portal.admin.auth.service.impl;

import com.teradata.portal.admin.auth.dao.ResourceInfoMapper;
import com.teradata.portal.admin.auth.entity.ResourceInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.ResourceInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Evan on 2016/7/20.
 */
@Transactional
@Service("resourceInfoService")
public class ResourceInfoServiceImpl implements ResourceInfoService {

    @Autowired
    private ResourceInfoMapper resourceInfoMapper;

    @Override
    public List<ResourceInfo> findResInfoByGrpId(Integer grpId) {
        return null;
    }

    @Override
    public List<ResourceInfo> findResInfoByLoginAcct(String loginAcct) {
        return resourceInfoMapper.findResInfoByLoginAcct(loginAcct);
    }

    @Override
    public List<ResourceInfo> findResAuthByGrpId(Integer grpId) {
        return resourceInfoMapper.findResAuthByGrpId(grpId);
    }

    @Override
    public List<ResourceInfo> findResAuthByUserId(Integer userId) {
        return resourceInfoMapper.findResAuthByUserId(userId);
    }

    @Override
    public PageView query(PageView pageView, ResourceInfo resourceInfo) {
        return null;
    }

    @Override
    public long count(ResourceInfo resourceInfo) {
        return 0;
    }

    @Override
    public List<ResourceInfo> queryAll(ResourceInfo resourceInfo) {
        return null;
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(ResourceInfo resourceInfo) throws Exception {
        return null;
    }

    @Override
    public ResourceInfo getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(ResourceInfo resourceInfo) throws Exception {
        return null;
    }
}
