package com.teradata.portal.admin.auth.controller;

import com.teradata.openapi.framework.util.DateUtils;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.base.BaseController;
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

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;

/**
 * Created by Evan on 2016/7/26.
 */
@Controller
@RequestMapping(value = "/admin/apiDeveloper")
public class ApiDeveloperController extends BaseController {

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private UserGrpInfoService userGrpInfoService;

    @RequestMapping(value = "/registSave",method = RequestMethod.POST)
    public ResponseEntity<String> registSave(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };
        int userId = UUIDUtils.getNotSimple(in,10);
        String loginAcct = request.getParameter("loginAcct");
        String loginPwd = request.getParameter("loginPwd");
        String realUserName = request.getParameter("realUserName");
        String orgCode = request.getParameter("orgCode");
        String userGender = request.getParameter("userGender");
        String userPhone = request.getParameter("userPhone");
        String userMail = request.getParameter("userMail");
        int userGrpId = Integer.parseInt(request.getParameter("userGrpId"));
        UserInfo userInfo = new UserInfo();
        userInfo.setUserId(userId);
        userInfo.setLoginAcct(loginAcct);
        userInfo.setLoginPwd(loginPwd);
        userInfo.setUserName(realUserName);
        userInfo.setOrgCode(Integer.parseInt(orgCode));
        userInfo.setUserStat("0");
        userInfo.setGender(Integer.parseInt(userGender));
        userInfo.setPhone(userPhone);
        userInfo.setEmail(userMail);
        userInfo.setUserType(2);
        userInfo.setRegistTime(DateUtils.parseDate(DateUtils.getDate()));
        userInfo.setGender(0);
        userInfo.setRegistPersn(loginAcct);
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
