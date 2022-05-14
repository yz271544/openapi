package com.teradata.openapi.rop.annotation;

/**
 * 
 * 是否需求进行签名校验.{@link #DEFAULT}是系统预留的，请不要在实际中使用
 * <p>
 * Copyright: Copyright (c) 2016年3月29日 下午6:00:41
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public enum IgnoreSignType {

	DEFAULT, NO, YES;

	public static boolean isIgnoreSign(IgnoreSignType type) {
		if (NO == type || DEFAULT == type) {
			return false;
		} else {
			return true;
		}
	}
}
