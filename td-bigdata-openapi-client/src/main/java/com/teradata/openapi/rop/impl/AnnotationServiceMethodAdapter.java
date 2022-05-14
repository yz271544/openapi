package com.teradata.openapi.rop.impl;

import java.lang.reflect.InvocationTargetException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;

import com.teradata.openapi.rop.RopRequest;
import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.ServiceMethodAdapter;
import com.teradata.openapi.rop.ServiceMethodHandler;

/**
 * <pre>
 * 通过该服务方法适配器调用目标的服务方法
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class AnnotationServiceMethodAdapter implements ServiceMethodAdapter {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	private ParameterNameDiscoverer parameterNameDiscoverer = new LocalVariableTableParameterNameDiscoverer();

	/**
	 * 调用ROP服务方法
	 * 
	 * @param ropRequest
	 * @return
	 */
	@Override
	public Object invokeServiceMethod(RopRequest ropRequest) {
		try {
			RopRequestContext ropRequestContext = ropRequest.getRopRequestContext();
			// 分析上下文中的错误
			ServiceMethodHandler serviceMethodHandler = ropRequestContext.getServiceMethodHandler();
			if (logger.isDebugEnabled()) {
				logger.debug("执行" + serviceMethodHandler.getHandler().getClass() + "." + serviceMethodHandler.getHandlerMethod().getName());
			}
			if (serviceMethodHandler.isHandlerMethodWithParameter()) {
				return serviceMethodHandler.getHandlerMethod().invoke(serviceMethodHandler.getHandler(), ropRequest);
			} else {
				return serviceMethodHandler.getHandlerMethod().invoke(serviceMethodHandler.getHandler());
			}
		}
		catch (Throwable e) {
			if (e instanceof InvocationTargetException) {
				InvocationTargetException inve = (InvocationTargetException) e;
				throw new RuntimeException(inve.getTargetException());
			} else {
				throw new RuntimeException(e);
			}
		}
	}

}
