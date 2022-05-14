package com.teradata.openapi.rop.event;

import com.teradata.openapi.rop.RopContext;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class PreCloseRopEvent extends RopEvent {

	public PreCloseRopEvent(Object source, RopContext ropContext) {
		super(source, ropContext);
	}
}
