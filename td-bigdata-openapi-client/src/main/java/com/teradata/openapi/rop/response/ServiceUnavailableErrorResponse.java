package com.teradata.openapi.rop.response;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Locale;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import com.teradata.openapi.rop.security.MainError;
import com.teradata.openapi.rop.security.SubError;
import com.teradata.openapi.rop.security.SubErrorType;
import com.teradata.openapi.rop.security.SubErrors;

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
public class ServiceUnavailableErrorResponse extends ErrorResponse {

	private static final String ISP = "isp.";

	private static final String SERVICE_UNAVAILABLE = "-service-unavailable";

	// 注意，这个不能删除，否则无法进行流化
	public ServiceUnavailableErrorResponse() {
	}

	public ServiceUnavailableErrorResponse(String method, Locale locale) {
		MainError mainError = SubErrors.getMainError(SubErrorType.ISP_SERVICE_UNAVAILABLE, locale);
		String errorCodeKey = ISP + transform(method) + SERVICE_UNAVAILABLE;
		SubError subError = SubErrors.getSubError(errorCodeKey, SubErrorType.ISP_SERVICE_UNAVAILABLE.value(), locale, method, "NONE",
		                                          "NONE");
		ArrayList<SubError> subErrors = new ArrayList<SubError>();
		subErrors.add(subError);

		setMainError(mainError);
		setSubErrors(subErrors);
	}

	public ServiceUnavailableErrorResponse(String method, Locale locale, Throwable throwable) {
		MainError mainError = SubErrors.getMainError(SubErrorType.ISP_SERVICE_UNAVAILABLE, locale);

		ArrayList<SubError> subErrors = new ArrayList<SubError>();

		String errorCodeKey = ISP + transform(method) + SERVICE_UNAVAILABLE;
		Throwable srcThrowable = throwable;
		if (throwable.getCause() != null) {
			srcThrowable = throwable.getCause();
		}
		SubError subError = SubErrors.getSubError(errorCodeKey, SubErrorType.ISP_SERVICE_UNAVAILABLE.value(), locale, method, srcThrowable
		        .getClass().getName(), getThrowableInfo(throwable));
		subErrors.add(subError);

		setSubErrors(subErrors);
		setMainError(mainError);
	}

	private String getThrowableInfo(Throwable throwable) {
		ByteArrayOutputStream outputStream = new ByteArrayOutputStream(1024);
		PrintStream printStream = new PrintStream(outputStream);
		throwable.printStackTrace(printStream);
		return outputStream.toString();
	}
}
