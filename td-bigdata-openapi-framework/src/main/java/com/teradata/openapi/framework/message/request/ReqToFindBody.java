package com.teradata.openapi.framework.message.request;

import com.teradata.openapi.framework.util.FastJSONUtil;

import java.io.Serializable;

public class ReqToFindBody implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 675771998589263601L;

	private Long clientTreadId;

	// body json
	private String messageBody;

	public String getMessageBody() {
		return messageBody;
	}

	public void setMessageBody(String messageBody) {
		this.messageBody = messageBody;
	}

	public Long getClientTreadId() {
		return clientTreadId;
	}

	public void setClientTreadId(Long clientTreadId) {
		this.clientTreadId = clientTreadId;
	}

	@Override
	public String toString(){
		return FastJSONUtil.serialize(this);
	}
}
