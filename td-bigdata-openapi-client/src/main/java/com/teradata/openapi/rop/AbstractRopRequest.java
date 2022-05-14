package com.teradata.openapi.rop;

import com.teradata.openapi.rop.annotation.Temporary;

/**
 * <pre>
 * 所有请求对象应该通过扩展此抽象类实现
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public abstract class AbstractRopRequest implements RopRequest {

	@Temporary
	private RopRequestContext ropRequestContext;

	@Override
	public RopRequestContext getRopRequestContext() {
		return ropRequestContext;
	}

	public final void setRopRequestContext(RopRequestContext ropRequestContext) {
		this.ropRequestContext = ropRequestContext;
	}
}
