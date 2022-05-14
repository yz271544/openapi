package com.teradata.portal.admin.auth.dao;

import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/22.
 */

public interface ApiInfoMapper extends BaseMapper<ApiInfo> {


    /**
     * 根据组id查找对应的权限Api信息，如果有则显示勾选状态
     * @param grpId
     * @return
     */
    List<ApiInfo> findApiInfoAuthByGrpId(Integer grpId);

    /**
     * 根据用户id查找对应的权限Api信息，如果有则显示勾选状态
     * @param userId
     * @return
     */
    List<ApiInfo> findApiInfoAuthByUserId(Integer userId);

    /**
     * 根据领域值得id查询相关API列表
     * @param map
     * @return
     */
    List<ApiInfo> findApiAuthByIdMaps(Map<String, Object> map);

    /**
     * 根据申请单id、用户id、组id查找对应的权限Api信息，如果有则显示勾选状态
     * @param applyId
     * @param userId
     * @param grpId
     * @return
     */
    List<ApiInfo> findApiInfoAuthByApplyId(@Param("applyId") Integer applyId, @Param("userId") Integer userId, @Param("grpId") Integer grpId);
}
