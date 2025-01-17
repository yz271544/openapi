package com.teradata.openapi.rop.event;

import java.util.EventListener;

/**
 * <pre>
 * 监听所有Rop框架的事件
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface RopEventListener<E extends RopEvent> extends EventListener {

	/**
	 * 执行的顺序号
	 * 
	 * @return
	 */
	int getOrder();

	/**
	 * 响应事件
	 * 
	 * @param ropEvent
	 */
	void onRopEvent(E ropEvent);
}
