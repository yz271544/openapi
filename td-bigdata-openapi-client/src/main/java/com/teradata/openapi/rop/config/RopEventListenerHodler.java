package com.teradata.openapi.rop.config;

import com.teradata.openapi.rop.event.RopEventListener;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class RopEventListenerHodler {

	private RopEventListener ropEventListener;

	public RopEventListenerHodler(RopEventListener ropEventListener) {
		this.ropEventListener = ropEventListener;
	}

	public RopEventListener getRopEventListener() {
		return ropEventListener;
	}
}
