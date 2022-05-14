package com.teradata.openapi.rop.event;

import com.teradata.openapi.rop.RopRequestContext;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class AfterDoServiceEvent extends RopEvent {

	private RopRequestContext ropRequestContext;

	public AfterDoServiceEvent(Object source, RopRequestContext ropRequestContext) {
		super(source, ropRequestContext.getRopContext());
		this.ropRequestContext = ropRequestContext;
	}

	public RopRequestContext getRopRequestContext() {
		return ropRequestContext;
	}

	public long getServiceBeginTime() {
		return ropRequestContext.getServiceBeginTime();
	}

	public long getServiceEndTime() {
		return ropRequestContext.getServiceEndTime();
	}
}
