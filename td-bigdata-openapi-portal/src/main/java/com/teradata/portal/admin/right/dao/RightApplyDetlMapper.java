package com.teradata.portal.admin.right.dao;

import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.right.entity.Apply2RightDetl;
import com.teradata.portal.admin.right.entity.RightApplyDetl;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/9/5.
 */
public interface RightApplyDetlMapper extends BaseMapper<RightApplyDetl> {

    int updateApplyDetlInfo(Apply2RightDetl apply2RightDetl);

    int deleteByApplyId(Integer applyId);

    List<RightApplyDetl> queryRightApplyDetl(Integer applyId);
}
