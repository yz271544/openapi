package com.teradata.portal.admin.right.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.right.entity.RightApplyDetl;
import java.util.List;

/**
 * Created by Administrator on 2016/9/5.
 */
public interface RightApplyDetlService extends BaseService<RightApplyDetl> {

    List<ApiInfo> findDetlbyApplyid(Integer applyId, Integer userId, Integer grpId);
    Integer deleteByApplyId(Integer applyId);
}
