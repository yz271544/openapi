package com.teradata.portal.admin.auth.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.*;
import com.teradata.portal.admin.auth.service.*;
import com.teradata.portal.admin.auth.util.TreeUtil;
import com.teradata.portal.admin.auth.vo.ApiInfoTreeObject;
import com.teradata.portal.admin.auth.vo.SysMenuInfoTreeObject;
import com.teradata.portal.admin.auth.vo.UserGrpInfoTreeObject;
import org.apache.commons.beanutils.PropertyUtils;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Evan on 2016/7/20.
 */
@Controller
@RequestMapping(value = "/admin/auth")
public class ResourceManageController extends BaseController {

    @Inject
    private ResourceInfoService resourceInfoService;

    @Inject
    private ApiInfoService apiInfoService;

    @Inject
    private ApiSortCodeService apiSortCodeService;

    @Inject
    private AuthInfoService authInfoService;

    @Inject
    private UserGrpInfoService userGrpInfoService;


    /**
     * 获得菜单树结构
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getResourceAuthTree",method = RequestMethod.POST)
    public Map<String,Object> getResourceAuthTree(HttpServletRequest request, HttpSession httpSession){

        String grpId = request.getParameter("grpId");
        List<ResourceInfo> resourceInfoList = resourceInfoService.findResAuthByGrpId(Integer.parseInt(grpId));
        //System.out.println("------ugis size -------"+ugis.size());
        List<SysMenuInfoTreeObject> treeObjects = new ArrayList<SysMenuInfoTreeObject>();
        for(ResourceInfo resinfo : resourceInfoList){
            SysMenuInfoTreeObject t = new SysMenuInfoTreeObject();
            try {
                PropertyUtils.copyProperties(t,resinfo);
                treeObjects.add(t);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        //System.out.println("------treeObjects size -------"+treeObjects.size());
        TreeUtil<SysMenuInfoTreeObject> tu = new TreeUtil<SysMenuInfoTreeObject>();
        List<SysMenuInfoTreeObject> ns = tu.getChildResources(treeObjects,-1);
        String str = JSON.toJSONString(ns);
        System.out.println("-------all tree json -------"+ str);

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree",str);
        return returnMap;
    }


    /**
     * 获得API树结构
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getApiInfoAuthTree",method = RequestMethod.POST)
    public Map<String,Object> getApiInfoAuthTree(HttpServletRequest request, HttpSession httpSession){

        String grpId = request.getParameter("grpId");
        List<ApiInfo> apiInfoList = apiInfoService.findApiInfoAuthByGrpId(Integer.parseInt(grpId));


        List<ApiInfoTreeObject> treeObjects = new ArrayList<ApiInfoTreeObject>();
        for(ApiInfo apiInfo : apiInfoList){
            ApiInfoTreeObject t = new ApiInfoTreeObject();
            try {
                PropertyUtils.copyProperties(t,apiInfo);
                treeObjects.add(t);
            }catch (Exception e) {
                e.printStackTrace();
            }
        }

        TreeUtil<ApiInfoTreeObject> tu = new TreeUtil<ApiInfoTreeObject>();
        /*List<ApiInfoTreeObject> finalList = new ArrayList<ApiInfoTreeObject>();
        ApiSortCode apiSortCode = new ApiSortCode();
        List<ApiSortCode> apiSortCodeList = apiSortCodeService.queryAll(apiSortCode);
        for(ApiSortCode apiSort : apiSortCodeList){
            List<ApiInfoTreeObject> ns = tu.getChildResources(treeObjects,apiSort.getApiSort());
            finalList.addAll(ns);
        }

        String str = JSON.toJSONString(finalList);*/

        List<ApiInfoTreeObject> ns = tu.getChildResources(treeObjects,0);
        String str = JSON.toJSONString(ns);
        System.out.println("-------all tree json -------"+ str);
        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree",str);
        return returnMap;

    }

    /**
     * 保存用户组的权限信息
     * @param httpSession
     * @param request
     * @return
     */
    @RequestMapping(value = "/saveAuthInfo",method = RequestMethod.POST)
    public ResponseEntity<String> saveAuthInfo(HttpSession httpSession, HttpServletRequest request){

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);
        int grpId = Integer.parseInt(request.getParameter("grpId"));
        String resId = request.getParameter("allResId");
        String apiId = request.getParameter("allApiId");
        System.out.println("==========resId=========="+resId);
        System.out.println("==========apiId=========="+apiId);
        int result = authInfoService.saveAuthAllInfo(grpId,resId,apiId);
        ResponseEntity<String> re=null;
        String returns = "";

        if(result == 1 || result == 0){
            returns = new String("保存权限成功!");
        }else if(result == 2){
            returns = new String("保存权限失败!");
        }
        System.out.println("==========保存组对应权限的状态==========>"+returns);
        re = new ResponseEntity<String>(returns,responseHeaders, HttpStatus.OK);
        return re;

    }

    /**
     * 获得菜单树结构(个人账号权限和用户组权限并集)
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getUnioResourceAuthTree",method = RequestMethod.POST)
    public Map<String,Object> getUnioResourceAuthTree(HttpServletRequest request, HttpSession httpSession){

        String userId = request.getParameter("userId");
        Integer grpId = null;
        String str = "";
        if(userId != null && userId != ""){

            AuthInfo authInfo = new AuthInfo();
            authInfo.setAuthMain("user");
            authInfo.setAuthMainValue(Integer.parseInt(userId));
            authInfo.setAuthDomain("menu");
            boolean flag = authInfoService.checkAuth(authInfo);
            List<ResourceInfo> resourceInfoList = new ArrayList<ResourceInfo>();

            /*if(flag == true){
                //根据用户id获取资源权限
                resourceInfoList = resourceInfoService.findResAuthByUserId(Integer.parseInt(userId));
            }else{
                //根据userid 查找 用户组id
                UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(Integer.parseInt(userId));
                if(userGrpInfo != null){
                    grpId = userGrpInfo.getId();
                    //根据用户组id获取资源权限
                    resourceInfoList = resourceInfoService.findResAuthByGrpId(grpId);
                }
            }*/

            UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(Integer.parseInt(userId));
            if(userGrpInfo != null){
                grpId = userGrpInfo.getId();
                //根据用户组id获取资源权限
                resourceInfoList = resourceInfoService.findResAuthByGrpId(grpId);
            }
            List<ResourceInfo> resourceInfoListFromUserId = resourceInfoService.findResAuthByUserId(Integer.parseInt(userId));
            System.out.println("----------resourceInfoListFromUserId------------"+resourceInfoListFromUserId.size());
            if(resourceInfoListFromUserId.size() > 0){
                for(ResourceInfo resInfo : resourceInfoListFromUserId){
                    if(resInfo.getRightMain() != null && resInfo.getRightMain().equals("user")){
                        for(ResourceInfo ri : resourceInfoList){
                            if(ri.getId().equals(resInfo.getId())){
                                ri.setRightFieldValue(resInfo.getRightFieldValue());
                                ri.setRightMain(resInfo.getRightMain());
                            }
                        }
                    }
                }
            }

            List<SysMenuInfoTreeObject> treeObjects = new ArrayList<SysMenuInfoTreeObject>();

            for(ResourceInfo resinfo : resourceInfoList){
                SysMenuInfoTreeObject t = new SysMenuInfoTreeObject();
                try {
                    PropertyUtils.copyProperties(t,resinfo);
                    treeObjects.add(t);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TreeUtil<SysMenuInfoTreeObject> tu = new TreeUtil<SysMenuInfoTreeObject>();
            List<SysMenuInfoTreeObject> ns = tu.getChildResources(treeObjects,-1);
            str = JSON.toJSONString(ns);
            System.out.println("-------all tree json -------"+ str);

        }

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree",str);
        return returnMap;
    }


    /**
     * 获得API树结构(个人账号权限和用户组权限并集)
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getUnioAPIAuthTree",method = RequestMethod.POST)
    public Map<String,Object> getUnioAPIAuthTree(HttpServletRequest request, HttpSession httpSession){

        String userId = request.getParameter("userId");
        Integer grpId = null;
        String str = "";
        if(userId != null && userId != ""){

            AuthInfo authInfo = new AuthInfo();
            authInfo.setAuthMain("user");
            authInfo.setAuthMainValue(Integer.parseInt(userId));
            authInfo.setAuthDomain("api");
            boolean flag = authInfoService.checkAuth(authInfo);
            List<ApiInfo> apiInfoList = new ArrayList<ApiInfo>();
            /*if(flag == true){
                //根据用户id获取资源权限
                apiInfoList = apiInfoService.findApiInfoAuthByUserId(Integer.parseInt(userId));
            }else{
                //根据userid 查找 用户组id
                UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(Integer.parseInt(userId));
                if(userGrpInfo != null){
                    grpId = userGrpInfo.getId();
                    System.out.println("=============>"+grpId);
                    //根据用户组id获取资源权限
                    apiInfoList = apiInfoService.findApiInfoAuthByGrpId(grpId);
                }
            }*/

            UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(Integer.parseInt(userId));
            if(userGrpInfo != null){
                grpId = userGrpInfo.getId();
                //根据用户组id获取资源权限
                apiInfoList = apiInfoService.findApiInfoAuthByGrpId(grpId);
            }
            List<ApiInfo> apiInfoListFromUserId = apiInfoService.findApiInfoAuthByUserId(Integer.parseInt(userId));

            if(apiInfoListFromUserId.size() > 0){
                for(ApiInfo apiInfo : apiInfoListFromUserId){
                    if(apiInfo.getRightMain() != null && apiInfo.getRightMain().equals("user")){
                        for(ApiInfo ai : apiInfoList){
                            if(ai.getId().equals(apiInfo.getId())){
                                ai.setRightFieldValue(apiInfo.getRightFieldValue());
                                ai.setRightMain(apiInfo.getRightMain());
                            }
                        }
                    }
                }
            }

            List<ApiInfoTreeObject> treeObjects = new ArrayList<ApiInfoTreeObject>();

            for(ApiInfo apiInfo : apiInfoList){
                ApiInfoTreeObject t = new ApiInfoTreeObject();
                try {
                    PropertyUtils.copyProperties(t,apiInfo);
                    treeObjects.add(t);
                }catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TreeUtil<ApiInfoTreeObject> tu = new TreeUtil<ApiInfoTreeObject>();
            List<ApiInfoTreeObject> ns = tu.getChildResources(treeObjects,0);
            str = JSON.toJSONString(ns);
            System.out.println("-------all api -------"+ str);

        }

        Map<String,Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree",str);
        return returnMap;
    }



}
