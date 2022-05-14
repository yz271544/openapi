package com.teradata.portal.admin.right.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.right.entity.RightApplyInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/9/5.
 */
public interface RightApplyInfoMapper extends BaseMapper<RightApplyInfo>{

    RightApplyInfo isExist(Integer applyId);

    List<Map<String, Object>> queryAuthAuditStat();

    /*
     * 删除api
     */
    int deleteByApplyId(Integer applyId);
    /*
     * 获取rightApprInfo列表信息
     */
    List<?> queryRightApprInfo(Map<String, Object> param, PageBounds pageBounds);

    /*
     * 获取rightApplyInfo列表信息
     */
    List<?> queryRightApplyInfo(Map<String, Object> param, PageBounds pageBounds);

    /*
     * 更新用户审批状态
     */
    int updateAuditStat(RightApplyInfo rightApplyInfo);
}
