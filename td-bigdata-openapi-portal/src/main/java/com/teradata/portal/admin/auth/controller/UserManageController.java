package com.teradata.portal.admin.auth.controller;

import com.google.common.collect.Maps;
import com.teradata.openapi.framework.util.DateUtils;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.ApiInfoService;
import com.teradata.portal.admin.auth.service.AuthInfoService;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import com.teradata.portal.admin.auth.service.UserInfoService;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.nio.charset.Charset;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/6/27.
 */
@Controller
@RequestMapping(value = "/admin/auth")
public class UserManageController extends BaseController {

    @Inject
    private UserInfoService userInfoService;

    @Inject
    private AuthInfoService authInfoService;

    @Inject
    private UserGrpInfoService userGrpInfoService;



    /**
     * 系统管理--用户管理main
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/userManage.htm")
    public ModelAndView UserManageIndex(HttpServletRequest request, HttpSession httpSession) {
        return new ModelAndView("admin/auth/userManage");
    }

    /**
     * 查询用户全部列表并进行分页
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserInfoList",method = RequestMethod.POST)
    public Map<String,Object> findUserInfoList(HttpServletRequest request,HttpSession httpSession){

        UserInfo userInfo = new UserInfo();
        String jsondata = request.getParameter("aoData");
        JSONArray jsonArray = new JSONArray().fromObject(jsondata);
        String start = "";
        String pageSize = "";
        int  sEcho = 0;

        int userid = -1;
        String loginAcct = "";
        String userName = "";
        String paraid = "";
        String paraType = "";

        for(int i=0;i<jsonArray.size();i++){
            JSONObject obj = (JSONObject) jsonArray.get(i);
            if(obj.get("name").equals("iDisplayStart"))
                start = obj.get("value").toString();
            if(obj.get("name").equals("iDisplayLength"))
                pageSize = obj.get("value").toString();
            if(obj.get("name").equals("sEcho"))
                sEcho = Integer.parseInt(obj.get("value").toString());
            /*if(obj.get("name").equals("userid")){
                if(obj.get("value").toString() != null && !obj.get("value").toString().equals("")){
                    userid = Integer.parseInt(obj.get("value").toString());
                }
            }*/
            if(obj.get("name").equals("loginAcct")){
                loginAcct = obj.get("value").toString();
                userInfo.setLoginAcct(loginAcct);
            }


            if(obj.get("name").equals("userName")){
                userName = obj.get("value").toString();
                userInfo.setUserName(userName);
            }

            if(obj.get("name").equals("paraid"))
                paraid = obj.get("value").toString();
            if(obj.get("name").equals("paraType"))
                paraType = obj.get("value").toString();
        }

        //获得符合条件的记录总数
        long userInfoCount = 0;
        if(paraid != null && !"".equals(paraid)){
            if("1".equals(paraType)){//按用户组查询
                userInfoCount = userInfoService.countQueryAllUserInfoByUserGrpId(Integer.parseInt(paraid));
            }else if("2".equals(paraType)){//按组织机构查询

            }
        }else{
            if("0".equals(paraType)){
                userInfoCount = userInfoService.count(userInfo);
            }
        }


        PageView pageView = new PageView();
        pageView.setRowCount(userInfoCount);//设置总记录数
        pageView.setStartPage(Integer.parseInt(start));//设置开始行索引号
        pageView.setPageSize(Integer.parseInt(pageSize));//设置每页显示的行数
        int page = (int) Math.ceil(Integer.parseInt(start) / Integer.parseInt(pageSize)) + 1; // 页号
        pageView.setPageNow(page);//设置分页开始页

        if(paraid != null && !"".equals(paraid)){
            if("1".equals(paraType)){//按用户组查询
                pageView = userInfoService.queryAllUserInfoByUserGrpIdPage(Integer.parseInt(paraid),pageView);
            }else if("2".equals(paraType)){//按组织机构查询

            }
        }else{
            if("0".equals(paraType)){

                pageView = userInfoService.queryAllPage(userInfo,pageView);
            }
        }

        List<UserInfo> userInfoList = pageView.getRecords();

        JSONArray rs = new JSONArray();
        for(int i=0;i<userInfoList.size();i++){
            JSONArray rr = new JSONArray();
            rr.add(userInfoList.get(i).getUserId());
            rr.add(userInfoList.get(i).getLoginAcct());
            rr.add(userInfoList.get(i).getUserName());
            rr.add(userInfoList.get(i).getUserStat());
            rs.add(rr);
        }

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("iTotalRecords",userInfoCount);//必须是int型
        returnMap.put("iTotalDisplayRecords", userInfoCount);
        returnMap.put("aaData", rs);
        System.out.println("-------count--------"+userInfoCount);
        System.out.println("-------rs------"+rs.size());

        return returnMap;
    }


    /**
     * 新增用户信息
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveUserInfo",method = RequestMethod.POST)
    public ResponseEntity<String> saveUserInfo(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };
        int userId = UUIDUtils.getNotSimple(in,10);
        String loginAcct = request.getParameter("addLoginAcct");
        String userName = request.getParameter("addUserName");
        String loginPwd = request.getParameter("addLoginPwd");
        String gender = request.getParameter("addGender");
        String phone = request.getParameter("addPhone");
        String mail = request.getParameter("addMail");
        String position = request.getParameter("addPosition");
        String userType = request.getParameter("addUserType");
        String registPersn = "admin";
        Date registTiem = DateUtils.parseDate(DateUtils.getDate());
        int userGrade = 1;
        int priorty = 1;

        ResponseEntity<String> re=null;
        String returns = "";
        boolean flagStatus = false;

        UserInfo user = userInfoService.isExist(loginAcct);
        if(user != null){
            returns = new String("用户名已经存在，请更换用户名");
        }else{
            UserInfo userInfo = new UserInfo();
            userInfo.setUserId(userId);
            userInfo.setLoginAcct(loginAcct);
            userInfo.setUserName(userName);
            userInfo.setLoginPwd(loginPwd);
            userInfo.setGender(Integer.parseInt(gender));
            userInfo.setPhone(phone);
            userInfo.setEmail(mail);
            userInfo.setPosition(position);
            userInfo.setUserType(Integer.parseInt(userType));
            userInfo.setRegistPersn(registPersn);
            userInfo.setRegistTime(registTiem);
            userInfo.setUserGrade(userGrade);
            userInfo.setPriority(priorty);
            userInfo.setUserStat("0");
            try {
                int result = userInfoService.add(userInfo);
                if(result > 0){
                    flagStatus = true;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            if(flagStatus){
                returns = new String("保存成功!");
            }else{
                returns = new String("保存失败!");
            }
        }

        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;

    }

    @RequestMapping(value = "/removeUserByLoginAcct",method = RequestMethod.POST)
    public ResponseEntity<String> removeUserByLoginAcct(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        String loginAcct = request.getParameter("loginAcct");

        ResponseEntity<String> re=null;
        String returns = "";
        boolean flagStatus = false;

        try {
            int result = userInfoService.deleteUserByLoginAcct(loginAcct);
            if(result > 0){
                flagStatus = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flagStatus){
            returns = new String("保存成功!");
        }else{
            returns = new String("保存失败!");
        }
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;
    }

    /**
     * 根据用户id查询用户信息
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserInfoByUserId",method = RequestMethod.POST)
    public Map<String,Object> findUserInfoByUserId(HttpServletRequest request,HttpSession httpSession){

        String userId = request.getParameter("userId");
        UserInfo userInfo = userInfoService.getById(Integer.parseInt(userId));
        System.out.println("***********RegistTime************"+userInfo.getRegistTime());
        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("uInfo",userInfo);
        return returnMap;

    }

    @RequestMapping(value = "/updateUserByUserId",method = RequestMethod.POST)
    public ResponseEntity<String> updateUserByUserId(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);


        ResponseEntity<String> re=null;
        String returns = "";
        boolean flagStatus = false;

        String grpId = request.getParameter("grpId");
        String userId = request.getParameter("userId");
        String userName = request.getParameter("userName");
        String orgCode = request.getParameter("orgCode");

        String userGender = request.getParameter("userGender");
        String userPhone = request.getParameter("userPhone");
        String userMail = request.getParameter("userMail");
        String userStat = request.getParameter("userStat");
        String userType = request.getParameter("userType");
        String registPersn = request.getParameter("registPersn");

        String resId = request.getParameter("allResId");
        String apiId = request.getParameter("allApiId");

        UserInfo userInfo = new UserInfo();
        if(userId != null && userId != ""){
            userInfo.setUserId(Integer.parseInt(userId));
            userInfo.setUserName(userName);
            userInfo.setOrgCode(Integer.parseInt(orgCode));
            userInfo.setGender(Integer.parseInt(userGender));
            userInfo.setPhone(userPhone);
            userInfo.setEmail(userMail);
            userInfo.setUserStat(userStat);
            userInfo.setUserType(Integer.parseInt(userType));
            userInfo.setRegistPersn(registPersn);

            try {
                flagStatus = userInfoService.updateAllUserInfo(userInfo,Integer.parseInt(grpId));
                if(flagStatus){
                    //returns = new String("更新信息成功!");
                    int result = authInfoService.saveAuthAllInfoOfUserId(Integer.parseInt(userId),resId,apiId);
                    if(result == 1 || result == 0){
                        returns = new String("更新信息及权限成功!");
                    }else if(result == 2){
                        returns = new String("更新限信息失败!");
                    }
                }else{
                    returns = new String("更新信息失败!");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }

        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;

    }





}
























