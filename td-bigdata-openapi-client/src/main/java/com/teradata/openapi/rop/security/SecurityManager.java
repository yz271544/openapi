package com.teradata.openapi.rop.security;

import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.session.SessionManager;

/**
 * <pre>
 *   负责对请求数据、会话、业务安全、应用密钥安全进行检查并返回相应的错误
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface SecurityManager {

	/**
	 * 获取应用密钥管理器
	 * 
	 * @return
	 */
	void setAppSecretManager(AppSecretManager appSecretManager);

	/**
	 * 文件上传控制器
	 * 
	 * @param fileUploadController
	 */
	void setFileUploadController(FileUploadController fileUploadController);

	/**
	 * 设置服务调度次数管理器
	 * 
	 * @param invokeTimesController
	 */
	void setInvokeTimesController(InvokeTimesController invokeTimesController);

	/**
	 * 获取安全管理器
	 * 
	 * @return
	 */
	void setServiceAccessController(ServiceAccessController serviceAccessController);

	/**
	 * 设置会话管理器，以验证会话的合法性
	 * 
	 * @return
	 */
	void setSessionManager(SessionManager sessionManager);

	/**
	 * 验证其它的事项：包括业务参数，业务安全性，会话安全等
	 * 
	 * @param ropRequestContext
	 * @return
	 */
	MainError validateOther(RopRequestContext ropRequestContext);

	/**
	 * 对请求服务的上下文进行检查校验
	 * 
	 * @param ropRequestContext
	 * @return
	 */
	MainError validateSystemParameters(RopRequestContext ropRequestContext);
}
