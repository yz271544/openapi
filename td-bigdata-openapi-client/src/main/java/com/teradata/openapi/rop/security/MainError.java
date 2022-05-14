package com.teradata.openapi.rop.security;

import java.util.List;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public interface MainError {

	MainError addSubError(SubError subError);

	String getCode();

	String getMessage();

	String getSolution();

	List<SubError> getSubErrors();

}
