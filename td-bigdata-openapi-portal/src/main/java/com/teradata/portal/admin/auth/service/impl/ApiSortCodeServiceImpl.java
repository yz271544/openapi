package com.teradata.portal.admin.auth.service.impl;

import com.teradata.portal.admin.auth.dao.ApiSortCodeMapper;
import com.teradata.portal.admin.auth.entity.ApiSortCode;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.ApiSortCodeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Evan on 2016/7/22.
 */
@Transactional
@Service("apiSortCodeService")
public class ApiSortCodeServiceImpl implements ApiSortCodeService {

    @Autowired
    private ApiSortCodeMapper apiSortCodeMapper;

    @Override
    public PageView query(PageView pageView, ApiSortCode apiSortCode) {
        return null;
    }

    @Override
    public long count(ApiSortCode apiSortCode) {
        return 0;
    }

    @Override
    public List<ApiSortCode> queryAll(ApiSortCode apiSortCode) {

        return apiSortCodeMapper.queryAll(apiSortCode);
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(ApiSortCode apiSortCode) throws Exception {
        return null;
    }

    @Override
    public ApiSortCode getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(ApiSortCode apiSortCode) throws Exception {
        return null;
    }
}
