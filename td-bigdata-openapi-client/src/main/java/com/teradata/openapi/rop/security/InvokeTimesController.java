package com.teradata.openapi.rop.security;

import com.teradata.openapi.rop.session.Session;

/**
 * <pre>
 * 服务访问次数及频率的控制管理器
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface InvokeTimesController {

	/**
	 * 计算应用、会话及用户服务调度总数
	 * 
	 * @param appKey
	 * @param session
	 */
	void caculateInvokeTimes(String appKey, Session session);

	/**
	 * 应用对服务的访问频率是否超限
	 * 
	 * @param appKey
	 * @return
	 */
	boolean isAppInvokeFrequencyExceed(String appKey);

	/**
	 * 应用的服务访问次数是否超限
	 * 
	 * @param appKey
	 * @return
	 */
	boolean isAppInvokeLimitExceed(String appKey);

	/**
	 * 某个会话的服务访问次数是否超限
	 * 
	 * @param sessionId
	 * @return
	 */
	boolean isSessionInvokeLimitExceed(String appKey, String sessionId);

	/**
	 * 用户服务访问次数是否超限
	 * 
	 * @param session
	 * @return
	 */
	boolean isUserInvokeLimitExceed(String appKey, Session session);
}
