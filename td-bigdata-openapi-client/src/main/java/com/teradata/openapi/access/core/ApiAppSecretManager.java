package com.teradata.openapi.access.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teradata.openapi.access.biz.bean.UserInfo;
import com.teradata.openapi.access.biz.service.AccessBizOperate;
import com.teradata.openapi.framework.util.SpringContextUtil;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.cache.CacheManager;
import com.teradata.openapi.rop.security.AppSecretManager;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

public class ApiAppSecretManager implements AppSecretManager, CacheManager {

	private final static Log log = StaticLog.get();

	private static Map<String, String> appKeySecretMap = new HashMap<String, String>();

	private static Map<String, UserInfo> appKeyUserMap = new HashMap<String, UserInfo>();

	static {
		// 初始化所有appSecret
		initGranted();
	}

	@Override
	public String getSecret(String appKey) {
		return appKeySecretMap.get(appKey);
	}

	public UserInfo getUserInfo(String appKey) {
		return appKeyUserMap.get(appKey);
	}

	@Override
	public boolean isValidAppKey(String appKey) {
		return getSecret(appKey) != null;
	}

	@Override
	public void cleanCache() {
		appKeySecretMap.clear();
		appKeyUserMap.clear();
	}

	@Override
	public void refreshCache() {
		log.info("清除ApiAppSecretManager 缓存 start");
		this.cleanCache();
		initGranted();
		log.info("清除ApiAppSecretManager 缓存 end");
	}

	private static void initGranted() {
		if (appKeySecretMap.isEmpty()) {
			List<UserInfo> userList = ((AccessBizOperate) SpringContextUtil.getBean("accessBizOperateImpl")).findAllUserInfo();
			for (UserInfo userInfo : userList) {
				appKeySecretMap.put(userInfo.getLoginAcct(), userInfo.getLoginPwd());
				appKeyUserMap.put(userInfo.getLoginAcct(), userInfo);
			}
			// 添加系统默认用户(不用校验)
			appKeySecretMap.put(Constants.SYS_DEFAULT_APP_KEY, Constants.SYS_DEFAULT_APP_SECRET);
		}
	}

}
