package com.teradata.portal.admin.auth.service.impl;

import com.teradata.portal.admin.auth.dao.UserGrpInfoMapper;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Evan on 2016/7/8.
 */

@Transactional
@Service("userGrpInfoService")
public class UserGrpInfoServiceImpl implements UserGrpInfoService {

    @Autowired
    private UserGrpInfoMapper userGrpInfoMapper;

    @Override
    public UserGrpInfo isExist(String userGrpName) {
        return null;
    }

    @Override
    public UserGrpInfo findbyUserGrpRelat(Integer userId) {

        return userGrpInfoMapper.findbyUserGrpRelat(userId);
    }

    @Override
    public int addUserGrpRelat(UserGrpRelat userGrpRelat) {

        return userGrpInfoMapper.addUserGrpRelat(userGrpRelat);
    }

    @Override
    public int deleteUserGrpRelat(Integer userId) {

        return userGrpInfoMapper.deleteUserGrpRelat(userId);
    }

    @Override
    public long count(UserGrpRelat userGrpRelat) {
        return 0;
    }

    @Override
    public List<UserGrpInfo> findAllPage(UserGrpInfo userGrpInfo, PageView pageView) {
        return null;
    }

    @Override
    public List<UserGrpInfo> findAllSubUserGrpById(Integer userGrpId) {
        return userGrpInfoMapper.findAllSubUserGrpById(userGrpId);
    }

    /**
     * 修改用户对应的用户组
     * @param userGrpRelat
     * @return
     */
    @Override
    public boolean updateUserGrpRelat(UserGrpRelat userGrpRelat) {
        boolean resultFlag = false;
        int delUserGrpRelatResult = this.deleteUserGrpRelat(userGrpRelat.getUserId());
        if(delUserGrpRelatResult > 0){
            int addUserGrpRelatResult = this.addUserGrpRelat(userGrpRelat);
            if(addUserGrpRelatResult > 0){
                resultFlag = true;
            }
        }
        return resultFlag;
    }

    @Override
    public PageView query(PageView pageView, UserGrpInfo userGrpInfo) {
        return null;
    }

    @Override
    public long count(UserGrpInfo userGrpInfo) {
        return 0;
    }

    @Override
    public List<UserGrpInfo> queryAll(UserGrpInfo userGrpInfo) {

        return userGrpInfoMapper.queryAll(userGrpInfo);

    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return userGrpInfoMapper.delete(id);
    }

    @Override
    public Integer update(UserGrpInfo userGrpInfo) throws Exception {
        return userGrpInfoMapper.update(userGrpInfo);
    }

    @Override
    public UserGrpInfo getById(Integer id) {

        return userGrpInfoMapper.getById(id);
    }

    @Override
    public Integer add(UserGrpInfo userGrpInfo) throws Exception {
        return userGrpInfoMapper.add(userGrpInfo);
    }
}
