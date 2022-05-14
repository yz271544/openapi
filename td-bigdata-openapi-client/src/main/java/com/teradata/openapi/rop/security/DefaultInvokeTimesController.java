package com.teradata.openapi.rop.security;

import com.teradata.openapi.rop.session.Session;

/**
 * <pre>
 * 默认的实现
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultInvokeTimesController implements InvokeTimesController {

	@Override
	public void caculateInvokeTimes(String appKey, Session session) {
	}

	@Override
	public boolean isAppInvokeFrequencyExceed(String appKey) {
		return false;
	}

	@Override
	public boolean isAppInvokeLimitExceed(String appKey) {
		return false;
	}

	@Override
	public boolean isSessionInvokeLimitExceed(String appKey, String sessionId) {
		return false;
	}

	@Override
	public boolean isUserInvokeLimitExceed(String appKey, Session session) {
		return false;
	}
}
