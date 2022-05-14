package com.teradata.portal.admin.auth.service.impl;

import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.dao.UserGrpInfoMapper;
import com.teradata.portal.admin.auth.dao.UserInfoMapper;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/4.
 */

@Transactional
@Service("userInfoService")
public class UserInfoServiceImpl implements UserInfoService {

    @Autowired
    private UserInfoMapper userInfoMapper;

    @Autowired
    private UserGrpInfoMapper userGrpInfoMapper;


    @Override
    public UserInfo querySingleUserInfo(String loginAcct) {
        return null;
    }

    @Override
    public UserInfo isExist(String loginAcct) {
        return userInfoMapper.isExist(loginAcct);
    }

    @Override
    public UserInfo countUserInfo(UserInfo userInfo) {

        return null;
    }

    @Override
    public PageView queryNoMatch(UserInfo userInfo, PageView pageView) {
        return null;
    }

    @Override
    public PageView queryAllPage(UserInfo userInfo, PageView pageView) {

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paging", pageView);
        map.put("t", userInfo);
        List<UserInfo> list = userInfoMapper.queryAllPage(map);
        pageView.setRecords(list);
        return pageView;
    }

    /**
     * 根据用户组id查找全部用户信息
     * @param
     * @return
     */
    @Override
    public PageView queryAllUserInfoByUserGrpIdPage(Integer userGrpId,PageView pageView) {

        //根据用户组id查询出当前全部的子节点的用户组id
        List<UserGrpInfo> userGrpInfoList = userGrpInfoMapper.findAllSubUserGrpById(userGrpId);
        List<Integer> userGrpIds = new ArrayList<Integer>();
        for(UserGrpInfo userGrpInfo : userGrpInfoList){
            Integer subUserGrpId = userGrpInfo.getId();
            userGrpIds.add(subUserGrpId);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("paging", pageView);
        map.put("userGrpIds", userGrpIds);
        List<UserInfo> list = userInfoMapper.queryAllUserInfoByUserGrpIdPage(map);
        pageView.setRecords(list);
        return pageView;
    }

    /**
     * 根据用户组id查找全部用户信息的记录总数
     * @param userGrpId
     * @return
     */
    @Override
    public long countQueryAllUserInfoByUserGrpId(Integer userGrpId) {
        List<UserGrpInfo> userGrpInfoList = userGrpInfoMapper.findAllSubUserGrpById(userGrpId);
        List<Integer> userGrpIds = new ArrayList<Integer>();
        for(UserGrpInfo userGrpInfo : userGrpInfoList){
            Integer subUserGrpId = userGrpInfo.getId();
            userGrpIds.add(subUserGrpId);
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("userGrpIds", userGrpIds);
        return userInfoMapper.countQueryAllUserInfoByUserGrpId(map);
    }

    @Override
    public int deleteUserByLoginAcct(String loginAcct) {
        return userInfoMapper.deleteUserByLoginAcct(loginAcct);
    }

    @Override
    public boolean registSave(UserInfo userInfo, UserGrpRelat userGrpRelat) {

        boolean flag = false;

        try {
            int saveUserFlag = userInfoMapper.add(userInfo);
            if(saveUserFlag > 0){
                int saveUserGrpRelat = userGrpInfoMapper.addUserGrpRelat(userGrpRelat);
                if(saveUserGrpRelat > 0){
                    flag = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    @Override
    public boolean updateAllUserInfo(UserInfo userInfo, Integer grpId) {

        boolean flag = false;
        try {
            //更新用户的详细信息
            int result = this.update(userInfo);
            if(result > 0){
                //更新用户和用户组关系信息
                int f1 = userGrpInfoMapper.deleteUserGrpRelat(userInfo.getUserId());
                if(f1 > 0){
                    UserGrpRelat userGrpRelat = new UserGrpRelat();
                    userGrpRelat.setUserGrpId(grpId);
                    userGrpRelat.setUserId(userInfo.getUserId());
                    int f2 = userGrpInfoMapper.addUserGrpRelat(userGrpRelat);
                    if(f2 > 0){
                        flag = true;
                    }
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return flag;
    }

    @Override
    public PageView query(PageView pageView, UserInfo userInfo) {

        Map<String,Object> map = new HashMap<String,Object>();
        map.put("paging",pageView);
        map.put("t",userInfo);
        List<UserInfo> list = userInfoMapper.query(map);
        pageView.setRecords(list);
        return pageView;
    }

    @Override
    public long count(UserInfo userInfo) {

        return userInfoMapper.count(userInfo);
    }

    @Override
    public List<UserInfo> queryAll(UserInfo userInfo) {

        return userInfoMapper.queryAll(userInfo);
    }

    @Override
    public Integer delete(Integer id) throws Exception {

        return 0;

    }

    @Override
    public Integer update(UserInfo userInfo) throws Exception {
        return userInfoMapper.update(userInfo);
    }

    @Override
    public UserInfo getById(Integer id) {
        return userInfoMapper.getById(id);
    }

    @Override
    public Integer add(UserInfo userInfo) throws Exception {
        return userInfoMapper.add(userInfo);
    }
}
