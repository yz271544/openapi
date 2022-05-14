package com.teradata.portal.admin.auth.dao;

import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.auth.entity.AuthInfo;

/**
 * Created by Evan on 2016/7/23.
 */

public interface AuthInfoMapper extends BaseMapper<AuthInfo> {


    public int deleteAuthInfoByGrpId(int grpId);

    public int addAuthRes(AuthInfo authInfo);

    public int deleteAuthInfoByUserId(int userId);


}
