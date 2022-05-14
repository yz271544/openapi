package com.teradata.portal.web.sandbox.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.teradata.openapi.rop.CommonConstant;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.client.ClientRequest;
import com.teradata.openapi.rop.config.SystemParameterNames;
import com.teradata.portal.admin.auth.entity.UserInfo;
import com.teradata.portal.web.sandbox.common.client.OpenApiClient;
import com.teradata.portal.web.sandbox.dao.SandBoxMapper;
import com.teradata.portal.web.sandbox.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.ApiInfoModel;
import com.teradata.portal.web.sandbox.pojo.ApiSortCode;
import com.teradata.portal.web.sandbox.pojo.ApiTestBox;
import com.teradata.portal.web.sandbox.pojo.DynamicApiArg;
import com.teradata.portal.web.sandbox.pojo.StructApiArg;
import com.teradata.portal.web.sandbox.service.SandBoxService;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;

@Service
public class SandBoxServiceImpl implements SandBoxService {

    @Autowired
    SandBoxMapper sandBoxMapper;

    /**
     * 查询所有API分类
     *
     * @return
     * @author houbl
     */
    @Override
    public List<Map<String, Object>> getApiSortMap() {
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<ApiSortCode> apiSortList = sandBoxMapper.queryAllApiSort();
        for (ApiSortCode sortCode : apiSortList) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("Value", sortCode.getApiSort());
            paramMap.put("Text", sortCode.getApiSortName());
            retList.add(paramMap);
        }
        return retList;
    }

    /**
     * 根据apiSort查询其分类下所有有效的api
     *
     * @param apiSort
     * @return
     * @author houbl
     */
    @Override
    public List<Map<String, Object>> getApiInfoMapBySort(Integer apiSort, String reqMethod) {
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<ApiInfo> apiInfoList = sandBoxMapper.queryApiBySort(apiSort, reqMethod);
        for (ApiInfo api : apiInfoList) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("Value", api.getApiId());
            paramMap.put("Text", api.getApiName());
            retList.add(paramMap);
        }
        return retList;
    }

    /**
     * 根据apiSort、reqMethod、expName、start和end 查询其分类下所有有效的api
     *
     * @param apiSort
     * @param reqMethod
     * @param expName
     * @param start
     * @param end
     * @return
     * @author huzy
     */
    @Override
    public List<Map<String, Object>> getApiInfoMapBySortPagination(Integer apiSort, String reqMethod, String expName, Integer start, Integer end) {
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<ApiInfo> apiInfoList = sandBoxMapper.queryApiBySortPagination(apiSort, reqMethod, expName, start, end);
        for (ApiInfo api : apiInfoList) {
            Map<String, Object> paramMap = new HashMap<String, Object>();
            paramMap.put("Value", api.getApiId());
            paramMap.put("Text", api.getApiName());
            retList.add(paramMap);
        }
        return retList;
    }

    public Long loadExpApiNameCount(Integer apiSort, String reqMethod, String expName){
        Long matchCount = 0L;
        matchCount = sandBoxMapper.loadExpApiNameCount(apiSort,reqMethod,expName);
        return matchCount;
    }
    /**
     * 根据apiId查询其下所有的版本信息
     *
     * @param apiId
     * @return
     * @author houbl
     */
    @Override
    public List<Map<String, Object>> getApiVersionMapById(Integer apiSort, Integer apiId) {
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<ApiInfoModel> modelList = this.getApiInfoModelListById(apiId);
        for (ApiInfoModel apiInfo : modelList) {
            if (apiSort.equals(apiInfo.getApiSort())) {
                Map<String, Object> paramMap = new HashMap<String, Object>();
                paramMap.put("Value", apiInfo.getApiVersion());
                paramMap.put("Text", apiInfo.getApiVersion());
                retList.add(paramMap);
            }
        }
        return retList;
    }

    /**
     * 根据apiId和apiVersion 查询其data_cycle_type 和trigger_method
     *
     * @param apiId
     * @param apiVersion
     * @return
     * @author houbl
     */
    @Override
    public List<Map<String, Object>> getApiOtherMapById(Integer apiId, Integer apiVersion) {
        List<Map<String, Object>> retList = new ArrayList<Map<String, Object>>();
        List<ApiInfoModel> modelList = this.getApiInfoModelListById(apiId);
        for (ApiInfoModel apiInfo : modelList) {
            if (apiVersion.equals(apiInfo.getApiVersion())) {
                Map<String, Object> dataCycMap = new HashMap<String, Object>();
                dataCycMap.put("Text", apiInfo.getDataCycleDesc());
                dataCycMap.put("Value", apiInfo.getDataCycleType());
                retList.add(dataCycMap);
                Map<String, Object> triggerMap = new HashMap<String, Object>();
                triggerMap.put("Text", apiInfo.getTriggerMethdDesc());
                triggerMap.put("Value", apiInfo.getTriggerMethd());
                retList.add(triggerMap);
            }
        }
        return retList;
    }

    /**
     * 根据apiId和apiVersion获取api_visit_methd信息
     *
     * @param apiId
     * @param apiVersion
     * @return
     * @author houbl
     */
    @Override
    public String getApiReqMethodById(Integer apiId, Integer apiVersion) {
        String reqMethod = null;
        ApiInfo apiInfo = sandBoxMapper.queryApiInfoByKey(apiId, apiVersion);
        if (apiInfo != null) {
            reqMethod = apiInfo.getApiVisitMethd();
        }
        return reqMethod;
    }

    /**
     * 根据apiId查询其下所有api视图信息
     *
     * @param apiId
     * @return
     * @author houbl
     */
    private List<ApiInfoModel> getApiInfoModelListById(Integer apiId) {
        return sandBoxMapper.queryApiInfoVWById(apiId);
    }

    /**
     * 根据apiId和apiVersion、reqMethod 动态获取其结构化参数
     *
     * @param apiId
     * @param apiVersion
     * @param reqMethod
     * @return
     * @author houbl
     */
    @Override
    public DynamicApiArg getDynamicApiArgById(Integer apiId, Integer apiVersion, Integer reqMethod) {
        DynamicApiArg dynArg = new DynamicApiArg();
        // 赋值
        dynArg.setApiId(apiId);
        dynArg.setApiVersion(apiVersion);
        List<StructApiArg> apiArgList = sandBoxMapper.queryStructArgByKey(apiId, apiVersion);
        String fields = "";

        List<StructApiArg> responseArgList = new ArrayList<StructApiArg>();// 响应参数列表
        List<StructApiArg> requiredArgList = new ArrayList<StructApiArg>();// 必选
        MultiValueMap<String, StructApiArg> chooseOneArgMap = new LinkedMultiValueMap<String, StructApiArg>();// 必选其一 key值是group分类
        List<String> chooseOneGroupList = new ArrayList<String>();// 必选其一 group值列表
        List<StructApiArg> choosableArgList = new ArrayList<StructApiArg>();// 可选

        for (StructApiArg apiArg : apiArgList) {
            // 先将其置空
            apiArg.setFieldSorcType(null);
            // 组装fields
            if (Constants.YES_FLAG.equals(apiArg.getRespnArgId())) {
                fields += apiArg.getFieldName() + ",";
                responseArgList.add(apiArg);
            }
            if (Constants.YES_FLAG.equals(apiArg.getReqArgId())) {
                Integer mustType = 0;
                Integer mustOneGrpId = 0;

                if (Constants.REQ_TYPE_0.equals(reqMethod)) {
                    mustType = apiArg.getSyncMustType();
                    mustOneGrpId = apiArg.getSyncMustOneGrpId();
                } else if (Constants.REQ_TYPE_1.equals(reqMethod)) {
                    mustType = apiArg.getAsynMustType();
                    mustOneGrpId = apiArg.getAsynMustOneGrpId();
                } else {
                    mustType = apiArg.getRssMustType();
                    mustOneGrpId = apiArg.getRssMustOneGrpId();
                }

                // 组装requiredArgList
                if (Constants.PROP_NECESSARY_0.equals(mustType) || Constants.PROP_NECESSARY_3.equals(mustType)) {
                    requiredArgList.add(apiArg);
                }

                // 组装chooseOneArgMap
                if (Constants.PROP_NECESSARY_1.equals(mustType)) {
                    if (mustOneGrpId != null) {
                        chooseOneArgMap.add("group_" + mustOneGrpId, apiArg);
                    }
                }

                // 组装choosableArgList
                if (Constants.PROP_NECESSARY_2.equals(mustType)) {
                    choosableArgList.add(apiArg);
                }
            }
        }
        int location = fields.lastIndexOf(",");
        if (location > -1) {
            fields = fields.substring(0, location);
        }
        dynArg.setFields(fields);
        dynArg.setResponseArgList(responseArgList);
        dynArg.setRequiredArgList(requiredArgList);
        dynArg.setChooseOneArgMap(chooseOneArgMap);
        for (String grouopKey : chooseOneArgMap.keySet()) {
            chooseOneGroupList.add(grouopKey);
        }
        dynArg.setChooseOneGroupList(chooseOneGroupList);
        dynArg.setChoosableArgList(choosableArgList);
        return dynArg;
    }

    /**
     * 调用远程返回数据
     *
     * @param srcParamMap
     * @param formArg
     * @param invokeMethod
     * @param appSecret
     * @return
     * @author houbl
     */
    @Override
    public Map<String, String> callRemoteApiData(Map<String, String[]> srcParamMap, ApiFormArg formArg, String invokeMethod, String appSecret,
                                                 String sourceFlag) {

        Map<String, String> retMap = new HashMap<String, String>();
        try {
            // 记录日志
            ApiTestBox boxLog = new ApiTestBox();
            Map<String, String> allParamMap = getRequestParams(srcParamMap);
            boxLog.setApiId(Integer.parseInt(allParamMap.get("apiId")));
            boxLog.setApiName(formArg.getMethod());
            boxLog.setApiVersion(Integer.parseInt(formArg.getVersion()));
            boxLog.setReqType(formArg.getReqType());
            boxLog.setAppKey(formArg.getAppKey());
            boxLog.setReturnType(formArg.getFormat());
            boxLog.setSubmitType(invokeMethod);

            String retValue = "";
            if (appSecret.contains("default")) {
                appSecret = Constants.SYS_DEFAULT_APP_SECRET;
            }
            OpenApiClient client = new OpenApiClient(formArg.getAppKey(), appSecret);
            ClientRequest request = client.buildClientRequest();
            Map<String, String> busiArgMap = getBusiReqParams(allParamMap);

            String reqUrl = request.buildUrl(formArg, busiArgMap, formArg.getMethod(), formArg.getVersion(), formArg.getReqType());
            retMap.put("reqUrl", reqUrl);
            if (Constants.REQ_WAY_GET.equals(invokeMethod)) {
                retValue = request.get(formArg, busiArgMap, formArg.getMethod(), formArg.getVersion(), formArg.getReqType());
            } else {
                retValue = request.post(formArg, busiArgMap, formArg.getMethod(), formArg.getVersion(), formArg.getReqType());
            }
            retMap.put("result", retValue);

            // 记录日志
            boxLog.setReqArgs(reqUrl);
            // 0 成功 1失败
            if (retValue.contains(CommonConstant.ERROR_TOKEN)) {
                boxLog.setIsSuccess("1");
            } else {
                boxLog.setIsSuccess("0");
            }
            boxLog.setTestResult(retValue);
            if ("2".equals(sourceFlag)) {
                // 从session中获取
                HttpServletRequest httpRequest = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
                UserInfo user = (UserInfo) httpRequest.getSession().getAttribute("userInfo");
                boxLog.setLoginName(user.getLoginAcct());
                sandBoxMapper.addTestBox(boxLog);
            }
        } catch (Exception e) {
            e.getMessage();
        }
        return retMap;
    }

    private Map<String, String> getRequestParams(Map<String, String[]> srcParamMap) {
        Map<String, String> destParamMap = new HashMap<String, String>(srcParamMap.size());
        for (Object obj : srcParamMap.keySet()) {
            String[] values = srcParamMap.get(obj);
            if (values != null && values.length > 0) {
                if (StringUtils.isNotBlank(values[0])) {
                    destParamMap.put((String) obj, values[0]);
                }
            }
        }
        return destParamMap;
    }

    /**
     * 获取业务级请求参数
     *
     * @param request
     * @return
     * @date 2016年6月16日
     * @author houbl
     */
    private Map<String, String> getBusiReqParams(Map<String, String> allParams) {
        String[] removeParams = SystemParameterNames.getSystemParamsName().split(Constants.SPLIT_SIGN);
        String[] specialParams = {"apiId", "apiSort", "appSecret", "invokeMethod", "sourceFlag"};
        for (String removeParam : removeParams) {
            allParams.remove(removeParam);
        }
        for (String removeParam : specialParams) {
            allParams.remove(removeParam);
        }
        return allParams;
    }

}
