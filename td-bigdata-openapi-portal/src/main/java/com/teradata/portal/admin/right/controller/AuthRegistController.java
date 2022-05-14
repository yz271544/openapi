package com.teradata.portal.admin.right.controller;

import com.alibaba.fastjson.JSON;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.google.common.collect.Maps;
import com.teradata.openapi.framework.util.DateUtil;
import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.framework.util.StringUtil;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.openapi.rop.session.Session;
import com.teradata.portal.admin.auth.base.BaseController;
import com.teradata.portal.admin.auth.entity.ApiInfo;
import com.teradata.portal.admin.auth.entity.AuthInfo;
import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.admin.auth.service.ApiInfoService;
import com.teradata.portal.admin.auth.service.AuthInfoService;
import com.teradata.portal.admin.auth.service.UserGrpInfoService;
import com.teradata.portal.admin.auth.util.TreeUtil;
import com.teradata.portal.admin.auth.vo.ApiInfoTreeObject;
import com.teradata.portal.admin.right.entity.RightApplyDetl;
import com.teradata.portal.admin.right.entity.RightApplyInfo;
import com.teradata.portal.admin.right.service.RightApplyDetlService;
import com.teradata.portal.admin.right.service.RightApplyInfoService;
import com.teradata.portal.web.registAppr.controller.RegistApprController;
import com.xiaoleilu.hutool.http.HttpUtil;
import net.sf.json.JSONObject;
import org.apache.commons.beanutils.PropertyUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
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
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.*;

/**
 * Created by Evan on 2016/9/2.
 */
@Controller
@RequestMapping(value = "/admin/right")
public class AuthRegistController extends BaseController {

    @Inject
    private AuthInfoService authInfoService;

    @Inject
    private UserGrpInfoService userGrpInfoService;

    @Inject
    private ApiInfoService apiInfoService;

    @Inject
    private RightApplyInfoService rightApplyInfoService;

    @Inject
    private RightApplyDetlService rightApplyDetlService;


    @RequestMapping(value = "/authRegistApply.htm")
    public ModelAndView AuthRegistApplyIndex(HttpServletRequest request, HttpSession httpSession) {
        return new ModelAndView("admin/right/authRegistApply");
    }

    /*
     * 生成唯一id
     */
    @RequestMapping(value = "/generateId.json")
    @ResponseBody
    public JSONObject generateId(HttpServletRequest request) throws Exception {
        Integer num = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("num"), "8"));
        JSONObject json = new JSONObject();
        json.put("id", UUIDUtils.getRandomNumNoZero(num));
        return json;
    }

    /*
     * 获取userId
     */
    @RequestMapping(value = "/getUserId.json")
    @ResponseBody
    public JSONObject getUserId(HttpServletRequest request, HttpSession httpSession) throws Exception {
        UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
        JSONObject json = new JSONObject();
        json.put("userId", user.getUserId());
        return json;
    }

    /**
     * 系统管理--权限申请main
     *
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/authRegist.htm")
    public ModelAndView AuthRegistIndex(HttpServletRequest request, HttpSession httpSession) {
        return new ModelAndView("admin/right/authRegist");
    }

    /**
     * 系统管理--权限审批main
     *
     * @param request
     * @param httpSession
     * @return
     */
    @RequestMapping(value = "/authAppr.htm")
    public ModelAndView AuthApprIndex(HttpServletRequest request, HttpSession httpSession) {
        return new ModelAndView("admin/right/authAppr");
    }


    /*
     * 删除apply
     */
    @RequestMapping(value = "/delApply.action")
    @ResponseBody
    public JSONObject delApply(HttpServletRequest request, RightApplyInfo rightApplyInfo) throws Exception {
        return rightApplyInfoService.deleteApply(rightApplyInfo);
    }

    /**
     * 获得API树结构(个人账号权限和用户组权限并集)
     *
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getUnioAPIAuthTree", method = RequestMethod.POST)
    public Map<String, Object> getUnioAPIAuthTree(HttpServletRequest request, HttpSession httpSession) {

        UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");

        Integer grpId = null;
        String str = "";
        if (user != null) {

            Integer userId = user.getUserId();

            AuthInfo authInfo = new AuthInfo();
            authInfo.setAuthMain("user");
            authInfo.setAuthMainValue(userId);
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

            UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(userId);
            if (userGrpInfo != null) {
                grpId = userGrpInfo.getId();
                //根据用户组id获取资源权限
                apiInfoList = apiInfoService.findApiInfoAuthByGrpId(grpId);
            }
            List<ApiInfo> apiInfoListFromUserId = apiInfoService.findApiInfoAuthByUserId(userId);

            if (apiInfoListFromUserId.size() > 0) {
                for (ApiInfo apiInfo : apiInfoListFromUserId) {
                    if (apiInfo.getRightMain() != null && apiInfo.getRightMain().equals("user")) {
                        for (ApiInfo ai : apiInfoList) {
                            if (ai.getId().equals(apiInfo.getId())) {
                                ai.setRightFieldValue(apiInfo.getRightFieldValue());
                                ai.setRightMain(apiInfo.getRightMain());
                            }
                        }
                    }
                }
            }

            List<ApiInfoTreeObject> treeObjects = new ArrayList<ApiInfoTreeObject>();

            for (ApiInfo apiInfo : apiInfoList) {
                ApiInfoTreeObject t = new ApiInfoTreeObject();
                try {
                    PropertyUtils.copyProperties(t, apiInfo);
                    treeObjects.add(t);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            TreeUtil<ApiInfoTreeObject> tu = new TreeUtil<ApiInfoTreeObject>();
            List<ApiInfoTreeObject> ns = tu.getChildResources(treeObjects, 0);
            str = JSON.toJSONString(ns);
            System.out.println("-------all api -------" + str);

        }

        Map<String, Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree", str);
        return returnMap;
    }

    @RequestMapping(value = "/registApiAuth", method = RequestMethod.POST)
    public ResponseEntity<String> registApiAuth(HttpSession httpSession, HttpServletRequest request) {

        HttpHeaders responseHeaders = new HttpHeaders();
        MediaType mediaType = new MediaType("text", "html", Charset.forName("UTF-8"));
        responseHeaders.setContentType(mediaType);


        ResponseEntity<String> re = null;
        String returns = "";
        //boolean flagStatus = false;
        UserInfo userInfo = (UserInfo) httpSession.getAttribute("userInfo");
        int userId = userInfo.getUserId();

        String applyIdStr = request.getParameter("applyId");
        String apiId = request.getParameter("allApiId");
        int[] in = {0, 1, 2, 3, 4, 5, 6, 7, 8, 9};
        System.out.println("---------> apiId:" + apiId);
        if (apiId != null && !apiId.equals("")) {

            RightApplyInfo rightApplyInfo = new RightApplyInfo();

            Integer applyId = StringUtils.isBlank(request.getParameter("applyId")) ? UUIDUtils.getNotSimple(in, 10) : Integer.valueOf(request.getParameter("applyId"));
            System.out.println("-------------> applyId:" + applyId);
            rightApplyInfo.setApplyId(applyId);
            rightApplyInfo.setUserId(userId);
            rightApplyInfo.setLoginAcct(userInfo.getLoginAcct());
            rightApplyInfo.setApplyTime(System.currentTimeMillis());
            rightApplyInfo.setAuditStat(-1);

            try {
                int f1 = rightApplyInfoService.registApiAuthInfo(rightApplyInfo);
                int f2 = rightApplyDetlService.deleteByApplyId(rightApplyInfo.getApplyId());
                System.out.println("----------> f1:" + f1 + " ------>f2:" + f2);
                if (f1 > 0 && f2 >= 0) {
                    String[] apiIds = apiId.split(",");
                    int addApiNum = 0;
                    for (int i = 0; i < apiIds.length; i++) {
                        RightApplyDetl rightApplyDetl = new RightApplyDetl();
                        rightApplyDetl.setApplyId(rightApplyInfo.getApplyId());
                        rightApplyDetl.setRightMain("user");
                        rightApplyDetl.setRightMainVal(userId);
                        rightApplyDetl.setRightField("api");
                        rightApplyDetl.setRightFieldVal(Integer.parseInt(apiIds[i]));
                        rightApplyDetlService.add(rightApplyDetl);
                        addApiNum += 1;
                    }

                    if (apiIds.length == addApiNum) {
                        returns = new String("权限申请成功!");
                    } else {
                        returns = new String("权限申请失败!");
                    }

                } else {
                    returns = new String("权限申请失败!");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else {
            returns = new String("权限申请失败!");
        }

        re = new ResponseEntity<String>(returns, responseHeaders, HttpStatus.OK);

        return re;

    }


    /*
     * 获取authApply信息列表
	 */
    @RequestMapping(value = "/getRightApplyMaster.json")
    @ResponseBody
    public Map<String, Object> getRightApplyMaster(HttpServletRequest request, HttpSession httpSession) throws Exception {
        Map<String, Object> result = new HashMap<String, Object>();
        UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
        String login_acct = user.getLoginAcct();
        result.put("loginAcct", login_acct);
        System.out.println("------------> " + result.get("loginAcct"));
        return result;
    }


    /*
     * 获取authApply信息列表
	 */
    @RequestMapping(value = "/getRightApplyInfoInfo.json")
    @ResponseBody
    public PageList<?> getAuthApplyInfo(HttpServletRequest request, HttpSession httpSession) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        UserInfo user = (UserInfo) httpSession.getAttribute("userInfo");
        String login_acct = user.getLoginAcct();
        System.out.println("-------->" + login_acct);
        Integer auditStat = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("auditStat"), "-2")); //默认获取未审批记录
        System.out.println("auditStat:" + auditStat);
        Date beginDate = StringUtils.isBlank(request.getParameter("beginDate")) ? null : DateUtils.parseDate(request.getParameter("beginDate"),
                "yyyy-MM-dd");
        Long beginDateTimeMills = (beginDate == null) ? null : (beginDate.getTime());
        Date endDate = StringUtils.isBlank(request.getParameter("endDate")) ? null : DateUtils.parseDate(request.getParameter("endDate"),
                "yyyy-MM-dd");
        Long endDateTimeMills = (endDate == null) ? null : (endDate.getTime());
        //String apiRange = request.getParameter("apiRange"); // 1:我 2:所有
        Integer pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        param.put("loginAcct", login_acct);
        param.put("auditStat", auditStat);
        param.put("beginDateTimeMills", beginDateTimeMills);
        param.put("endDateTimeMills", endDateTimeMills);
        param.put("pageIndex", pageIndex);
        param.put("pageSize", pageSize);
        return (PageList)rightApplyInfoService.getRightApplyInfo(param);
    }

    /*
     * 获取authAppr信息列表
	 */
    @RequestMapping(value = "/getRightApprInfoInfo.json")
    @ResponseBody
    public PageList<?> getAuthApprInfo(HttpServletRequest request, HttpSession httpSession) throws Exception {
        Map<String, Object> param = new HashMap<String, Object>();
        String login_acct = StringUtils.defaultIfBlank(request.getParameter("loginAcct"), "");
        System.out.println("-------->" + login_acct);
        Integer auditStat = Integer.parseInt(StringUtils.defaultIfBlank(request.getParameter("auditStat"), "-2")); //默认获取未审批记录
        System.out.println("auditStat:" + auditStat);
        Date beginDate = StringUtils.isBlank(request.getParameter("beginDate")) ? null : DateUtils.parseDate(request.getParameter("beginDate"),
                "yyyy-MM-dd");
        Long beginDateTimeMills = (beginDate == null) ? null : (beginDate.getTime());
        Date endDate = StringUtils.isBlank(request.getParameter("endDate")) ? null : DateUtils.parseDate(request.getParameter("endDate"),
                "yyyy-MM-dd");
        Long endDateTimeMills = (endDate == null) ? null : (endDate.getTime());
        //String apiRange = request.getParameter("apiRange"); // 1:我 2:所有
        Integer pageIndex = Integer.parseInt(request.getParameter("pageIndex"));
        Integer pageSize = Integer.parseInt(request.getParameter("pageSize"));

        param.put("loginAcct", login_acct);
        param.put("auditStat", auditStat);
        param.put("beginDateTimeMills", beginDateTimeMills);
        param.put("endDateTimeMills", endDateTimeMills);
        param.put("pageIndex", pageIndex);
        param.put("pageSize", pageSize);
        return (PageList)rightApplyInfoService.getRightApplyInfo(param);
    }

    /*
     * 获取下拉框
	 */
    @RequestMapping(value = "/getSelect.json")
    @ResponseBody
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> getSelect(HttpServletRequest request) {
        HashMap<String, Object> param = (HashMap<String, Object>) RegistApprController.getParameterMap(request);
        List<Map<String, Object>> rstList = new ArrayList<Map<String, Object>>();
        try {
            Class<?> ownerClass = rightApplyInfoService.getClass();
            Object[] args = new Object[1];
            args[0] = param;
            int paramSize = param.keySet().size();
            Class<?>[] argsClass = new Class[args.length];
            argsClass[0] = param.getClass();
            String methodName = request.getParameter("method");
            System.out.println(paramSize);

            Method method;
            if (paramSize == 1) {
                method = ownerClass.getMethod(methodName);
                rstList = (List<Map<String, Object>>) method.invoke(rightApplyInfoService);
            } else {
                method = ownerClass.getMethod(methodName, argsClass);
                rstList = (List<Map<String, Object>>) method.invoke(rightApplyInfoService, args);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rstList;
    }

    /*
     * 权限申请审批-审批
     */
    @RequestMapping(value = "/doAuthApprove.action")
    @ResponseBody
    public JSONObject doAuthApprove(HttpServletRequest request, RightApplyInfo rightApplyInfo, HttpSession httpSession) {
        System.out.println("---> applyId: " + rightApplyInfo.getApplyId());
        System.out.println("---> auditStat: " + rightApplyInfo.getAuditStat());
        System.out.println("---> auditAdvc: " + rightApplyInfo.getAuditAdvc());
        UserInfo audit_persn = (UserInfo) httpSession.getAttribute("userInfo");
        rightApplyInfo.setAuditPersn(audit_persn.getLoginAcct());
        String SERVER_URL = LoadProperties.getProp("serviceUrl");
        Map<String, Object> paramMap = new HashMap<String, Object>();
        paramMap.put("refFlag", 2);
        JSONObject ret = rightApplyInfoService.doAuthApprove(rightApplyInfo);
        try {
            HttpUtil.post(String.format("%s/refresh", SERVER_URL),paramMap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ret;
    }

    /*
     * 权限申请发布
     */
    @RequestMapping(value = "/doAuthRelease.action")
    @ResponseBody
    public JSONObject doAuthRelease(HttpServletRequest request, RightApplyInfo rightApplyInfo, HttpSession httpSession) {
        System.out.println("---> applyId: " + rightApplyInfo.getApplyId());
        System.out.println("---> auditStat: " + rightApplyInfo.getAuditStat());
        return rightApplyInfoService.doAuthRelease(rightApplyInfo);
    }

    /**
     * 获得API树结构(个人账号权限和用户组权限并集)
     *
     * @param request
     * @param httpSession
     * @return
     */

    @ResponseBody
    @RequestMapping(value = "/getUnioAPIAuthApplyTree", method = RequestMethod.POST)
    public Map<String, Object> getUnioAPIAuthApplyTree(HttpServletRequest request, RightApplyInfo rightApplyInfo, HttpSession httpSession) {

        Integer applyId = rightApplyInfo.getApplyId() == null ? null : rightApplyInfo.getApplyId();
        Integer userId = rightApplyInfo.getUserId() == null ? null : rightApplyInfo.getUserId();
        Integer grpId = null;
        String str = "";

        UserGrpInfo userGrpInfo = userGrpInfoService.findbyUserGrpRelat(userId);
        if (userGrpInfo != null) {
            grpId = userGrpInfo.getId();
        }

        List<ApiInfo> apiInfoList = new ArrayList<ApiInfo>();

        apiInfoList = rightApplyDetlService.findDetlbyApplyid(applyId, userId, grpId);

        List<ApiInfoTreeObject> treeObjects = new ArrayList<ApiInfoTreeObject>();

        for (ApiInfo apiInfo : apiInfoList) {
            ApiInfoTreeObject t = new ApiInfoTreeObject();
            try {
                PropertyUtils.copyProperties(t, apiInfo);
                treeObjects.add(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        TreeUtil<ApiInfoTreeObject> tu = new TreeUtil<ApiInfoTreeObject>();
        List<ApiInfoTreeObject> ns = tu.getChildResources(treeObjects, 0);
        str = JSON.toJSONString(ns);
        System.out.println("-------all api -------" + str);


        Map<String, Object> returnMap = Maps.newHashMap();
        returnMap.put("ugtree", str);
        return returnMap;
    }
}
