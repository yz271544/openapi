package com.teradata.openapi.rop;

import java.lang.reflect.Method;
import java.util.List;

/**
 * <pre>
 * 服务处理器的相关信息
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class ServiceMethodHandler {

	public static String methodWithVersion(String methodName, String version) {
		return methodName + "#" + version;
	}

	// 处理器对象
	private Object handler;

	// 处理器的处理方法
	private Method handlerMethod;

	// 无需签名的字段列表
	private List<String> ignoreSignFieldNames;

	// 处理方法的请求对象类
	private Class<? extends RopRequest> requestType = RopRequest.class;

	// 是否是实现类
	private boolean ropRequestImplType;

	private ServiceMethodDefinition serviceMethodDefinition;

	// 属性类型为FileItem的字段列表
	private List<String> uploadFileFieldNames;

	public ServiceMethodHandler() {
	}

	public Object getHandler() {
		return handler;
	}

	public Method getHandlerMethod() {
		return handlerMethod;
	}

	public List<String> getIgnoreSignFieldNames() {
		return ignoreSignFieldNames;
	}

	public Class<? extends RopRequest> getRequestType() {
		return requestType;
	}

	public ServiceMethodDefinition getServiceMethodDefinition() {
		return serviceMethodDefinition;
	}

	public List<String> getUploadFileFieldNames() {
		return uploadFileFieldNames;
	}

	public boolean hasUploadFiles() {
		return uploadFileFieldNames != null && uploadFileFieldNames.size() > 0;
	}

	public boolean isHandlerMethodWithParameter() {
		return this.requestType != null;
	}

	public boolean isRopRequestImplType() {
		return ropRequestImplType;
	}

	public void setHandler(Object handler) {
		this.handler = handler;
	}

	public void setHandlerMethod(Method handlerMethod) {
		this.handlerMethod = handlerMethod;
	}

	public void setIgnoreSignFieldNames(List<String> ignoreSignFieldNames) {
		this.ignoreSignFieldNames = ignoreSignFieldNames;
	}

	public void setRequestType(Class<? extends RopRequest> requestType) {
		this.requestType = requestType;
	}

	public void setRopRequestImplType(boolean ropRequestImplType) {
		this.ropRequestImplType = ropRequestImplType;
	}

	public void setServiceMethodDefinition(ServiceMethodDefinition serviceMethodDefinition) {
		this.serviceMethodDefinition = serviceMethodDefinition;
	}

	public void setUploadFileFieldNames(List<String> uploadFileFieldNames) {
		this.uploadFileFieldNames = uploadFileFieldNames;
	}
}
