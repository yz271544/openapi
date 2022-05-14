package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;

/**
 * 用户和用户组关系
 * Created by Evan on 2016/7/3.
 */
@SuppressWarnings("serial")
public class UserGrpRelat implements Serializable {

    private int userGrpId;

    private int userId;

    public int getUserGrpId() {
        return userGrpId;
    }

    public void setUserGrpId(int userGrpId) {
        this.userGrpId = userGrpId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
}
