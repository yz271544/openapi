package com.teradata.openapi.rop.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.security.MainError;
import com.teradata.openapi.rop.security.MainErrorType;
import com.teradata.openapi.rop.security.MainErrors;

/**
 * <pre>
 *   当服务平台资源耗尽（超过最大线程数且列队排满后）
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class RejectedServiceResponse extends ErrorResponse {

	public RejectedServiceResponse() {
	}

	public RejectedServiceResponse(RopRequestContext context) {
		MainError mainError = MainErrors.getError(MainErrorType.FORBIDDEN_REQUEST, context.getLocale(), context.getMethod(),
		                                          context.getVersion());
		setMainError(mainError);
	}
}
