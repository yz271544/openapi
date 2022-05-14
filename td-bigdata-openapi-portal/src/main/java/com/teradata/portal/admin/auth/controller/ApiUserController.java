package com.teradata.portal.admin.auth.controller;

import com.google.common.collect.Maps;
import com.teradata.openapi.framework.util.DateUtils;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import com.teradata.portal.admin.auth.service.UserInfoService;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/25.
 */
@Controller
@RequestMapping(value = "/admin")
public class ApiUserController extends BaseController {

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private UserGrpInfoService userGrpInfoService;

    /**
     * 获得用户组的信息
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserGrpCode",method = RequestMethod.POST)
    public Map<String,Object> getUserGrpCode(HttpServletRequest request, HttpSession httpSession){

        UserGrpInfo userGrpInfo = new UserGrpInfo();
        List<UserGrpInfo> userGrps = userGrpInfoService.queryAll(userGrpInfo);
        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("userGrp",userGrps);
        return returnMap;
    }


    @RequestMapping(value = "/registSave",method = RequestMethod.POST)
    public ResponseEntity<String> registSave(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };
        int userId = UUIDUtils.getNotSimple(in,10);
        String loginAcct = request.getParameter("loginAcct");
        String loginPwd = request.getParameter("loginPwd");
        String appName = request.getParameter("appName");
        String registPsn = request.getParameter("registPsn");
        int userGrpId = Integer.parseInt(request.getParameter("userGrpId"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setLoginAcct(loginAcct);
        userInfo.setLoginPwd(loginPwd);
        userInfo.setUserName(appName);
        userInfo.setUserStat("0");
        userInfo.setRegistPersn(registPsn);
        userInfo.setRegistTime(DateUtils.parseDate(DateUtils.getDate()));
        userInfo.setUserType(1);
        userInfo.setRegistPersn(loginAcct);
        userInfo.setGender(0);
        userInfo.setUserGrade(1);
        userInfo.setPriority(1);

        UserGrpRelat userGrpRelat = new UserGrpRelat();
        userGrpRelat.setUserId(userId);
        userGrpRelat.setUserGrpId(userGrpId);

        ResponseEntity<String> re=null;
        String returns = "";

        boolean flag = userInfoService.registSave(userInfo,userGrpRelat);
        if(flag == true){
            returns = new String("注册成功!");
        }else{
            returns = new String("保存失败!");
        }

        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;

    }


}
