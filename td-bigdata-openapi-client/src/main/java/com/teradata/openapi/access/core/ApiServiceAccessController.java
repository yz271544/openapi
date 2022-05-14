package com.teradata.openapi.access.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teradata.openapi.access.biz.bean.ApiInfo;
import com.teradata.openapi.access.biz.service.AccessBizOperate;
import com.teradata.openapi.framework.util.SpringContextUtil;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.cache.CacheManager;
import com.teradata.openapi.rop.security.ServiceAccessController;
import com.teradata.openapi.rop.session.Session;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

public class ApiServiceAccessController implements ServiceAccessController, CacheManager {

	private final static Log log = StaticLog.get();

	private static Map<String, List<String>> aclMap = new HashMap<String, List<String>>();

	private static Map<Object, Object> apiMap = new HashMap<Object, Object>();

	static {
		// 初始化用户权限包含哪些api信息
		initGranted();
		// 初始化所有api信息
		initApiInfo();
	}

	@Override
	public boolean isAppGranted(String appKey, String method, String version) {
		if (aclMap.containsKey(appKey)) {
			List<String> serviceMethods = aclMap.get(appKey);
			if (serviceMethods != null) {
				return serviceMethods.contains(method);
			} else {
				return false;
			}
		} else {
			return true;
		}
	}

	@Override
	public boolean isUserGranted(Session session, String method, String version) {
		return true;
	}

	@Override
	public void cleanCache() {
		aclMap.clear();
		apiMap.clear();
	}

	@Override
	public void refreshCache() {
		log.info("清除ApiServiceAccessController 缓存 start");
		this.cleanCache();
		initGranted();
		initApiInfo();
		log.info("清除ApiServiceAccessController 缓存 end");
	}

	private static void initGranted() {
		// 初始化appkey所包含的api
		if (aclMap.isEmpty()) {
			aclMap = ((AccessBizOperate) SpringContextUtil.getBean("accessBizOperateImpl")).findUserIncludeApi();
		}

	}

	private static void initApiInfo() {
		if (apiMap.isEmpty()) {
			List<ApiInfo> apiList = ((AccessBizOperate) SpringContextUtil.getBean("accessBizOperateImpl")).findAllApiInfo();
			for (ApiInfo apiInfo : apiList) {
				apiMap.put(apiInfo.getApiName() + Constants.JOIN_SIGN + apiInfo.getApiVersion(), apiInfo);
			}
		}
	}

	/**
	 * 对类外的其它调用
	 * 
	 * @return
	 * @date 2016年4月29日
	 * @author houbl
	 */
	public static Map<Object, Object> getApiMap() {
		return apiMap;
	}
}
