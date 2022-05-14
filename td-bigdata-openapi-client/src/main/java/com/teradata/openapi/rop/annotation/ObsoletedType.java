package com.teradata.openapi.rop.annotation;

/**
 * 
 * 服务方法是否已经过期，过期的服务方法不能再访问 <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午6:02:29
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum ObsoletedType {
	DEFAULT, NO, YES;

	public static boolean isObsoleted(ObsoletedType type) {
		if (YES == type) {
			return true;
		} else {
			return false;
		}
	}
}
