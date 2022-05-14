package com.teradata.portal.admin.auth.service.impl;

import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.dao.AuthInfoMapper;
import com.teradata.portal.admin.auth.entity.AuthInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.AuthInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

/**
 * Created by Evan on 2016/7/23.
 */

@Transactional
@Service("authInfoService")
public class AuthInfoServiceImpl implements AuthInfoService {

    @Autowired
    private AuthInfoMapper authInfoMapper;

    @Override
    public int deleteAuthInfoByGrpId(int grpId) {
        return authInfoMapper.deleteAuthInfoByGrpId(grpId);
    }

    @Override
    public int deleteAuthInfoByUserId(int userId) {
        return authInfoMapper.deleteAuthInfoByUserId(userId);
    }

    @Override
    public int addAuthRes(AuthInfo authInfo) {
        return authInfoMapper.addAuthRes(authInfo);
    }

    /**
     * 保存先关权限的全部信息
     * @param grpId
     * @param allResId
     * @param allApiId
     * @return
     */
    @Override
    public int saveAuthAllInfo(int grpId, String allResId, String allApiId) {
        /**
         * 1 表示 保存成功
         * 2 表示 删除权限信息失败
         * 3 表示 保全对应的权限信息失败
         */
        int resultFlag = 0;
        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };
        //删除用户组id对应的权限信息
        //int delResutl = this.deleteAuthInfoByGrpId(grpId);

        if(allResId != null && !allResId.equals("") && allApiId != null && !allApiId.equals("")){
            int deleteResult = this.deleteAuthInfoByGrpId(grpId);
            System.out.println("--------删除的记录数----------"+deleteResult);
            String[] resIds = allResId.split(",");
            int addResNum = 0; //计数器
            for(int i = 0;i<resIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("group");
                authInfo.setAuthMainValue(grpId);
                authInfo.setAuthDomain("menu");
                authInfo.setAuthDomainValue(Integer.parseInt(resIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addResNum += 1;
            }

            String[] apiIds = allApiId.split(",");
            int addApiNum = 0;
            for(int i = 0;i<apiIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("group");
                authInfo.setAuthMainValue(grpId);
                authInfo.setAuthDomain("api");
                authInfo.setAuthDomainValue(Integer.parseInt(apiIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addApiNum += 1;
            }

            if(resIds.length == addResNum && apiIds.length == addApiNum ){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }


        }else if(allApiId != null && !allApiId.equals("")){

            this.deleteAuthInfoByGrpId(grpId);
            String[] resIds = allResId.split(",");
            int addResNum = 0; //计数器
            for(int i = 0;i<resIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("group");
                authInfo.setAuthMainValue(grpId);
                authInfo.setAuthDomain("menu");
                authInfo.setAuthDomainValue(Integer.parseInt(resIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addResNum += 1;
            }

            if(resIds.length == addResNum){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }

        }else if(allResId != null && !allResId.equals("")){

            String[] apiIds = allApiId.split(",");
            int addApiNum = 0;
            for(int i = 0;i<apiIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("group");
                authInfo.setAuthMainValue(grpId);
                authInfo.setAuthDomain("api");
                authInfo.setAuthDomainValue(Integer.parseInt(apiIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addApiNum += 1;
            }

            if(apiIds.length == addApiNum){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }

        }


        return resultFlag;
    }

    /**
     * 核查权限
     * @param authInfo
     * @return
     */
    @Override
    public boolean checkAuth(AuthInfo authInfo) {

        boolean flag = false;
        List<AuthInfo> list = authInfoMapper.queryAll(authInfo);
        if(list.size() > 0){
            flag = true;
        }

        return flag;
    }

    @Override
    public int saveAuthAllInfoOfUserId(int userId, String allResId, String allApiId) {
        /**
         * 1 表示 保存成功
         * 2 表示 删除权限信息失败
         * 3 表示 保全对应的权限信息失败
         */
        int resultFlag = 0;
        //删除用户组id对应的权限信息
        //int delResutl = this.deleteAuthInfoByUserId(userId);
        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };

        if(allResId != null && !allResId.equals("") && allApiId != null && !allApiId.equals("")){
            this.deleteAuthInfoByUserId(userId);
            String[] resIds = allResId.split(",");
            int addResNum = 0;
            for(int i = 0;i<resIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("user");
                authInfo.setAuthMainValue(userId);
                authInfo.setAuthDomain("menu");
                authInfo.setAuthDomainValue(Integer.parseInt(resIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addResNum += 1;
            }

            String[] apiIds = allApiId.split(",");
            int addApiNum = 0;
            for(int i = 0;i<apiIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("user");
                authInfo.setAuthMainValue(userId);
                authInfo.setAuthDomain("api");
                authInfo.setAuthDomainValue(Integer.parseInt(apiIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addApiNum += 1;
            }
            if(resIds.length == addResNum && apiIds.length == addApiNum ){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }
        }else if(allApiId != null && !allApiId.equals("")){
            System.out.println("&&&&&&"+allApiId);
            this.deleteAuthInfoByUserId(userId);
            String[] apiIds = allApiId.split(",");
            int addApiNum = 0;
            for(int i = 0;i<apiIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("user");
                authInfo.setAuthMainValue(userId);
                authInfo.setAuthDomain("api");
                authInfo.setAuthDomainValue(Integer.parseInt(apiIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addApiNum += 1;
            }

            if(apiIds.length == addApiNum){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }
        }else if(allResId != null && !allResId.equals("")){
            this.deleteAuthInfoByUserId(userId);
            String[] resIds = allResId.split(",");
            int addResNum = 0;
            for(int i = 0;i<resIds.length;i++){
                AuthInfo authInfo = new AuthInfo();
                authInfo.setAuthId(UUIDUtils.getNotSimple(in,10));
                authInfo.setAuthMain("user");
                authInfo.setAuthMainValue(userId);
                authInfo.setAuthDomain("menu");
                authInfo.setAuthDomainValue(Integer.parseInt(resIds[i]));
                authInfoMapper.addAuthRes(authInfo);
                addResNum += 1;
            }
            if(resIds.length == addResNum){
                resultFlag = 1;
            }else{
                resultFlag = 2;
            }
        }

        return resultFlag;
    }

    @Override
    public PageView query(PageView pageView, AuthInfo authInfo) {
        return null;
    }

    @Override
    public long count(AuthInfo authInfo) {
        return 0;
    }

    @Override
    public List<AuthInfo> queryAll(AuthInfo authInfo) {
        return null;
    }

    @Override
    public Integer delete(Integer id) throws Exception {
        return null;
    }

    @Override
    public Integer update(AuthInfo authInfo) throws Exception {
        return null;
    }

    @Override
    public AuthInfo getById(Integer id) {
        return null;
    }

    @Override
    public Integer add(AuthInfo authInfo) throws Exception {
        return null;
    }
}
