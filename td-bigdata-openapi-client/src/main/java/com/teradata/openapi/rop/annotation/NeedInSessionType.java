package com.teradata.openapi.rop.annotation;

/**
 * 
 * 是否需求会话检查<br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午6:01:57
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum NeedInSessionType {
	DEFAULT, NO, YES;

	public static boolean isNeedInSession(NeedInSessionType type) {
		if (YES == type || DEFAULT == type) {
			return true;
		} else {
			return false;
		}
	}
}
