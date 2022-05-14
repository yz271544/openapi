package com.teradata.openapi.rop.config;

import com.teradata.openapi.rop.Interceptor;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class InterceptorHolder {

	private Interceptor interceptor;

	public InterceptorHolder(Interceptor interceptor) {
		this.interceptor = interceptor;
	}

	public Interceptor getInterceptor() {
		return interceptor;
	}
}
