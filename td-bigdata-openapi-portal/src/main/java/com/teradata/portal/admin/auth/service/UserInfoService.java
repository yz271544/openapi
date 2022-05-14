package com.teradata.portal.admin.auth.service;

import com.teradata.portal.admin.auth.base.BaseService;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;

import java.util.List;

/**
 * Created by Evan on 2016/7/4.
 */
public interface UserInfoService extends BaseService<UserInfo>{

    public UserInfo querySingleUserInfo(String loginAcct);

    public UserInfo isExist(String loginAcct);

    /**
     * 验证用户登录
     * @param userInfo
     * @return
     */
    public UserInfo countUserInfo(UserInfo userInfo);

    /**
     * 获取所有未匹配组账号
     * @param userInfo
     * @param pageView
     * @return
     */
    public PageView queryNoMatch(UserInfo userInfo, PageView pageView);


    public PageView queryAllPage(UserInfo userInfo, PageView pageView);


    public PageView queryAllUserInfoByUserGrpIdPage(Integer userGrpId,PageView pageView);

    public long countQueryAllUserInfoByUserGrpId(Integer userGrpId);

    /**
     * 通过登录账号删除该用户信息
     * @param loginAcct
     * @return
     */
    public int deleteUserByLoginAcct(String loginAcct);

    /**
     *
     * @param userInfo
     * @param userGrpRelat
     * @return
     */
    public boolean registSave(UserInfo userInfo, UserGrpRelat userGrpRelat);

    /**
     * 更新用户的全部相关信息
     * @param userInfo
     * @param grpId
     * @return
     */
    public boolean updateAllUserInfo(UserInfo userInfo,Integer grpId);




}
