package com.teradata.openapi.rop.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;

import org.apache.commons.lang3.StringUtils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import com.teradata.openapi.access.biz.bean.ApiInfo;
import com.teradata.openapi.access.biz.bean.CalcPrincCode;
import com.teradata.openapi.access.biz.bean.StructApiArg;
import com.teradata.openapi.access.core.AccessCacheFactory;
import com.teradata.openapi.access.core.ApiServiceAccessController;
import com.teradata.openapi.framework.message.request.FormDetail;
import com.teradata.openapi.framework.util.FastJSONUtil;
import com.teradata.openapi.rop.Constants;
import com.teradata.openapi.rop.MessageFormat;
import com.teradata.openapi.rop.RequestTypeFormat;
import com.teradata.openapi.rop.RopContext;
import com.teradata.openapi.rop.RopException;
import com.teradata.openapi.rop.RopRequestContext;
import com.teradata.openapi.rop.ServiceMethodHandler;
import com.teradata.openapi.rop.config.SystemParameterNames;
import com.teradata.openapi.rop.impl.DefaultServiceAccessController;
import com.teradata.openapi.rop.request.UploadFileUtils;
import com.teradata.openapi.rop.session.SessionManager;
import com.teradata.openapi.rop.utils.RopUtils;
import com.xiaoleilu.hutool.log.Log;
import com.xiaoleilu.hutool.log.StaticLog;

/**
 * 
 * 安全处理器. <br>
 * 类详细说明.
 * <p>
 * Copyright: Copyright (c) 2016年8月29日 上午11:33:46
 * <p>
 * Company: TERADATA
 * <p>
 * 
 * @author Baolin.Hou@Teradata.com
 * @version 1.0.0
 */
public class DefaultSecurityManager implements SecurityManager {

	private final static Log log = StaticLog.get();

	private static final Map<String, SubErrorType> INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS = new LinkedHashMap<String, SubErrorType>();

	static {
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("typeMismatch", SubErrorType.ISV_PARAMETERS_MISMATCH);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("NotNull", SubErrorType.ISV_MISSING_PARAMETER);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("NotEmpty", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Size", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Range", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Pattern", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Min", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Max", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("DecimalMin", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("DecimalMax", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Digits", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Past", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("Future", SubErrorType.ISV_INVALID_PARAMETE);
		INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.put("AssertFalse", SubErrorType.ISV_INVALID_PARAMETE);
	}

	protected AppSecretManager appSecretManager = new FileBaseAppSecretManager();

	protected FileUploadController fileUploadController;

	protected InvokeTimesController invokeTimesController;

	protected ServiceAccessController serviceAccessController = new DefaultServiceAccessController();

	protected SessionManager sessionManager;

	private MainError checkInvokeTimesLimit(RopRequestContext rrctx) {
		if (invokeTimesController.isAppInvokeFrequencyExceed(rrctx.getAppKey())) {
			return MainErrors.getError(MainErrorType.EXCEED_APP_INVOKE_FREQUENCY_LIMITED, rrctx.getLocale());
		} else if (invokeTimesController.isAppInvokeLimitExceed(rrctx.getAppKey())) {
			return MainErrors.getError(MainErrorType.EXCEED_APP_INVOKE_LIMITED, rrctx.getLocale());
		} else if (invokeTimesController.isSessionInvokeLimitExceed(rrctx.getAppKey(), rrctx.getSessionId())) {
			return MainErrors.getError(MainErrorType.EXCEED_SESSION_INVOKE_LIMITED, rrctx.getLocale());
		} else if (invokeTimesController.isUserInvokeLimitExceed(rrctx.getAppKey(), rrctx.getSession())) {
			return MainErrors.getError(MainErrorType.EXCEED_USER_INVOKE_LIMITED, rrctx.getLocale());
		} else {
			return null;
		}
	}

	private MainError checkServiceAccessAllow(RopRequestContext smc) {
		if (!getServiceAccessController().isAppGranted(smc.getAppKey(), smc.getMethod(), smc.getVersion())) {
			MainError mainError = SubErrors.getMainError(SubErrorType.ISV_INVALID_PERMISSION, smc.getLocale());
			SubError subError = SubErrors.getSubError(SubErrorType.ISV_INVALID_PERMISSION.value(), SubErrorType.ISV_INVALID_PERMISSION.value(),
			                                          smc.getLocale());
			mainError.addSubError(subError);
			if (mainError != null && log.isErrorEnabled()) {
				log.debug("未向ISV开放该服务的执行权限(" + smc.getMethod() + ")");
			}
			return mainError;
		} else {
			if (!getServiceAccessController().isUserGranted(smc.getSession(), smc.getMethod(), smc.getVersion())) {
				MainError mainError = MainErrors.getError(MainErrorType.INSUFFICIENT_USER_PERMISSIONS, smc.getLocale(), smc.getMethod(),
				                                          smc.getVersion());
				SubError subError = SubErrors.getSubError(SubErrorType.ISV_INVALID_PERMISSION.value(),
				                                          SubErrorType.ISV_INVALID_PERMISSION.value(), smc.getLocale());
				mainError.addSubError(subError);
				if (mainError != null && log.isErrorEnabled()) {
					log.debug("未向会话用户开放该服务的执行权限(" + smc.getMethod() + ")");
				}
				return mainError;
			}
			return null;
		}
	}

	/**
	 * 是否是合法的会话
	 * 
	 * @param context
	 * @return
	 */
	private MainError checkSession(RopRequestContext context) {
		// 需要进行session检查
		if (context.getServiceMethodHandler() != null && context.getServiceMethodHandler().getServiceMethodDefinition().isNeedInSession()) {
			if (context.getSessionId() == null) {
				return MainErrors.getError(MainErrorType.MISSING_SESSION, context.getLocale(), context.getMethod(), context.getVersion(),
				                           SystemParameterNames.getSessionId());
			} else {
				if (!isValidSession(context)) {
					return MainErrors.getError(MainErrorType.INVALID_SESSION, context.getLocale(), context.getMethod(), context.getVersion(),
					                           context.getSessionId());
				}
			}
		}
		return null;
	}

	/**
	 * 检查签名的有效性
	 * 
	 * @param context
	 * @return
	 */
	private MainError checkSign(RopRequestContext context) {
		// 特殊处理 针对工具类API 不进行签名
		String methodName = context.getMethod();
		String version = context.getVersion();
		// 通过methodName version获取其参数的详细信息
		ApiInfo apiInfo = (ApiInfo) ApiServiceAccessController.getApiMap().get(methodName + Constants.JOIN_SIGN + version);
		if (apiInfo == null) {
			return MainErrors.getError(MainErrorType.INVALID_METHOD, context.getLocale(), context.getMethod());
		}
		if (!Constants.API_SORT_CODE_4.equals(apiInfo.getApiSort())) {
			// 系统级签名开启,且服务方法需求签名
			if (context.isSignEnable()) {
				if (context.getSign() == null) {
					return MainErrors.getError(MainErrorType.MISSING_SIGNATURE, context.getLocale(), context.getMethod(), context.getVersion(),
					                           SystemParameterNames.getSign());
				} else {

					// 获取需要签名的参数
					List<String> ignoreSignFieldNames = new ArrayList<String>();
					// 排除sign
					ignoreSignFieldNames.add("sign");
					HashMap<String, String> needSignParams = new HashMap<String, String>();
					for (Map.Entry<String, String> entry : context.getAllParams().entrySet()) {
						if (!ignoreSignFieldNames.contains(entry.getKey())) {
							needSignParams.put(entry.getKey(), entry.getValue());
						}
					}

					// 查看密钥是否存在，不存在则说明appKey是非法的
					String signSecret = getAppSecretManager().getSecret(context.getAppKey());
					if (signSecret == null) {
						throw new RopException("无法获取" + context.getAppKey() + "对应的密钥");
					}

					String signValue = RopUtils.sign(needSignParams, signSecret);
					if (!signValue.equals(context.getSign())) {
						if (log.isErrorEnabled()) {
							log.error(context.getAppKey() + "的签名不合法，请检查");
						}
						return MainErrors.getError(MainErrorType.INVALID_SIGNATURE, context.getLocale(), context.getMethod(),
						                           context.getVersion());
					} else {
						return null;
					}
				}
			} else {
				if (log.isDebugEnabled()) {
					log.warn("{}{}服务方法未开启签名", context.getMethod(), context.getVersion());
				}
				return null;
			}
		} else {
			return null;
		}

	}

	private MainError checkUploadFile(RopRequestContext rrctx) {
		ServiceMethodHandler serviceMethodHandler = rrctx.getServiceMethodHandler();
		if (serviceMethodHandler != null && serviceMethodHandler.hasUploadFiles()) {
			List<String> fileFieldNames = serviceMethodHandler.getUploadFileFieldNames();
			for (String fileFieldName : fileFieldNames) {
				String paramValue = rrctx.getParamValue(fileFieldName);
				if (paramValue != null) {
					if (paramValue.indexOf("@") < 0) {
						return MainErrors.getError(MainErrorType.UPLOAD_FAIL, rrctx.getLocale(), rrctx.getMethod(), rrctx.getVersion(),
						                           "MESSAGE_VALID:not contain '@'.");
					} else {
						String fileType = UploadFileUtils.getFileType(paramValue);
						if (!fileUploadController.isAllowFileType(fileType)) {
							return MainErrors
							        .getError(MainErrorType.UPLOAD_FAIL, rrctx.getLocale(), rrctx.getMethod(), rrctx.getVersion(),
							                  "FILE_TYPE_NOT_ALLOW:the valid file types is:" + fileUploadController.getAllowFileTypes());
						}
						byte[] fileContent = UploadFileUtils.decode(paramValue);
						if (fileUploadController.isExceedMaxSize(fileContent.length)) {
							return MainErrors.getError(MainErrorType.UPLOAD_FAIL, rrctx.getLocale(), rrctx.getMethod(), rrctx.getVersion(),
							                           "EXCEED_MAX_SIZE:" + fileUploadController.getMaxSize() + "k");
						}
					}
				}
			}
		}
		return null;
	}

	public AppSecretManager getAppSecretManager() {
		return appSecretManager;
	}

	/**
	 * 生成对应子错误的错误类
	 * 
	 * @param allErrors
	 * @param locale
	 * @param subErrorType
	 * @return
	 */
	@SuppressWarnings("unused")
	private MainError getBusinessParameterMainError(List<ObjectError> allErrors, Locale locale, SubErrorType subErrorType,
	        RopRequestContext context) {
		MainError mainError = SubErrors.getMainError(subErrorType, locale, context.getMethod(), context.getVersion());
		for (ObjectError objectError : allErrors) {
			if (objectError instanceof FieldError) {
				FieldError fieldError = (FieldError) objectError;
				SubErrorType tempSubErrorType = INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.get(fieldError.getCode());
				if (tempSubErrorType == subErrorType) {
					String subErrorCode = SubErrors.getSubErrorCode(tempSubErrorType, fieldError.getField(), fieldError.getRejectedValue());
					SubError subError = SubErrors.getSubError(subErrorCode, tempSubErrorType.value(), locale, fieldError.getField(),
					                                          fieldError.getRejectedValue());
					mainError.addSubError(subError);
				}
			}
		}
		return mainError;
	}

	public ServiceAccessController getServiceAccessController() {
		return serviceAccessController;
	}

	/**
	 * 判断错误列表中是否包括指定的子错误
	 * 
	 * @param allErrors
	 * @param subErrorType1
	 * @return
	 */
	@SuppressWarnings("unused")
	private boolean hastSubErrorType(List<ObjectError> allErrors, SubErrorType subErrorType1) {
		for (ObjectError objectError : allErrors) {
			if (objectError instanceof FieldError) {
				FieldError fieldError = (FieldError) objectError;
				if (INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.containsKey(fieldError.getCode())) {
					SubErrorType tempSubErrorType = INVALIDE_CONSTRAINT_SUBERROR_MAPPINGS.get(fieldError.getCode());
					if (tempSubErrorType == subErrorType1) {
						return true;
					}
				}
			}
		}
		return false;
	}

	private boolean isValidSession(RopRequestContext smc) {
		if (sessionManager.getSession(smc.getSessionId()) == null) {
			if (log.isDebugEnabled()) {
				log.debug(smc.getSessionId() + "会话不存在，请检查。");
			}
			return false;
		} else {
			return true;
		}
	}

	@Override
	public void setAppSecretManager(AppSecretManager appSecretManager) {
		this.appSecretManager = appSecretManager;
	}

	@Override
	public void setFileUploadController(FileUploadController fileUploadController) {
		this.fileUploadController = fileUploadController;
	}

	@Override
	public void setInvokeTimesController(InvokeTimesController invokeTimesController) {
		this.invokeTimesController = invokeTimesController;
	}

	@Override
	public void setServiceAccessController(ServiceAccessController serviceAccessController) {
		this.serviceAccessController = serviceAccessController;
	}

	@Override
	public void setSessionManager(SessionManager sessionManager) {
		this.sessionManager = sessionManager;
	}

	private MainError validateBusinessParams(RopRequestContext context) {
		String methodName = context.getMethod();
		String version = context.getVersion();
		Map<String, String> paramMap = context.getAllParams();

		// 特殊处理 针对format 为xlsx 需要校验excelHeader字段
		if (context.getFormat().equals(MessageFormat.xls.name())) {
			String excelHeader = paramMap.get(Constants.EXCEL_HEADER);
			if (excelHeader == null) {
				return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), methodName, context.getVersion(),
				                           Constants.EXCEL_HEADER);
			} else {
				try {
					FastJSONUtil.deserialize(excelHeader, FormDetail.class);
				}
				catch (Exception e) {
					return MainErrors.getError(MainErrorType.INVALID_ARGUMENTS, context.getLocale(), methodName, Constants.EXCEL_HEADER);
				}
			}
		}

		// 通过methodName version获取其参数的详细信息
		ApiInfo apiInfo = (ApiInfo) ApiServiceAccessController.getApiMap().get(methodName + Constants.JOIN_SIGN + version);
		// 校验reqType是否有权限访问数据
		String visitMethod = apiInfo.getApiVisitMethd();
		Boolean passFlag = true;
		if (StringUtils.isNotBlank(visitMethod)) {
			if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
				if (visitMethod.indexOf(Constants.REQ_TYPE_0 + "") == -1) {
					passFlag = false;
				}
			} else {
				if (visitMethod.indexOf(Constants.REQ_TYPE_1 + "") == -1) {
					passFlag = false;
				}
			}
		}
		if (!passFlag) {
			return MainErrors.getError(MainErrorType.EXCEED_REQTYPE_LIMIT, context.getLocale(), context.getMethod(), context.getVersion(),
			                           context.getReqType());
		}

		List<StructApiArg> apiArgList = apiInfo.getApiArgList();

		// 把必选其一的放入到map中
		MultiValueMap<Integer, StructApiArg> groupFieldMap = new LinkedMultiValueMap<Integer, StructApiArg>();
		// 把响应参数放入到list中
		List<String> resposeArgList = new ArrayList<String>();
		Integer mustType = 0;
		Integer mustOneGrpId = 0;

		for (StructApiArg apiArg : apiArgList) {
			if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
				mustType = apiArg.getSyncMustType();
				mustOneGrpId = apiArg.getSyncMustOneGrpId();
			} else {
				mustType = apiArg.getAsynMustType();
				mustOneGrpId = apiArg.getAsynMustOneGrpId();
			}
			if (Constants.PROP_NECESSARY_1.equals(mustType) && mustOneGrpId != null) {
				groupFieldMap.add(mustOneGrpId, apiArg);
			}
			if (Constants.YES_FLAG.equals(apiArg.getRespnArgId())) {
				resposeArgList.add(apiArg.getFieldName());
			}
		}

		// 取出所有计算法则
		Map<Integer, CalcPrincCode> calcPrincMap = AccessCacheFactory.getCalcPrincMap();
		ScriptEngine jse = new ScriptEngineManager().getEngineByName("JavaScript");

		// 判断业务级请求参数是否缺失
		for (StructApiArg apiArg : apiArgList) {
			if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
				mustType = apiArg.getSyncMustType();
				mustOneGrpId = apiArg.getSyncMustOneGrpId();
			} else {
				mustType = apiArg.getAsynMustType();
				mustOneGrpId = apiArg.getAsynMustOneGrpId();
			}
			// 周期字段校验
			if (Constants.YES_FLAG.equals(apiArg.getReqArgId()) && Constants.PROP_NECESSARY_3.equals(mustType)) {
				String resultVal = paramMap.get(apiArg.getFieldName());
				if (resultVal == null) {
					return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), methodName, context.getVersion(),
					                           apiArg.getFieldName());
				} else {
					// 同步周期字段有且只一个字 异步可以有多个
					String[] result = resultVal.split(Constants.SPLIT_SIGN);
					if (result.length > 1 && context.getReqType().equals(RequestTypeFormat.syn.name())) {
						return MainErrors.getError(MainErrorType.INVALID_ARGUMENTS, context.getLocale(), methodName, apiArg.getFieldName());
					}
				}
			}
			// 必选校验
			if (Constants.YES_FLAG.equals(apiArg.getReqArgId()) && Constants.PROP_NECESSARY_0.equals(mustType)) {
				String resultVal = paramMap.get(apiArg.getFieldName());
				if (resultVal == null) {
					return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), methodName, context.getVersion(),
					                           apiArg.getFieldName());
				} else {
					// 计算法则和取值范围为空则不进行校验
					if (apiArg.getCalcPrincId() != null && StringUtils.isNotEmpty(apiArg.getValueRange())) {
						// 校验值是否在取值范围
						MainError mainError = validateIsClude(apiArg, resultVal, calcPrincMap, jse, context);
						if (mainError != null) {
							return mainError;
						}
					}
				}
			}

		}
		// 必选其一校验
		for (Integer key : groupFieldMap.keySet()) {
			List<StructApiArg> mustOneList = groupFieldMap.get(key);
			boolean isInclude = true;
			String fieldNames = "";
			for (StructApiArg apiArg : mustOneList) {
				if (paramMap.get(apiArg.getFieldName()) != null) {
					isInclude = isInclude || true;
					break;
				} else {
					isInclude = false;
				}
				fieldNames += apiArg.getFieldName() + " ";

			}
			if (!isInclude) {
				return MainErrors.getError(MainErrorType.MISSING_SELECT_MUSTONE, context.getLocale(), methodName, context.getVersion(),
				                           fieldNames);
			}
			for (StructApiArg apiArg : mustOneList) {
				String resultVal = paramMap.get(apiArg.getFieldName());
				if (resultVal != null) {
					// 计算法则和取值范围为空则不进行校验
					if (apiArg.getCalcPrincId() != null && StringUtils.isNotEmpty(apiArg.getValueRange())) {
						// 校验值是否在取值范围
						MainError mainError = validateIsClude(apiArg, resultVal, calcPrincMap, jse, context);
						if (mainError != null) {
							return mainError;
						}
					}
				}
			}
		}

		// 检查fields
		if (context.getFields() == null) {
			return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), context.getMethod(), context.getVersion(),
			                           SystemParameterNames.getFields());
		} else {
			List<String> filedsList = Arrays.asList(context.getFields().split(Constants.SPLIT_SIGN));
			Boolean isPass = true;
			for (String validParam : filedsList) {
				if (!resposeArgList.contains(validParam)) {
					log.debug("validParam is not pass:" + validParam);
					isPass = false;
					break;
				}
			}
			if (!isPass) {
				return MainErrors.getError(MainErrorType.INVALID_REPPARAM_VALUE_FORMAT, context.getLocale(), context.getMethod(),
				                           context.getVersion(), SystemParameterNames.getFields());
			}
		}

		// 同步 pageSize与 pageNumber如果都为空不校验 不为空校验
		if (context.getReqType().equals(RequestTypeFormat.syn.name())) {
			boolean pageSIsNull = StringUtils.isBlank(paramMap.get(Constants.PAGE_SIZE));
			boolean pageNIsNull = StringUtils.isBlank(paramMap.get(Constants.PAGE_NUM));
			if (pageSIsNull && !pageNIsNull) {
				return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), context.getMethod(), context.getVersion(),
				                           Constants.PAGE_SIZE);
			}

			if (!pageSIsNull && pageNIsNull) {
				return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), context.getMethod(), context.getVersion(),
				                           Constants.PAGE_NUM);
			}

			if (!pageSIsNull && !pageNIsNull) {
				try {
					int pageSize = Integer.parseInt(paramMap.get(Constants.PAGE_SIZE));
					if (pageSize > Constants.DEFAULT_PAGE_SIZE) {
						return MainErrors.getError(MainErrorType.INVALID_BEYOND_LIMIT, context.getLocale(), context.getMethod(),
						                           context.getVersion(), Constants.PAGE_SIZE);
					}
				}
				catch (NumberFormatException e) {
					return MainErrors.getError(MainErrorType.INVALID_ARGUMENTS, context.getLocale(), context.getMethod(), context.getVersion());
				}
			}
		}

		return null;
	}

	private boolean validateValueNum(String[] result, String numType) {
		// 单、双目都传一个值 多目的不校验
		boolean validFlag = false;
		if (!Constants.SUBHD_TYPE_N.equals(numType)) {
			if (result.length == 1) {
				validFlag = true;
			}
		} else {
			validFlag = true;
		}

		return validFlag;
	}

	private MainError validateIsClude(StructApiArg apiArg, String resultVal, Map<Integer, CalcPrincCode> calcPrincMap, ScriptEngine jse,
	        RopRequestContext context) {
		MainError mainError = null;
		Integer calcId = apiArg.getCalcPrincId();
		String valueRange = apiArg.getValueRange();
		// 法则ID和取值范围都不为空才进行校验
		if (calcId != null && StringUtils.isNotBlank(valueRange)) {

			CalcPrincCode calCode = calcPrincMap.get(calcId);
			String subhdTye = calCode.getSubhdType();
			String[] resultValArr = resultVal.toUpperCase().split(Constants.SPLIT_SIGN);
			// 如果传过来的值与法则类型不符合 则通不过
			if (validateValueNum(resultValArr, subhdTye)) {
				StringBuffer tempExe = new StringBuffer();
				tempExe.append("var x=" + resultVal + ", ");
				String[] valueRangeArr = valueRange.toUpperCase().split(Constants.SPLIT_SIGN);
				// 单目,双目
				if (!Constants.SUBHD_TYPE_N.equals(subhdTye)) {
					// 单目里针对Like的不做校验
					if (!calCode.getCalcFormula().contains("LIKE")) {
						if (Constants.SUBHD_TYPE_1.equals(subhdTye)) {
							tempExe.append("a=" + valueRangeArr[0] + "; ");

						} else if (Constants.SUBHD_TYPE_2.equals(subhdTye)) {
							tempExe.append("a=" + valueRangeArr[0] + ", ");
							tempExe.append("b=" + valueRangeArr[1] + "; ");
						}
						tempExe.append(calCode.getCalcFormula().replace("AND", " && "));
						try {
							if (!(Boolean) jse.eval(tempExe.toString())) {
								mainError = MainErrors.getError(MainErrorType.INVALID_REQPARAM_VALUE_FORMAT, context.getLocale(),
								                                context.getMethod(), context.getVersion(), apiArg.getFieldName());
							}
						}
						catch (Exception ex) {
							ex.getMessage();
						}
					}

				} else {
					// 多目
					List<String> vauleRageList = new ArrayList<String>();
					Collections.addAll(vauleRageList, valueRangeArr);
					List<String> resutlValList = Arrays.asList(resultValArr);
					// 取交集
					vauleRageList.retainAll(resutlValList);
					if (vauleRageList.size() == 0) {
						mainError = MainErrors.getError(MainErrorType.INVALID_REQPARAM_VALUE_FORMAT, context.getLocale(), context.getMethod(),
						                                context.getVersion(), apiArg.getFieldName());
					}

				}

			}

		}
		return mainError;
	}

	/**
	 * 校验返回的字符集是否合法
	 * 
	 * @param ropRequestContext
	 * @return
	 * @date 2016年4月5日 上午10:59:43
	 * @author houbl
	 */
	private MainError validateCodeType(RopRequestContext context) {
		MainError mainError = null;
		String codeType = context.getCodeType();
		boolean validFlag = false;
		if (Constants.UTF8.equalsIgnoreCase(codeType) || Constants.GBK.equalsIgnoreCase(codeType)) {
			validFlag = true;
		}
		if (!validFlag) {
			mainError = MainErrors.getError(MainErrorType.INVALID_CODETYPE_FORMAT, context.getLocale(), context.getMethod(),
			                                context.getVersion(), SystemParameterNames.getCodeType());
		}
		return mainError;
	}

	@Override
	public MainError validateOther(RopRequestContext rrctx) {

		MainError mainError = null;

		// 1.判断应用/用户是否有权访问目标服务
		mainError = checkServiceAccessAllow(rrctx);
		if (mainError != null) {
			return mainError;
		}

		// 2.判断应用/会话/用户访问服务的次数或频度是否超限
		mainError = checkInvokeTimesLimit(rrctx);
		if (mainError != null) {
			return mainError;
		}

		// 3.如果是上传文件的服务，检查文件类型和大小是否满足要求
		mainError = checkUploadFile(rrctx);
		if (mainError != null) {
			return mainError;
		}

		// 4.检查业务参数合法性
		long startTime = System.currentTimeMillis();
		mainError = validateBusinessParams(rrctx);
		long endTime = System.currentTimeMillis();
		log.info("检查业务参数合法性时长：{}", endTime - startTime);

		if (mainError != null) {
			return mainError;
		}
		return null;
	}

	@Override
	public MainError validateSystemParameters(RopRequestContext context) {
		RopContext ropContext = context.getRopContext();
		MainError mainError = null;

		// 1.检查appKey
		if (context.getAppKey() == null) {
			return MainErrors.getError(MainErrorType.MISSING_APP_KEY, context.getLocale(), context.getMethod(), context.getVersion(),
			                           SystemParameterNames.getAppKey());
		}
		if (!appSecretManager.isValidAppKey(context.getAppKey())) {
			return MainErrors.getError(MainErrorType.INVALID_APP_KEY, context.getLocale(), context.getMethod(), context.getVersion(),
			                           context.getAppKey());
		}

		// 2.检查会话
		mainError = checkSession(context);
		if (mainError != null) {
			return mainError;
		}

		// 3.检查method参数
		if (context.getMethod() == null) {
			return MainErrors.getError(MainErrorType.MISSING_METHOD, context.getLocale(), SystemParameterNames.getMethod());
		} else {
			if (!context.isSysAppKey()) {
				if (!ropContext.isValidMethod(context.getMethod())) {
					return MainErrors.getError(MainErrorType.INVALID_METHOD, context.getLocale(), context.getMethod());
				}
			}

		}

		// 4.检查v参数
		if (context.getVersion() == null) {
			return MainErrors.getError(MainErrorType.MISSING_VERSION, context.getLocale(), context.getMethod(),
			                           SystemParameterNames.getVersion());
		} else {
			if (!context.isSysAppKey()) {
				if (!ropContext.isValidVersion(context.getMethod(), context.getVersion())) {
					return MainErrors
					        .getError(MainErrorType.UNSUPPORTED_VERSION, context.getLocale(), context.getMethod(), context.getVersion());
				}
			}
		}

		// 5.检查签名正确性
		mainError = checkSign(context);
		if (mainError != null) {
			return mainError;
		}

		// 6.检查 format
		if (!MessageFormat.isValidFormat(context.getFormat())) {
			return MainErrors.getError(MainErrorType.INVALID_FORMAT, context.getLocale(), context.getMethod(), context.getVersion(),
			                           context.getFormat());
		}

		// 7.检查reqType
		if (context.getReqType() == null) {
			return MainErrors.getError(MainErrorType.MISSING_REQPARAM, context.getLocale(), context.getMethod(), context.getVersion(),
			                           SystemParameterNames.getReqType());
		} else {
			if (!RequestTypeFormat.isValidFormat(context.getReqType())) {
				return MainErrors.getError(MainErrorType.INVALID_REQTYPE_FORMAT, context.getLocale(), context.getMethod(), context.getVersion(),
				                           SystemParameterNames.getReqType());
			}
		}

		// 8.检查codeType
		mainError = validateCodeType(context);
		if (mainError != null) {
			return mainError;
		}

		return null;
	}

}
