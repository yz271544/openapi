package com.teradata.portal.admin.auth.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;

import java.util.List;

/**
 * Created by Evan on 2016/7/8.
 */
public interface UserGrpInfoService extends BaseService<UserGrpInfo> {

    public UserGrpInfo isExist(String userGrpName);

    public UserGrpInfo findbyUserGrpRelat(Integer userId);

    public int addUserGrpRelat(UserGrpRelat userGrpRelat);

    public int deleteUserGrpRelat(Integer userId);

    public long count(UserGrpRelat userGrpRelat);

    public List<UserGrpInfo> findAllPage(UserGrpInfo userGrpInfo, PageView pageView);

    public List<UserGrpInfo> findAllSubUserGrpById(Integer userGrpId);

    /**
     * 修改用户对应的用户组
     * @param userGrpRelat
     * @return
     */
    public boolean updateUserGrpRelat(UserGrpRelat userGrpRelat);



}
