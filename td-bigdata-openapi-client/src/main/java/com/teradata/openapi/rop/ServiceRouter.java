package com.teradata.openapi.rop;

import java.util.concurrent.ThreadPoolExecutor;

import org.springframework.context.ApplicationContext;
import org.springframework.format.support.FormattingConversionService;

import com.teradata.openapi.rop.event.RopEventListener;
import com.teradata.openapi.rop.security.InvokeTimesController;
import com.teradata.openapi.rop.security.SecurityManager;
import com.teradata.openapi.rop.session.SessionManager;

/**
 * <pre>
 *      ROP的服务路由器，服务方法必须位于@Controller的类中，服务方法使用{@link com.rop.annotation.ServiceMethod}注解，有两个合法的方法签名方式：
 * 签名方式1：有入参，且参数必须实现{@link RopRequest}接口，返回参数为{@link RopResponse}
 *   <code>
 *    @ServiceMethod("method1")
 *    RopResponse handleMethod1(RopRequest ropRequest){
 *        ...
 *    }
 *   </code>
 * 签名方式2：无入参，返回参数为{@link RopResponse}
 *   <code>
 *    @ServiceMethod("method1")
 *    RopResponse handleMethod1(){
 *        ...
 *    }
 *   </code>
 *   ROP框架会自动将请求参数的值绑定到入参请求对象中。
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface ServiceRouter {

	/**
	 * 注册拦截器
	 * 
	 * @param interceptor
	 */
	void addInterceptor(Interceptor interceptor);

	/**
	 * 注册监听器
	 * 
	 * @param listener
	 */
	void addListener(RopEventListener listener);

	/**
	 * 获取{@link RopContext}
	 * 
	 * @return
	 */
	RopContext getRopContext();

	/**
	 * ROP框架的总入口，一般框架实现，开发者无需关注。
	 * 
	 * @param request
	 * @param response
	 */
	void service(Object request, Object response);

	/**
	 * 设置Spring的上下文
	 * 
	 * @param applicationContext
	 */
	void setApplicationContext(ApplicationContext applicationContext);

	/**
	 * 设置扩展错误资源基名
	 * 
	 * @param extErrorBasename
	 */
	void setExtErrorBasename(String extErrorBasename);

	/**
	 * 允许设置多个资源文件
	 * 
	 * @param extErrorBasenames
	 */
	void setExtErrorBasenames(String[] extErrorBasenames);

	/**
	 * 设置格式化类型转换器
	 * 
	 * @param conversionService
	 */
	void setFormattingConversionService(FormattingConversionService conversionService);

	/**
	 * 设置服务调用限制管理器
	 * 
	 * @param invokeTimesController
	 */
	void setInvokeTimesController(InvokeTimesController invokeTimesController);

	/**
	 * 设置{@link com.rop.security.SecurityManager}
	 * 
	 * @param securityManager
	 */
	void setSecurityManager(SecurityManager securityManager);

	/**
	 * 设置所有服务的通用过期时间，单位为秒
	 * 
	 * @param serviceTimeoutSeconds
	 */
	void setServiceTimeoutSeconds(int serviceTimeoutSeconds);

	/**
	 * 添加会话管理器
	 * 
	 * @param sessionManager
	 */
	void setSessionManager(SessionManager sessionManager);

	/**
	 * 设置是否需要进行签名校验
	 * 
	 * @param signEnable
	 */
	void setSignEnable(boolean signEnable);

	/**
	 * 设置线程信息摆渡器
	 * 
	 * @param threadFerryClass
	 */
	void setThreadFerryClass(Class<? extends ThreadFerry> threadFerryClass);

	/**
	 * 注册
	 * 
	 * @param threadPoolExecutor
	 */
	void setThreadPoolExecutor(ThreadPoolExecutor threadPoolExecutor);

	/**
	 * 关闭服务路由器
	 */
	void shutdown();

	/**
	 * 启动服务路由器
	 */
	void startup();
}
