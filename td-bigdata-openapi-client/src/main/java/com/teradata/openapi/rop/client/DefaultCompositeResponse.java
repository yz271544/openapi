package com.teradata.openapi.rop.client;

import com.teradata.openapi.rop.response.ErrorResponse;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class DefaultCompositeResponse<T> implements CompositeResponse {

	private ErrorResponse errorResponse;

	private boolean successful;

	private T successRopResponse;

	public DefaultCompositeResponse(boolean successful) {
		this.successful = successful;
	}

	@Override
	public ErrorResponse getErrorResponse() {
		return this.errorResponse;
	}

	@Override
	public T getSuccessResponse() {
		return this.successRopResponse;
	}

	@Override
	public boolean isSuccessful() {
		return successful;
	}

	public void setErrorResponse(ErrorResponse errorResponse) {
		this.errorResponse = errorResponse;
	}

	public void setSuccessRopResponse(T successRopResponse) {
		this.successRopResponse = successRopResponse;
	}
}
