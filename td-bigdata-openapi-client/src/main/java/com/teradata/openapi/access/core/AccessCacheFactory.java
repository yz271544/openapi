package com.teradata.openapi.access.core;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.teradata.openapi.access.biz.bean.CalcPrincCode;
import com.teradata.openapi.access.biz.service.AccessBizOperate;
import com.teradata.openapi.framework.util.SpringContextUtil;
import com.teradata.openapi.rop.cache.CacheManager;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 
 * 缓存工厂 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月28日 下午5:33:39
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class AccessCacheFactory implements CacheManager {

	private final static Log log = StaticLog.get();

	private static Map<Integer, CalcPrincCode> calcPrincMap = new HashMap<Integer, CalcPrincCode>();

	static {
		// 初始化计算法则信息
		initCalcPrinc();
	}

	@Override
	public void cleanCache() {
		calcPrincMap.clear();
	}

	@Override
	public void refreshCache() {
		log.info("清除AccessCacheFactory 缓存 start");
		this.cleanCache();
		initCalcPrinc();
		log.info("清除AccessCacheFactory 缓存 end");
	}

	private static void initCalcPrinc() {
		log.info("初始化计算法则信息");
		if (calcPrincMap.isEmpty()) {
			List<CalcPrincCode> calcList = ((AccessBizOperate) SpringContextUtil.getBean("accessBizOperateImpl")).findCalcPrincInfo();
			for (CalcPrincCode calcCode : calcList) {
				calcPrincMap.put(calcCode.getCalcPrincId(), calcCode);
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
	public static Map<Integer, CalcPrincCode> getCalcPrincMap() {
		return calcPrincMap;
	}

}
