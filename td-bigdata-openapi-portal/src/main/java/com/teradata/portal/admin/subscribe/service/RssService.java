package com.teradata.portal.admin.subscribe.service;

import com.github.miemiedev.mybatis.paginator.domain.PageList;
import com.teradata.portal.admin.subscribe.pojo.ApiRssInfo;
import com.teradata.portal.admin.subscribe.pojo.FtpUseInfo;
import com.teradata.portal.web.sandbox.vojo.ApiFormArg;

/**
 * 订阅管理业务层. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年6月16日 上午10:55:27
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface RssService {

	/**
	 * 分页查询订阅信息
	 * 
	 * @param rssInfo
	 * @return
	 * @author houbl
	 */
	public PageList<ApiRssInfo> queryRssInfosList(ApiRssInfo rssInfo);

	/**
	 * 根据订阅ID查询其订阅信息
	 * 
	 * @param rssId
	 * @return
	 * @author houbl
	 */
	public ApiRssInfo queryRssInfoById(Integer rssId);

	/**
	 * 保存与修改订阅信息
	 * 
	 * @param rssInfo
	 * @param method
	 * @author houbl
	 */
	public void saveRssInfo(ApiRssInfo rssInfo, String method) throws Exception;

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
	public Boolean callFtpApiData(FtpUseInfo ftpInfo, ApiFormArg formArg, String invokeMethod, String appSecret);
}
