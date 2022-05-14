package com.teradata.openapi.rop;

/**
 * <pre>
 *    抽象拦截器，实现类仅需覆盖特定的方法即可。
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public abstract class AbstractInterceptor implements Interceptor {

	@Override
	public void beforeResponse(RopRequestContext ropRequestContext) {
	}

	@Override
	public void beforeService(RopRequestContext ropRequestContext) {
	}

	/**
	 * 放在拦截器链的最后
	 * 
	 * @return
	 */
	@Override
	public int getOrder() {
		return Integer.MAX_VALUE;
	}

	@Override
	public boolean isMatch(RopRequestContext ropRequestContext) {
		return true;
	}
}
