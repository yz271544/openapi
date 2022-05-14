package com.teradata.portal.admin.auth.dao;

import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.portal.admin.auth.base.BaseMapper;
import com.teradata.portal.admin.auth.entity.UserInfo;

import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/4.
 */
public interface UserInfoMapper extends BaseMapper<UserInfo> {

    UserInfo querySingleAccount(String userInfo);

    UserInfo isExist(String userInfo);

    UserInfo countUserInfo(UserInfo userInfo);

    List<UserInfo> queryNoMatch(Map<String, Object> map);

    long count(UserInfo userInfo);

    List<UserInfo> queryAllPage(Map<String, Object> map);

    /**
     * 根据用户组id查找全部用户信息
     * @param map
     * @return
     */
    List<UserInfo> queryAllUserInfoByUserGrpIdPage(Map<String, Object> map);

    long countQueryAllUserInfoByUserGrpId(Map<String, Object> map);

    /**
     * 通过登录账号删除该用户信息
     * @param loginAcct
     * @return
     */
    int deleteUserByLoginAcct(String loginAcct);

    List<UserInfo> getAuditUsers(char userStat);

    List<Map<String, Object>> queryRegExamStat();


    /*
	 * 获取注册工号列表信息
	 */
    List<?> queryRegInfo(Map<String, Object> param, PageBounds pageBounds);
    /*
     * 更新用户审批状态
     */
    int updateUserStatInfo(UserInfo user);
}
