package com.teradata.portal.web.system.exception;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fasterxml.jackson.databind.ObjectMapper;

/**
 * Controller 统一异常处理类
 * 
 * @author houbl
 * 
 */
public class FrameHandlerExceptionResolver implements HandlerExceptionResolver {

	final Logger LOG = LoggerFactory.getLogger(FrameHandlerExceptionResolver.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
		LOG.error("截获异常", ex);

		ExceptionModel exceptionModel = null;
		if (ex instanceof ServiceRopException) {
			exceptionModel = generateExceptionModel(ex);
		} else {
			exceptionModel = generateExceptionModel(BusinessException.wrapException(ex));
		}

		if (isJsonRequest(request)) {
			LOG.debug("json请求，返回异常的json形式");

			// 设置response的header，如果不设置contentType，则默认是text/html，则json串在前端被解析为string，而不是object
			response.setContentType("application/json");
			response.setCharacterEncoding("UTF-8");

			PrintWriter writer = null;
			try {
				writer = response.getWriter();
				new ObjectMapper().writeValue(writer, exceptionModel);
				writer.flush();
			}
			catch (IOException e) {
				LOG.error("写入response失败", e);
				e.printStackTrace();
			}
			finally {
				if (writer != null)
					writer.close();
			}
			return null;

		} else {
			String viewName = "include/error";
			LOG.debug("非JSON请求，直接设置 exception model");
			Map<String, Object> model = new HashMap<String, Object>();
			model.put("exception", exceptionModel);
			return new ModelAndView(viewName, model);

		}
	}

	/**
	 * 判断是否为JSON请求
	 * 
	 * @param request
	 * @return
	 */
	private boolean isJsonRequest(HttpServletRequest request) {
		try {
			return request.getHeader("accept").indexOf("application/json") != -1
			        || (request.getHeader("X-Requested-With") != null && request.getHeader("X-Requested-With").indexOf("XMLHttpRequest") != -1);
		}
		catch (Exception e) {
			LOG.error("获取请求头发生错误", e);
			return false;
		}
	}

	private ExceptionModel generateExceptionModel(Exception ex) {
		ExceptionModel exceptionModel = new ExceptionModel();
		if (ex instanceof ServiceRopException) {
			ServiceRopException serExc = (ServiceRopException) ex;
			exceptionModel.setCode(serExc.getStatusCode());
			exceptionModel.setDesc(serExc.getStatusDesc());
			if (StringUtils.isBlank(serExc.getMessage())) {
				exceptionModel.setMessage(serExc.getMessage());
			}
		} else {
			BusinessException busiExc = (BusinessException) ex;
			exceptionModel.setCode(busiExc.getStatusCode().getCode());
			exceptionModel.setDesc(busiExc.getStatusCode().getDescription());
			if (StringUtils.isBlank(busiExc.getMessage())) {
				exceptionModel.setMessage(busiExc.getTraceMessage());
			}
		}

		return exceptionModel;
	}

}
