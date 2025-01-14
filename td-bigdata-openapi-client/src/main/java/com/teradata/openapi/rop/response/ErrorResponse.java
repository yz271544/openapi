package com.teradata.openapi.rop.response;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.teradata.openapi.rop.CommonConstant;
import com.teradata.openapi.rop.security.MainError;
import com.teradata.openapi.rop.security.MainErrorType;
import com.teradata.openapi.rop.security.MainErrors;
import com.teradata.openapi.rop.security.SubError;

/**
 * <pre>
 * 功能说明：
 * </pre>
 * 
 * @author 陈雄华
 * @version 1.0
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "error")
public class ErrorResponse {

	@XmlAttribute
	protected String code;

	@XmlAttribute
	protected String errorToken = CommonConstant.ERROR_TOKEN;

	@XmlElement
	protected String message;

	@XmlElement
	protected String solution;

	@XmlElementWrapper(name = "subErrors")
	@XmlElement(name = "subError")
	protected List<SubError> subErrors;

	public ErrorResponse() {
	}

	public ErrorResponse(MainError mainError) {
		this.code = mainError.getCode();
		this.message = mainError.getMessage();
		this.solution = mainError.getSolution();
		if (mainError.getSubErrors() != null && mainError.getSubErrors().size() > 0) {
			this.subErrors = mainError.getSubErrors();
		}
	}

	public void addSubError(SubError subError) {
		if (subErrors == null) {
			subErrors = new ArrayList<SubError>();
		}
		subErrors.add(subError);
	}

	public String getCode() {
		return code;
	}

	public String getErrorToken() {
		return errorToken;
	}

	protected MainError getInvalidArgumentsError(Locale locale) {
		return MainErrors.getError(MainErrorType.INVALID_ARGUMENTS, locale);
	}

	public String getMessage() {
		return message;
	}

	public String getSolution() {
		return solution;
	}

	public List<SubError> getSubErrors() {
		return subErrors;
	}

	public void setCode(String code) {
		this.code = code;
	}

	protected void setMainError(MainError mainError) {
		setCode(mainError.getCode());
		setMessage(mainError.getMessage());
		setSolution(mainError.getSolution());
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public void setSolution(String solution) {
		this.solution = solution;
	}

	public void setSubErrors(List<SubError> subErrors) {
		this.subErrors = subErrors;
	}

	/**
	 * 对服务名进行标准化处理：如book.upload转换为book-upload，
	 * 
	 * @param method
	 * @return
	 */
	protected String transform(String method) {
		if (method != null) {
			method = method.replace(".", "-");
			return method;
		} else {
			return "LACK_METHOD";
		}
	}

}
