package com.teradata.portal.admin.auth.dao;

import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/8.
 */
public interface UserGrpInfoMapper  extends BaseMapper<UserGrpInfo>{

    public UserGrpInfo isExist(String name);

    //通过userid查找对应的用户组信息
    public UserGrpInfo findbyUserGrpRelat(Integer userId);

    //增加用户和用户组关系信息
    public int addUserGrpRelat(UserGrpRelat userGrpRelat);

    public int deleteUserGrpRelat(Integer userId);

    public long count(UserGrpRelat userGrpRelat);

    public List<UserGrpInfo> findAllPage(Map<String, Object> map);

    /**
     * 采用递归的方式，根据某一个userGrpId找到全部子id
     * @param userGrpId
     * @return
     */
    public List<UserGrpInfo> findAllSubUserGrpById(Integer userGrpId);

}
