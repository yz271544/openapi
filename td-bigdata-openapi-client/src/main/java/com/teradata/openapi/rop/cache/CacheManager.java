package com.teradata.openapi.rop.cache;

/**
 * 
 * 缓存管理器 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月22日 下午6:14:03
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface CacheManager {

	/**
	 * 清除缓存
	 * 
	 * @date 2016年4月22日
	 * @author houbl
	 */
	public void cleanCache();

	/**
	 * 刷新缓存
	 * 
	 * @date 2016年4月22日
	 * @author houbl
	 */
	public void refreshCache();

}
