package com.teradata.openapi.rop.security;

/**
 * <pre>
 *    应用键管理器，可根据appKey获取对应的secret.
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface AppSecretManager {

	/**
	 * 获取应用程序的密钥
	 * 
	 * @param appKey
	 * @return
	 */
	String getSecret(String appKey);

	/**
	 * 是否是合法的appKey
	 * 
	 * @param appKey
	 * @return
	 */
	boolean isValidAppKey(String appKey);
}
