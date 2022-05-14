package com.teradata.openapi.rop;

import com.teradata.openapi.rop.annotation.HttpAction;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class ServiceMethodDefinition {

	/**
	 * 默认的组
	 */
	public static final String DEFAULT_GROUP = "DEFAULT";

	/**
	 * 默认分组标识
	 */
	public static final String DEFAULT_GROUP_TITLE = "DEFAULT GROUP";

	/**
	 * HTTP请求的方法
	 */
	private HttpAction[] httpAction;

	/**
	 * 是否忽略服务请求签名的校验，默认为false
	 */
	private boolean ignoreSign = false;

	/**
	 * API的方法
	 */
	private String method;

	/**
	 * API方法所属组名
	 */
	private String methodGroup = DEFAULT_GROUP;

	/**
	 * API方法组名的标识
	 */
	private String methodGroupTitle;

	/**
	 * API的方法的标识
	 */
	private String methodTitle;

	/**
	 * 是否需要进行会话校验
	 */
	private boolean needInSession;

	/**
	 * 服务方法是否已经过期
	 */
	private boolean obsoleted = false;

	/**
	 * API所属的标签
	 */
	private String[] tags = {};

	/**
	 * 过期时间，单位为秒，0或负数表示不过期
	 */
	private int timeout = -9999;

	/**
	 * 对应的版本号，如果为null或""表示不区分版本
	 */
	private String version = null;

	public HttpAction[] getHttpAction() {
		return httpAction;
	}

	public String getMethod() {
		return method;
	}

	public String getMethodGroup() {
		return methodGroup;
	}

	public String getMethodGroupTitle() {
		return methodGroupTitle;
	}

	public String getMethodTitle() {
		return methodTitle;
	}

	public String[] getTags() {
		return tags;
	}

	public int getTimeout() {
		return this.timeout;
	}

	public String getVersion() {
		return version;
	}

	public boolean isIgnoreSign() {
		return ignoreSign;
	}

	public boolean isNeedInSession() {
		return needInSession;
	}

	public boolean isObsoleted() {
		return obsoleted;
	}

	public void setHttpAction(HttpAction[] httpAction) {
		this.httpAction = httpAction;
	}

	public void setIgnoreSign(boolean ignoreSign) {
		this.ignoreSign = ignoreSign;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	public void setMethodGroup(String methodGroup) {
		this.methodGroup = methodGroup;
	}

	public void setMethodGroupTitle(String methodGroupTitle) {
		this.methodGroupTitle = methodGroupTitle;
	}

	public void setMethodTitle(String methodTitle) {
		this.methodTitle = methodTitle;
	}

	public void setNeedInSession(boolean needInSession) {
		this.needInSession = needInSession;
	}

	public void setObsoleted(boolean obsoleted) {
		this.obsoleted = obsoleted;
	}

	public void setTags(String[] tags) {
		this.tags = tags;
	}

	public void setTimeout(int timeout) {
		this.timeout = timeout;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
