/**
 * 
 * 日 期：12-2-14
 */
package com.teradata.openapi.rop.security;

import java.util.ArrayList;
import java.util.List;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
public class SimpleMainError implements MainError {

	private String code;

	private String message;

	private String solution;

	private List<SubError> subErrors = new ArrayList<SubError>();

	public SimpleMainError(String code, String message, String solution) {
		this.code = code;
		this.message = message;
		this.solution = solution;
	}

	@Override
	public MainError addSubError(SubError subError) {
		this.subErrors.add(subError);
		return this;
	}

	@Override
	public String getCode() {
		return code;
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getSolution() {
		return solution;
	}

	@Override
	public List<SubError> getSubErrors() {
		return this.subErrors;
	}

	public void setSubErrors(List<SubError> subErrors) {
		this.subErrors = subErrors;
	}
}
