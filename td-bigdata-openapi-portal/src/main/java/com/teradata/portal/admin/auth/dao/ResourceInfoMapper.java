package com.teradata.portal.admin.auth.dao;

import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.auth.entity.ResourceInfo;

import java.util.List;

/**
 * Created by Evan on 2016/7/13.
 */

public interface ResourceInfoMapper extends BaseMapper<ResourceInfo>{

    /**
     * 根据组Id查询页面资源
     * @param grpId
     * @return
     */
    public List<ResourceInfo> findResInfoByGrpId(Integer grpId);

    /**
     * 根据登录账号查询页面资源
     * @param loginAcct
     * @return
     */
    public List<ResourceInfo> findResInfoByLoginAcct(String loginAcct);

    /**
     * 根据组id查找对应的权限资源信息，如果有则显示勾选状态
     * @param grpId
     * @return
     */
    public List<ResourceInfo> findResAuthByGrpId(Integer grpId);

    /**
     * 根据用户id查找对应的权限资源信息，如果有则显示勾选状态
     * @param userId
     * @return
     */
    public List<ResourceInfo> findResAuthByUserId(Integer userId);

}
