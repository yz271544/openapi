package com.teradata.portal.admin.right.service.impl;

import com.teradata.portal.admin.auth.dao.ApiInfoMapper;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.right.dao.RightApplyDetlMapper;
import com.teradata.portal.admin.right.entity.RightApplyDetl;
import com.teradata.portal.admin.right.service.RightApplyDetlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Evan on 2016/9/5.
 */

@Transactional
@Service("rightApplyDetlService")
public class RightApplyDetlServiceImpl implements RightApplyDetlService {

    @Autowired
    private RightApplyDetlMapper rightApplyDetlMapper;

    @Autowired
    private ApiInfoMapper apiInfoMapper;
    @Override
    public PageView query(PageView pageView, RightApplyDetl rightApplyDetl) {
        return null;
    }

    @Override
    public long count(RightApplyDetl rightApplyDetl) {
        return 0;
    }

    @Override
    public List<RightApplyDetl> queryAll(RightApplyDetl rightApplyDetl) {
        return null;
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(RightApplyDetl rightApplyDetl) throws Exception {
        return null;
    }

    @Override
    public RightApplyDetl getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(RightApplyDetl rightApplyDetl) throws Exception {
        return rightApplyDetlMapper.add(rightApplyDetl);
    }

    @Override
    public List<ApiInfo> findDetlbyApplyid(Integer applyId,Integer userId, Integer grpId) {
        return apiInfoMapper.findApiInfoAuthByApplyId(applyId,userId,grpId);
    }

    @Override
    public Integer deleteByApplyId(Integer applyId) {
        return rightApplyDetlMapper.deleteByApplyId(applyId);
    }
}
