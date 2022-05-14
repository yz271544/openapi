/**
 * 
 * 日 期：12-2-22
 */
package com.teradata.openapi.rop.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * <pre>
 * 通用的响应对象
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response")
public class CommonRopResponse {

	public static final CommonRopResponse FAILURE_RESPONSE = new CommonRopResponse(false);

	public static final CommonRopResponse SUCCESSFUL_RESPONSE = new CommonRopResponse(true);

	@XmlAttribute
	private boolean successful = false;

	public CommonRopResponse() {
	}

	private CommonRopResponse(boolean successful) {
		this.successful = successful;
	}

	public boolean isSuccessful() {
		return successful;
	}
}
