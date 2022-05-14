package com.teradata.portal.web.apiRelease.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.github.miemiedev.mybatis.paginator.domain.Order;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.teradata.openapi.framework.util.LoadProperties;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.portal.web.apiRelease.dao.ApiReleaseMapper;
import com.teradata.portal.web.apiRelease.pojo.ApiArgRst;
import com.teradata.portal.web.apiRelease.pojo.ApiInfo;
import com.teradata.portal.web.apiRelease.pojo.ResponseResult;
import com.teradata.portal.web.apiRelease.pojo.SorcFieldRst;
import com.teradata.portal.web.apiRelease.pojo.StructApiArg;
import com.teradata.portal.web.apiRelease.service.ApiReleaseService;
import com.xiaoleilu.hutool.http.HttpUtil;

/**
 * API发布业务层实现. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016-7-6 下午4:09:06
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Wei.Duan@Teradata.com
 * @version 1.0.0
 */
@Service
public class ApiReleaseServiceImpl implements ApiReleaseService {

	@Autowired
	private ApiReleaseMapper apiReleaseMapper;

	JSONObject json = new JSONObject();

	/*
	 * 克隆api（基本信息+详情）
	 */
	@Override
	public JSONObject cloneApi(ApiInfo apiItem) {
		// apiItem.setRelsePersn("cloner_dw");
		Integer latestApiVersion = apiReleaseMapper.queryLatestVersion(apiItem.getApiId());
		Integer newApiVersion = latestApiVersion + 1;
		apiItem.setNewApiVersion(newApiVersion);
		apiReleaseMapper.cloneApiItem(apiItem);
		apiReleaseMapper.cloneApiArg(apiItem);
		apiReleaseMapper.cloneApiTabInfo(apiItem);
		json.put("data", 1);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}

	@Override
	public JSONObject delApi(ApiInfo apiItem) {
		Integer apiId = apiItem.getApiId();
		Integer apiVersion = apiItem.getApiVersion();
		apiReleaseMapper.deleteApiItem(apiItem);
		apiReleaseMapper.deleteApiArg(apiId, apiVersion);
		json.put("data", 1);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}

	/*
	 * 获取api的配置信息
	 */
	@Override
	public List<ApiArgRst> getApiArg(Integer apiId, Integer apiVersion) {
		return apiReleaseMapper.queryApiArg(apiId, apiVersion);
	}

	@Override
	public List<Map<String, Object>> getApiCls() {
		return apiReleaseMapper.queryApiCls();
	}

	/*
	 * 获取api列表信息
	 */
	@Override
	public List<Map<String, Object>> getApiDataCycle() {
		return apiReleaseMapper.queryApiDataCycle();
	}

	/*
	 * 获取api状态信息
	 */
	@Override
	public List<Map<String, Object>> getApiExamStat() {
		return apiReleaseMapper.queryApiExamStat();
	}

	/*
	 * 获取api分类信息
	 */
	@Override
	public List<?> getApiInfo(Map<String, Object> param) {
		Integer page = Integer.parseInt(param.get("pageIndex").toString()) + 1;
		Integer limit = Integer.parseInt(param.get("pageSize").toString());
		String sortString = "relse_time.desc";
		PageBounds pageBounds = new PageBounds(page, limit, Order.formString(sortString), true);
		return apiReleaseMapper.queryApiInfo(param, pageBounds);
	}

	/*
	 * 获取api状态
	 */
	@Override
	public List<Map<String, Object>> getApiSort() {
		return apiReleaseMapper.queryApiSort();
	}

	/*
	 * 获取api数据周期类型信息
	 */
	@Override
	public List<Map<String, Object>> getApiStat() {
		return apiReleaseMapper.queryApiStat();
	}

	/*
	 * 获取api数据规模类型信息
	 */
	@Override
	public List<Map<String, Object>> getApiTabScale() {
		return apiReleaseMapper.queryApiTabScale();
	}

	/*
	 * 获取必选类型信息
	 */
	@Override
	public List<Map<String, Object>> getArgMustCode() {
		return apiReleaseMapper.queryArgMustCode();
	}

	/*
	 * 获取计算法则类型信息
	 */
	@Override
	public List<Map<String, Object>> getCalcPrincCode() {
		return apiReleaseMapper.queryCalcPrincCode();
	}

	/*
	 * 获取目标字段类型信息
	 */
	@Override
	public List<Map<String, Object>> getDataTargtTypeCode() {
		return apiReleaseMapper.queryDataTargtTypeCode();
	}

	/*
	 * 获取组信息
	 */
	@Override
	public List<Map<String, Object>> getMustOneGrpId() {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

		// 暂时前端静态写死
		return list;
	}

	@Override
	public List<Map<String, Object>> getSchema(HashMap<String, Object> param) {
		Integer sourceId = Integer.parseInt(param.get("sourceId").toString());
		return apiReleaseMapper.querySchema(sourceId);
	}

	@Override
	public List<Map<String, Object>> getSource() {
		return apiReleaseMapper.querySource();
	}

	/*
	 * 获取不同数据源的字段（取交集）
	 */
	@Override
	public List<SorcFieldRst> getSourceField(List<Map<String, String>> param) {
		return apiReleaseMapper.querySourceField(param);
	}

	@Override
	public List<Map<String, Object>> getTable(HashMap<String, Object> param) {
		Integer sourceId = Integer.parseInt(param.get("sourceId").toString());
		param.put("sourceId", sourceId);
		return apiReleaseMapper.queryTable(param);
	}

	/*
	 * 获取api触发方式信息
	 */
	@Override
	public List<Map<String, Object>> getTriggerMethd() {
		return apiReleaseMapper.queryTriggerMethd();
	}

	/*
	 * 修改api
	 */
	@Override
	public JSONObject modifyApiItem(ApiInfo apiItem) {
		int a = apiReleaseMapper.updateApiItem(apiItem);
		json.put("data", a);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}

	/*
	 * 保存api参数后，调用http请求，更新actor
	 */
	@Override
	public void modifyRemoteActor(Integer apiId, Integer apiVersion) {
		try {
			Map<String, Object> paramMap = new HashMap<String, Object>();
			paramMap.put("refFlag", 2);
			paramMap.put("apiId", apiId);
			paramMap.put("apiVersion", apiVersion);
			String SERVER_URL = LoadProperties.getProp("serviceUrl");
			String response = HttpUtil.post(String.format("%s/load", SERVER_URL), paramMap);
			ResponseResult responseResult = (ResponseResult) JSONObject.toBean(JSONObject.fromObject(response), ResponseResult.class);
			String retCode = responseResult.getRetCode();

			// 调用失败后，插入错误日志表
			if (!retCode.equals("0")) {
				Integer errAbnormId = Integer.parseInt(UUIDUtils.getRandomNumNoZero(8));
				String errAbnormSurc = "更新远端actor有误";
				String reqUrl = SERVER_URL + "/load?apiId=" + apiId + "&apiVersion=" + apiVersion + "&refFlag=2";
				apiReleaseMapper.insertErrAbnorm(errAbnormId, errAbnormSurc, reqUrl, response);
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * 发布api
	 */
	@Override
	public JSONObject releaseApiItem(ApiInfo apiItem) {
		int a = apiReleaseMapper.releaseApiItem(apiItem);
		json.put("data", a);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}

	/*
	 * 配置api详情
	 * @param param1:数据源信息 param2:字段配置信息
	 */
	@Override
	public JSONObject saveApiArg(List<Map<String, String>> param1, List<Map<String, String>> param2, Integer apiId, Integer apiVersion) {

		// 清除api配置相关信息（struct_api_arg、api_tab_info）
		apiReleaseMapper.deleteApiArg(apiId, apiVersion);

		for (Map<String, String> map : param2) {
			StructApiArg structApiArg = new StructApiArg();

			// schema_name
			// TAB_NAME
			String fieldName = map.get("fieldName");
			String fieldAlias = fieldName;
			Integer fieldEffStat = 1;

			// 来源字段的信息拼装成json_begin
			List<Map<String, Object>> list = apiReleaseMapper.queryFieldSorcType(param1, fieldName);
			JSONArray jsonArray = JSONArray.fromObject(list);
			String fieldSorcType = jsonArray.toString();
			// 来源字段的信息拼装成json_end
			String fieldTargtType = map.get("fieldTargtType");
			String fieldTitle = "no title";
			// 请求参数标识没选中时，必须类型为“可选”
			Integer syncMustType = Integer.parseInt(StringUtils.defaultIfBlank(map.get("syncMustType"), "2"));
			Integer syncMustOneGrpId = map.get("syncMustOneGrpId") != null ? (Integer.parseInt(map.get("syncMustOneGrpId"))) : null;
			Integer asynMustType = Integer.parseInt(StringUtils.defaultIfBlank(map.get("asynMustType"), "2"));
			Integer asynMustOneGrpId = map.get("asynMustOneGrpId") != null ? (Integer.parseInt(map.get("asynMustOneGrpId"))) : null;
			Integer rssMustType = Integer.parseInt(StringUtils.defaultIfBlank(map.get("rssMustType"), "2"));
			Integer rssMustOneGrpId = map.get("rssMustOneGrpId") != null ? (Integer.parseInt(map.get("rssMustOneGrpId"))) : null;
			String reqArgId = StringUtils.defaultIfBlank(map.get("reqArgId"), "0");
			String reqArgDefltVal = map.get("reqArgDefltVal");
			String respnArgId = StringUtils.defaultIfBlank(map.get("respnArgId"), "0");
			String respnArgSampVal = map.get("respnArgSampVal");
			String fieldFileDesc = map.get("fieldFileDesc");
			Integer calcPrincId = Integer.parseInt(map.get("calcPrincId"));
			String valueRange = StringUtils.defaultIfBlank(map.get("valueRange"), "");// 暂时没做

			structApiArg.setApiId(apiId);
			structApiArg.setApiVersion(apiVersion);
			structApiArg.setFieldName(fieldName);
			structApiArg.setFieldAlias(fieldAlias);
			structApiArg.setFieldEffStat(fieldEffStat);
			structApiArg.setFieldSorcType(fieldSorcType);
			structApiArg.setFieldTargtType(fieldTargtType);
			structApiArg.setFieldTitle(fieldTitle);
			structApiArg.setSyncMustType(syncMustType);
			structApiArg.setSyncMustOneGrpId(syncMustOneGrpId);
			structApiArg.setAsynMustType(asynMustType);
			structApiArg.setAsynMustOneGrpId(asynMustOneGrpId);
			structApiArg.setRssMustType(rssMustType);
			structApiArg.setRssMustOneGrpId(rssMustOneGrpId);
			structApiArg.setReqArgId(reqArgId);
			structApiArg.setReqArgDefltVal(reqArgDefltVal);
			structApiArg.setRespnArgId(respnArgId);
			structApiArg.setRespnArgSampVal(respnArgSampVal);
			structApiArg.setFieldFileDesc(fieldFileDesc);
			structApiArg.setCalcPrincId(calcPrincId);
			structApiArg.setValueRange(valueRange);

			apiReleaseMapper.insertApiArg(structApiArg);
			apiReleaseMapper.insertApiTabInfo(param1, apiId, apiVersion, fieldName);
		}

		json.put("data", 1);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}

	/*
	 * 新增api
	 */
	@Override
	public JSONObject saveApiItem(ApiInfo apiItem) {
		apiItem.setApiStatCode(1);// 生效
		apiItem.setDataStrctTypeCode(1);// 1结构化 2非结构化 3半结构化
		apiItem.setRelseType(0);// 0周期冗余1同库关联2跨库关
		// apiItem.setRelsePersn("dw");// 发布人
		apiItem.setExamStat(0);// 0未发布1待审批2已审批3审批驳回
		apiItem.setApiClassName("");// api 类名默认为空

		int a = apiReleaseMapper.insertApiItem(apiItem);
		json.put("data", a);
		json.put("success", true);
		json.put("msg", "操作成功");
		return json;
	}
}