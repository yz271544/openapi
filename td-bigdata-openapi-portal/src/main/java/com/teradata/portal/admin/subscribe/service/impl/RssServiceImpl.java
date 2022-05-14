package com.teradata.portal.admin.subscribe.service.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.beust.jcommander.internal.Maps;
import com.github.miemiedev.mybatis.paginator.domain.PageBounds;
import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.teradata.openapi.framework.message.request.Format;
import com.teradata.openapi.framework.message.request.RepArg;
import com.teradata.openapi.framework.message.request.ReqArg;
import com.teradata.openapi.framework.message.request.ReqToFind;
import com.teradata.openapi.framework.message.request.SorcType;
import com.teradata.openapi.framework.util.FastJSONUtil;
import com.teradata.openapi.framework.util.UUIDUtils;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.client.ClientRequest;
import com.teradata.openapi.rop.config.SystemParameterNames;
import com.teradata.portal.admin.subscribe.dao.ApiRssInfoMapper;
import com.teradata.portal.admin.subscribe.pojo.ApiRssInfo;
import com.teradata.portal.admin.subscribe.pojo.ApiRssInfoExample;
import com.teradata.portal.admin.subscribe.pojo.FtpUseInfo;
import com.teradata.portal.admin.subscribe.service.RssService;
import com.teradata.portal.admin.subscribe.vojo.FtpResponse;
import com.teradata.portal.web.sandbox.common.client.OpenApiClient;
import com.teradata.portal.web.sandbox.pojo.ApiInfo;
import com.teradata.portal.web.sandbox.pojo.StructApiArg;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;
import com.xiaoleilu.hutool.util.SecureUtil;

@Service
public class RssServiceImpl implements RssService {

	@Autowired
	ApiRssInfoMapper rssInfoMapper;

	/**
	 * 分页查询订阅信息
	 * 
	 * @param rssInfo
	 * @return
	 * @author houbl
	 */
	@Override
	public PageList<ApiRssInfo> queryRssInfosList(ApiRssInfo rssInfo) {
		PageBounds pageBounds = new PageBounds();
		pageBounds.setLimit(rssInfo.getRows());
		pageBounds.setPage(rssInfo.getPage());

		ApiRssInfoExample example = new ApiRssInfoExample();
		// 添加查询条件
		ApiRssInfoExample.Criteria criteria = example.createCriteria();
		if (StringUtils.isNotBlank(rssInfo.getApiName())) {
			criteria.andApiNameLike("%" + rssInfo.getApiName() + "%");
		}

		if (rssInfo.getEffFlag() != -1) {
			criteria.andEffFlagEqualTo(rssInfo.getEffFlag());
		}

		if (rssInfo.getDataCycleType() != -1) {
			criteria.andDataCycleTypeEqualTo(rssInfo.getDataCycleType());
		}

		if (rssInfo.getApiSort() != -1) {
			criteria.andApiSortEqualTo(rssInfo.getApiSort());
		}

		// 根据登录用户查询其下所有订阅信息 如果登录用户为管理员则查询其组下所有订阅信息
		if (StringUtils.isNotBlank(rssInfo.getAppkey()) && !"admin".equals(rssInfo.getAppkey())) {
			criteria.andAppkeyEqualTo(rssInfo.getAppkey());
		}

		return rssInfoMapper.selectByExample(example, pageBounds);
	}

	/**
	 * 根据订阅ID查询其订阅信息
	 * 
	 * @param rssId
	 * @return
	 * @author houbl
	 */
	@Override
	public ApiRssInfo queryRssInfoById(Integer rssId) {
		return rssInfoMapper.selectByPrimaryKey(rssId);
	}

	/**
	 * 保存与修改订阅信息
	 * 
	 * @param rssInfo
	 * @param method
	 * @author houbl
	 */
	@Override
	public void saveRssInfo(ApiRssInfo rssInfo, String method) throws Exception {
		if (!"operate".equals(method)) {
			// 组装数据
			ReqToFind tofind = reqToFindBean(rssInfo);
			// 带格式的数据指纹(md5加密)
			String retFormatFinger = finger(tofind, true);
			// 不带格式的数据指纹(md5加密)
			String retDataFinger = finger(tofind, false);

			rssInfo.setRetnFormFinger(retFormatFinger);
			rssInfo.setRetnDataFinger(retDataFinger);
			String pushArg = FastJSONUtil.serialize(rssInfo.getFtpUseInfo());
			rssInfo.setPushArg(pushArg);
			rssInfo.setPushArgRender(pushArg);
			String reqArg = FastJSONUtil.serialize(tofind.getReqArgs());
			rssInfo.setReqArg(reqArg);
			String respnArg = FastJSONUtil.serialize(tofind.getRepArgs());
			rssInfo.setRespnArg(respnArg);
			String format = FastJSONUtil.serialize(tofind.getFormat());
			rssInfo.setFormCode(format);
		}
		if ("add".equals(method)) {
			rssInfo.setEffFlag(1);
			rssInfo.setRssId(Integer.valueOf(UUIDUtils.getRandomNumNoZero(9)));
			rssInfoMapper.insert(rssInfo);

		} else if ("edit".equals(method) || "operate".equals(method)) {
			rssInfoMapper.updateByPrimaryKeySelective(rssInfo);
		}
	}

	/**
	 * 请求数据转化finder 实体对象
	 * 
	 * @param rssInfo
	 * @return
	 * @author houbl
	 */
	private ReqToFind reqToFindBean(ApiRssInfo rssInfo) {
		ReqToFind reqToFind = new ReqToFind();
		reqToFind.setAppKey(rssInfo.getAppkey());

		// 组装数据
		Map<String, Object> paramMap = new HashMap<String, Object>();
		paramMap.put("apiId", rssInfo.getApiId());
		paramMap.put("apiVersion", rssInfo.getApiVersion());

		ApiInfo apiInfo = rssInfoMapper.queryApiInfoWithOption(paramMap);
		reqToFind.setApiId(apiInfo.getApiId());
		reqToFind.setApiSort(apiInfo.getApiSort());
		reqToFind.setApiClsCode(apiInfo.getApiClsCode());
		reqToFind.setApi_version(rssInfo.getApiVersion());
		reqToFind.setApiName(apiInfo.getApiName());
		reqToFind.setTimeStamp(System.currentTimeMillis());

		// 封装format
		Format format = new Format();
		format.setFormType(rssInfo.getFormCode());
		reqToFind.setFormat(format);
		reqToFind.setEncode(rssInfo.getEncode());

		List<RepArg> repArgs = new ArrayList<RepArg>();
		String[] fieldsArry = rssInfo.getRespnArgRender().split(Constants.SPLIT_SIGN);

		List<ReqArg> reqArgs = new ArrayList<ReqArg>();
		String[] reqParamArry = rssInfo.getReqArgRender().split(";");
		Map<String, String> reqParamMap = new HashMap<String, String>();
		for (int i = 0; i < reqParamArry.length; i++) {
			String[] reqParam = reqParamArry[i].split("=");
			reqParamMap.put(reqParam[0], reqParam[1]);
		}

		List<StructApiArg> apiArgList = apiInfo.getApiArgList();
		Map<String, StructApiArg> apiArgMap = new HashMap<String, StructApiArg>();
		for (StructApiArg apiArg : apiArgList) {
			apiArgMap.put(apiArg.getFieldName(), apiArg);
		}

		for (String fields : fieldsArry) {
			RepArg repArg = new RepArg();
			StructApiArg apiArg = apiArgMap.get(fields);
			BeanUtils.copyProperties(apiArg, repArg);
			repArgs.add(repArg);
		}
		reqToFind.setRepArgs(repArgs);

		for (String paramKey : reqParamMap.keySet()) {
			ReqArg reqArg = new ReqArg();
			StructApiArg apiArg = apiArgMap.get(paramKey);
			BeanUtils.copyProperties(apiArg, reqArg);
			reqArg.setMustType(apiArg.getRssMustType());
			String paramValue = reqParamMap.get(paramKey);
			List<String> fieldValue = Arrays.asList(paramValue.split(Constants.SPLIT_SIGN));
			reqArg.setFieldValue(fieldValue);
			String sorcType = apiArg.getFieldSorcType();
			List<SorcType> sorcTypeList = FastJSONUtil.deserializeList(sorcType, SorcType.class);
			reqArg.setField_sorc_type(sorcTypeList);
			reqArgs.add(reqArg);
		}
		reqToFind.setReqArgs(reqArgs);
		return reqToFind;
	}

	/**
	 * 数据指纹生成（带格式与非格式）
	 * 
	 * @param toFind
	 * @param isFormat
	 * @return
	 * @author houbl
	 */
	private String finger(ReqToFind toFind, boolean isFormat) {
		Map<String, Object> paramValues = new HashMap<String, Object>();
		// 设置参数
		paramValues.put("apiId", toFind.getApiId());
		paramValues.put(SystemParameterNames.getVersion(), toFind.getApi_version());
		List<ReqArg> reqArgs = toFind.getReqArgs();
		for (ReqArg reqArg : reqArgs) {
			paramValues.put(reqArg.getFieldName(),
			                reqArg.getCalcPrincId() + Constants.JOIN_SIGN + StringUtils.join(reqArg.getFieldValue(), Constants.SPLIT_SIGN));
		}

		List<RepArg> repArgs = toFind.getRepArgs();
		List<String> repParam = new ArrayList<String>();
		for (RepArg repArg : repArgs) {
			repParam.add(repArg.getFieldName());
		}
		// 对业务级响应参数进行排序
		Collections.sort(repParam);
		paramValues.put(SystemParameterNames.getFields(), StringUtils.join(repParam, Constants.SPLIT_SIGN));

		if (isFormat) {
			paramValues.put(SystemParameterNames.getFormat(), toFind.getFormat());
			paramValues.put(SystemParameterNames.getCodeType(), toFind.getEncode());
		}

		StringBuilder sb = new StringBuilder();
		List<String> paramNames = new ArrayList<String>(paramValues.size());
		paramNames.addAll(paramValues.keySet());
		Collections.sort(paramNames);

		for (String paramName : paramNames) {
			sb.append(paramName).append(paramValues.get(paramName));
		}

		return SecureUtil.md5(sb.toString(), Constants.UTF8);

	}

	/**
	 * 调用ftp接口
	 * 
	 * @param ftpInfo
	 * @param formArg
	 * @param invokeMethod
	 * @param appSecret
	 * @return
	 * @date 2018年2月7日 上午11:30:56
	 * @author houbl
	 */
	@Override
	public Boolean callFtpApiData(FtpUseInfo ftpInfo, ApiFormArg formArg, String invokeMethod, String appSecret) {
		Boolean resultFlag = false;
		try {
			Map<String, String> busiArgMap = Maps.newHashMap();
			String retValue = "";
			OpenApiClient client = new OpenApiClient(formArg.getAppKey(), appSecret);
			ClientRequest request = client.buildClientRequest();

			busiArgMap.put("PushPluginType", ftpInfo.getFtpType());
			busiArgMap.put("UserName", ftpInfo.getUserName());
			String ftpPort = "";
			if (ftpInfo.getFtpPort() == null) {
				if ("ftp".equals(ftpInfo.getFtpType())) {
					ftpPort = "21";
				} else {
					ftpPort = "22";
				}
			} else {
				ftpPort = ftpInfo.getFtpPort().toString();
			}
			busiArgMap.put("Port", ftpPort);
			busiArgMap.put("Host", ftpInfo.getFtpHost());
			busiArgMap.put("FtpMode", StringUtils.defaultIfEmpty(ftpInfo.getFtpMode(), "active"));
			busiArgMap.put("Directory", ftpInfo.getFtpPath());
			busiArgMap.put("Password", ftpInfo.getPassword());

			if (Constants.REQ_WAY_GET.equals(invokeMethod)) {
				retValue = request.get(formArg, busiArgMap, formArg.getMethod(), formArg.getVersion(), formArg.getReqType());
			} else {
				retValue = request.post(formArg, busiArgMap, formArg.getMethod(), formArg.getVersion(), formArg.getReqType());
			}
			FtpResponse ftpRes = FastJSONUtil.deserialize(retValue, FtpResponse.class);
			if ("0".equals(ftpRes.getRetCode())) {
				resultFlag = true;
			}
		}
		catch (Exception e) {
			e.getMessage();
		}
		return resultFlag;
	}
}
