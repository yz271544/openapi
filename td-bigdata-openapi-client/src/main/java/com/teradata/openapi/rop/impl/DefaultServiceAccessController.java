package com.teradata.openapi.rop.impl;

import com.teradata.openapi.rop.security.ServiceAccessController;
import com.teradata.openapi.rop.session.Session;

/**
 * <pre>
 * 功能说明：对调用的方法进行安全性检查
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultServiceAccessController implements ServiceAccessController {

	@Override
	public boolean isAppGranted(String appKey, String method, String version) {
		return true;
	}

	@Override
	public boolean isUserGranted(Session session, String method, String version) {
		return true;
	}
}
