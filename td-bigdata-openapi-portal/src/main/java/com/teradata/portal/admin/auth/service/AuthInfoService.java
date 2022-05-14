package com.teradata.portal.admin.auth.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.auth.entity.AuthInfo;

/**
 * Created by Evan on 2016/7/23.
 */

public interface AuthInfoService extends BaseService<AuthInfo> {

    public int deleteAuthInfoByGrpId(int grpId);

    public int deleteAuthInfoByUserId(int userId);

    public int addAuthRes(AuthInfo authInfo);

    public int saveAuthAllInfo(int grpId,String allResId,String allApiId);

    /**
     * 核查权限
     * @param authInfo
     * @return
     */
    public boolean checkAuth(AuthInfo authInfo);

    public int saveAuthAllInfoOfUserId(int userId,String allResId,String allApiId);


}
