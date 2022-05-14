package com.teradata.portal.admin.auth.controller;

import java.nio.charset.Charset;
import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserGrpRelat;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import com.teradata.portal.admin.auth.service.UserInfoService;
import com.teradata.portal.admin.auth.util.PropertiesUtils;
import com.teradata.portal.admin.auth.util.TreeObject;
import com.teradata.portal.admin.auth.util.TreeUtil;
import com.teradata.portal.admin.auth.vo.UserGrpInfoTreeObject;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
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
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/8.
 */
@Controller
@RequestMapping(value = "/admin/auth")
public class UserGrpManageController extends BaseController{

    @Inject
    private UserGrpInfoService userGrpInfoService;

    @Inject
    private UserInfoService userInfoService;


    /**
     * 系统管理--用户组管理main
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/grpManage.htm")
    public ModelAndView grpManageIndex(HttpServletRequest request, HttpSession httpSession){
        return new ModelAndView("admin/auth/groupManage");
    }

    /**
     * 获得用户组树结构
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getUserGrpTree",method = RequestMethod.POST)
    public Map<String,Object> getUserGrpTree(HttpServletRequest request, HttpSession httpSession){

        List<UserGrpInfo> ugis = userGrpInfoService.queryAll(new UserGrpInfo());
        //System.out.println("------ugis size -------"+ugis.size());
        List<UserGrpInfoTreeObject> treeObjects = new ArrayList<UserGrpInfoTreeObject>();
        for(UserGrpInfo ugi : ugis){
            UserGrpInfoTreeObject t = new UserGrpInfoTreeObject();
            try {
                PropertyUtils.copyProperties(t,ugi);
                treeObjects.add(t);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("------treeObjects size -------"+treeObjects.size());
        TreeUtil<UserGrpInfoTreeObject> tu = new TreeUtil<UserGrpInfoTreeObject>();
        List<UserGrpInfoTreeObject> ns = tu.getChildResources(treeObjects,-1);
        String str = JSON.toJSONString(ns);
        //System.out.println("-------all tree json -------"+ str);

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree",str);

        //根据用户id定位选中的用户组id
        String userId = request.getParameter("userId");
        Integer grpId = null;
        if(userId != null && userId != ""){
            UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(Integer.parseInt(userId));
            grpId = userGrpInfo.getId();
            if(grpId != null){
                returnMap.put("gId",grpId);
            }
        }

        return returnMap;
    }

    /**
     * 根据组id查找用户组信息
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/getUserGrpByGrpId",method = RequestMethod.POST)
    public Map<String,Object> getUserGrpByGrpId(HttpServletRequest request, HttpSession httpSession){

        String grpId = request.getParameter("grpId");
        UserGrpInfo userGrp = userGrpInfoService.getById(Integer.parseInt(grpId));

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("grpInfo",userGrp);
        return returnMap;
    }

    /**
     * 更新单个用户组信息
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateUserGrpInfo",method = RequestMethod.POST)
    public ResponseEntity<String> updateUserGrpInfo(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int grpId = Integer.parseInt(request.getParameter("grpId"));
        String grpName = request.getParameter("grpName");
        int  parentId = Integer.parseInt(request.getParameter("parentId"));
        String  grpDesc = request.getParameter("grpDesc");

        UserGrpInfo userGrpInfo = new UserGrpInfo();
        userGrpInfo.setId(grpId);
        userGrpInfo.setName(grpName);
        userGrpInfo.setParentId(parentId);
        userGrpInfo.setDesc(grpDesc);

        ResponseEntity<String> re=null;
        String returns = "";
        boolean flagStatus = false;
        try {
            int result = userGrpInfoService.update(userGrpInfo);
            if(result > 0){
                flagStatus = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if(flagStatus){
            returns = new String("更新成功!");
        }else{
            returns = new String("更新失败!");
        }
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;

    }

    /**
     * 保存用户组信息
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveUserGrpInfo",method = RequestMethod.POST)
    public ResponseEntity<String> saveUserGrpInfo(HttpSession httpSession, HttpServletRequest request){
        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int[] in = { 0,1, 2, 3, 4, 5, 6, 7,8,9 };
        int grpId = UUIDUtils.getNotSimple(in,10);
        //int grpId = Integer.parseInt(UUIDUtils.getRandomNum(10).trim());
        String grpName = request.getParameter("grpName");
        int  parentId = Integer.parseInt(request.getParameter("parentId"));
        String  grpDesc = request.getParameter("grpDesc");

        UserGrpInfo userGrpInfo = new UserGrpInfo();
        userGrpInfo.setId(grpId);
        userGrpInfo.setName(grpName);
        userGrpInfo.setParentId(parentId);
        userGrpInfo.setDesc(grpDesc);

        ResponseEntity<String> re=null;
        String returns = "";
        boolean flagStatus = false;
        try {
            int result = userGrpInfoService.add(userGrpInfo);
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
     * 删除用户组信息
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/removeUserGrpInfo",method = RequestMethod.POST)
    public ResponseEntity<String> removeUserGrpInfo(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int grpId = Integer.parseInt(request.getParameter("grpId"));

        ResponseEntity<String> re=null;
        String returns = "";
        //删除用户组前，先查找该用户组下是否还有子用户组，如果还有则不能进行删除。直接返回
        try {
            List<UserGrpInfo> userGrpInfoList = userGrpInfoService.findAllSubUserGrpById(grpId);

            System.out.println("-----节点id：------"+grpId + " 下的子节点的个数:"+userGrpInfoList.size());

            if(userGrpInfoList.size() == 1){
                int result = userGrpInfoService.delete(grpId);
                if(result > 0){
                    returns = new String("删除成功!");
                }else{
                    returns = new String("删除失败!");
                }
            }else{
                returns = new String("该用户组下还有子用户组，不能进行删除!");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);

        return re;

    }

    /**
     * 通过用户组id查找对应的用户集合
     * @param request
     * @param httpSession
     * @return
     */
    @ResponseBody
    @RequestMapping(value = "/findUserListByGrpId",method = RequestMethod.POST)
    public Map<String,Object> findUserListByGrpId(HttpServletRequest request,HttpSession httpSession){

        UserInfo userInfo = new UserInfo();
        String jsondata = request.getParameter("aoData");
        JSONArray jsonArray = new JSONArray().fromObject(jsondata);
        String start = "";
        String pageSize = "";
        int  sEcho = 0;

        int grpId = -1;

        for(int i=0;i<jsonArray.size();i++){
            JSONObject obj = (JSONObject) jsonArray.get(i);
            if(obj.get("name").equals("iDisplayStart"))
                start = obj.get("value").toString();
            if(obj.get("name").equals("iDisplayLength"))
                pageSize = obj.get("value").toString();
            if(obj.get("name").equals("sEcho"))
                sEcho = Integer.parseInt(obj.get("value").toString());
            if(obj.get("name").equals("grpid")){
                grpId = Integer.parseInt(obj.get("value").toString());
            }
        }
        //获得符合条件的记录总数
        long userInfoCount = 0;
        userInfoCount = userInfoService.countQueryAllUserInfoByUserGrpId(grpId);
        PageView pageView = new PageView();
        pageView.setRowCount(userInfoCount);//设置总记录数
        pageView.setStartPage(Integer.parseInt(start));//设置开始行索引号
        pageView.setPageSize(Integer.parseInt(pageSize));//设置每页显示的行数
        int page = (int) Math.ceil(Integer.parseInt(start) / Integer.parseInt(pageSize)) + 1; // 页号
        pageView.setPageNow(page);//设置分页开始页
        pageView = userInfoService.queryAllUserInfoByUserGrpIdPage(grpId,pageView);

        List<UserInfo> userInfoList = pageView.getRecords();

        JSONArray rs = new JSONArray();
        for(int i=0;i<userInfoList.size();i++){
            JSONArray rr = new JSONArray();
            rr.add(userInfoList.get(i).getUserId());
            rr.add(userInfoList.get(i).getLoginAcct());
            rr.add(userInfoList.get(i).getUserName());
            rr.add(userInfoList.get(i).getUserStat());
            rr.add(userInfoList.get(i).getUserGrpName());
            rs.add(rr);
        }

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("iTotalRecords",userInfoCount);//必须是int型
        returnMap.put("iTotalDisplayRecords", userInfoCount);
        returnMap.put("aaData", rs);

        return returnMap;


    }

    /**
     * 修改用户对应的用户组关系
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/updateUserGrpRelat",method = RequestMethod.POST)
    public ResponseEntity<String> updateUserGrpRelat(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);

        int grpId = Integer.parseInt(request.getParameter("grpId"));
        int userId = Integer.parseInt(request.getParameter("loginAcct"));
        UserGrpRelat userGrpRelat = new UserGrpRelat();
        userGrpRelat.setUserId(userId);
        userGrpRelat.setUserGrpId(grpId);
        boolean flag = userGrpInfoService.updateUserGrpRelat(userGrpRelat);

        ResponseEntity<String> re=null;
        String returns = "";
        if(flag == true){
            returns = new String("修改用户组成功!");
        }else{
            returns = new String("修改用户组失败!");
        }
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);
        return re;

    }

    /**
     * 删除用户对应用户组关系
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/deleteUserGrpRelat",method = RequestMethod.POST)
    public ResponseEntity<String> deleteUserGrpRelat(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);
        int userId = Integer.parseInt(request.getParameter("userId"));
        int flag = userGrpInfoService.deleteUserGrpRelat(userId);

        ResponseEntity<String> re=null;
        String returns = "";
        if(flag > 0){
            returns = new String("删除用户组成功!");
        }else{
            returns = new String("删除用户组失败!");
        }
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);
        return re;

    }

}


















