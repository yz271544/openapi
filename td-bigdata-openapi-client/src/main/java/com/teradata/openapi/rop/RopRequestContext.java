package com.teradata.openapi.rop;

import java.util.Locale;
import java.util.Map;

import com.teradata.openapi.rop.annotation.HttpAction;
import com.teradata.openapi.rop.session.Session;

/**
 * <pre>
 *    接到服务请求后，将产生一个{@link RopRequestContext}上下文对象，它被本次请求直到返回响应的这个线程共享。
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface RopRequestContext {

	/**
	 * 绑定一个会话
	 * 
	 * @param session
	 */
	void addSession(String sessionId, Session session);

	/**
	 * 获取请求参数列表
	 * 
	 * @return
	 */
	Map<String, String> getAllParams();

	/**
	 * 获取应用的appKey
	 * 
	 * @return
	 */
	String getAppKey();

	/**
	 * 获取特定属性
	 * 
	 * @param name
	 * @return
	 */
	Object getAttribute(String name);

	/**
	 * 获取报文格式化参数
	 * 
	 * @return
	 */
	String getFormat();

	/**
	 * 获取请求的方法 如POST
	 */
	HttpAction getHttpAction();

	/**
	 * 获取客户端的IP
	 * 
	 * @return
	 */
	String getIp();

	/**
	 * 获取本地化对象
	 * 
	 * @return
	 */
	Locale getLocale();

	/**
	 * 获取响应的格式
	 * 
	 * @return
	 */
	MessageFormat getMessageFormat();

	/**
	 * 获取服务的方法
	 * 
	 * @return
	 */
	String getMethod();

	/**
	 * 获取请求参数值
	 * 
	 * @param paramName
	 * @return
	 */
	String getParamValue(String paramName);

	/**
	 * 获取请求的原对象（如HttpServletRequest）
	 * 
	 * @return
	 */
	Object getRawRequestObject();

	/**
	 * 获取请求的原响应对象（如HttpServletResponse）
	 * 
	 * @return
	 */
	Object getRawResponseObject();

	/**
	 * 获取请求ID，是一个唯一的UUID，每次请求对应一个唯一的ID
	 * 
	 * @return
	 */
	String getRequestId();

	/**
	 * 获取Rop的上下文
	 * 
	 * @return
	 */
	RopContext getRopContext();

	/**
	 * 返回响应对象
	 * 
	 * @return
	 */
	Object getRopResponse();

	/**
	 * 获取服务开始时间，单位为毫秒，为-1表示无意义
	 * 
	 * @return
	 */
	long getServiceBeginTime();

	/**
	 * 获取服务结束时间，单位为毫秒，为-1表示无意义
	 * 
	 * @return
	 */
	long getServiceEndTime();

	/**
	 * 获取服务方法对应的ApiMethod对象信息
	 * 
	 * @return
	 */
	ServiceMethodDefinition getServiceMethodDefinition();

	/**
	 * 获取服务的处理者
	 * 
	 * @return
	 */
	ServiceMethodHandler getServiceMethodHandler();

	/**
	 * 获取请求所对应的会话
	 * 
	 * @return
	 */
	Session getSession();

	/**
	 * 获取会话的ID
	 * 
	 * @return
	 */
	String getSessionId();

	/**
	 * 获取签名
	 * 
	 * @return
	 */
	String getSign();

	/**
	 * 获取服务的版本号
	 * 
	 * @return
	 */
	String getVersion();

	/**
	 * 获取请求类型
	 * 
	 * @return
	 */
	String getReqType();

	/**
	 * 获取请求格式
	 * 
	 * @return
	 */
	RequestTypeFormat getRequestTypeFormat();

	/**
	 * 获取返回字符集
	 * 
	 * @return
	 */
	String getCodeType();

	/**
	 * 获取响应参数（业务级）
	 * 
	 * @return
	 */
	String getFields();

	/**
	 * 获取请求参数(业务级)
	 * 
	 * @return
	 */
	Map<String, String> getReqParams();

	/**
	 * 判断是否是系统appKey
	 * 
	 * @return
	 * @author houbl
	 */
	boolean isSysAppKey();

	/**
	 * 该方法是否开启签名的功能
	 * 
	 * @return
	 */
	boolean isSignEnable();

	/**
	 * 删除会话，删除{@link #getSessionId()}对应的Session
	 */
	void removeSession();

	/**
	 * 设置属性的值
	 * 
	 * @param name
	 * @param value
	 */
	void setAttribute(String name, Object value);

	/**
	 * @param ropResponse
	 */
	void setRopResponse(Object ropResponse);

	/**
	 * 设置服务开始时间
	 * 
	 * @param serviceBeginTime
	 */
	void setServiceBeginTime(long serviceBeginTime);

	/**
	 * 设置服务开始时间
	 * 
	 * @param serviceEndTime
	 */
	void setServiceEndTime(long serviceEndTime);
}
