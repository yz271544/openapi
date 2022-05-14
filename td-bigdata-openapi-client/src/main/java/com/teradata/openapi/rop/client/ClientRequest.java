package com.teradata.openapi.rop.client;

import java.util.Map;

import com.teradata.openapi.rop.RopRequest;

/**
 * 
 * 每个请求对应一个ClientRequest对象<br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年4月7日 上午11:43:19
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public interface ClientRequest {

	/**
	 * 添加请求参数,默认需要签名，如果类已经标注了{@link com.rop.annotation.IgnoreSign}则始终不加入签名
	 * 
	 * @param paramName
	 * @param paramValue
	 * @return
	 */
	ClientRequest addParam(String paramName, Object paramValue);

	/**
	 * 清除参数列表
	 * 
	 * @return
	 */
	ClientRequest clearParam();

	/**
	 * 使用GET发送服务请求
	 * 
	 * @param ropResponseClass
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param <T>
	 * @return
	 */
	<T> CompositeResponse get(Class<T> ropResponseClass, String methodName, String version, String reqType);

	/**
	 * 使用GET发送ropRequest的请求
	 * 
	 * @param ropRequest
	 * @param ropResponseClass
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param <T>
	 * @return
	 */
	<T> CompositeResponse get(RopRequest ropRequest, Class<T> ropResponseClass, String methodName, String version, String reqType);

	/**
	 * 使用POST发起请求
	 * 
	 * @param ropResponseClass
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param <T>
	 * @return
	 */
	<T> CompositeResponse post(Class<T> ropResponseClass, String methodName, String version, String reqType);

	/**
	 * 直接使用 ropRequest发送请求
	 * 
	 * @param ropRequest
	 * @param ropResponseClass
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param <T>
	 * @return
	 */
	<T> CompositeResponse post(RopRequest ropRequest, Class<T> ropResponseClass, String methodName, String version, String reqType);

	/**
	 * 使用GET发送服务请求
	 * 
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param String
	 * @return
	 */
	String get(String methodName, String version, String reqType);

	/**
	 * 使用GET发送ropRequest的请求
	 * 
	 * @param ropRequest
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param String
	 * @return
	 */
	String get(RopRequest ropRequest, String methodName, String version, String reqType);

	/**
	 * 使用GET发送ropRequest的请求(扩展)
	 * 
	 * @param ropRequest
	 * @param extParam
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @return
	 * @author houbl
	 */
	String get(RopRequest ropRequest, Map<String, String> extParam, String methodName, String version, String reqType);

	/**
	 * 使用POST发起请求
	 * 
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param String
	 * @return
	 */
	String post(String methodName, String version, String reqType);

	/**
	 * 直接使用 ropRequest发送请求
	 * 
	 * @param ropRequest
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @param String
	 * @return
	 */
	String post(RopRequest ropRequest, String methodName, String version, String reqType);

	/**
	 * 使用 ropRequest发送请求(扩展)
	 * 
	 * @param ropRequest
	 * @param extParam
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @return
	 * @author houbl
	 */
	String post(RopRequest ropRequest, Map<String, String> extParam, String methodName, String version, String reqType);

	/**
	 * 获取客户端URL
	 * 
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @return
	 * @author houbl
	 */
	String buildUrl(String methodName, String version, String reqType);

	/**
	 * 获取客户端URL
	 * 
	 * @param ropRequest
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @return
	 * @author houbl
	 */
	String buildUrl(RopRequest ropRequest, String methodName, String version, String reqType);

	/**
	 * 获取客户端URL(扩展)
	 * 
	 * @param ropRequest
	 * @param extParam
	 * @param methodName
	 * @param version
	 * @param reqType
	 * @return
	 * @author houbl
	 */
	String buildUrl(RopRequest ropRequest, Map<String, String> extParam, String methodName, String version, String reqType);
}
