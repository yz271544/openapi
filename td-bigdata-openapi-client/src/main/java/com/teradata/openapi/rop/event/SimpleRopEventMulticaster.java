package com.teradata.openapi.rop.event;

import java.util.concurrent.Executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class SimpleRopEventMulticaster extends AbstractRopEventMulticaster {

	private Executor executor;

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	public Executor getExecutor() {
		return executor;
	}

	@Override
	public void multicastEvent(final RopEvent event) {
		try {
			for (final RopEventListener listener : getRopEventListeners(event)) {
				Executor executor = getExecutor();
				if (executor != null) {
					executor.execute(new Runnable() {

						@Override
						public void run() {
							listener.onRopEvent(event);
						}
					});
				} else {
					listener.onRopEvent(event);
				}
			}
		}
		catch (Exception e) {
			logger.error("处理" + event.getClass().getName() + "事件发生异常", e);
		}
	}

	public void setExecutor(Executor executor) {
		this.executor = executor;
	}
}
