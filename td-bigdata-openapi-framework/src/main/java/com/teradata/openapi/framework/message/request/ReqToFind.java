package com.teradata.openapi.framework.message.request;

import java.io.Serializable;
import java.util.List;

public class ReqToFind implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 5186861888806965837L;

	private String reqID;

	private Integer apiClsCode;

	private Integer apiId;

	private String appKey;

	private Integer apiSort;

	private Integer api_version;

	private String apiName;

	private Long timeStamp;

	private Format format;

	private String encode;

	private Integer priority;

	private List<ReqArg> reqArgs;

	private List<RepArg> repArgs;

	private String retFormatFinger;

	private String retDataFinger;

	private Integer isSyn;

	private Integer pageSize;

	private Integer pageNum;

	public String getReqID() {
		return reqID;
	}

	public void setReqID(String reqID) {
		this.reqID = reqID;
	}

	public Integer getApiClsCode() {
		return apiClsCode;
	}

	public void setApiClsCode(Integer apiClsCode) {
		this.apiClsCode = apiClsCode;
	}

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public String getAppKey() {
		return appKey;
	}

	public void setAppKey(String appKey) {
		this.appKey = appKey;
	}

	public Integer getApiSort() {
		return apiSort;
	}

	public void setApiSort(Integer apiSort) {
		this.apiSort = apiSort;
	}

	public Integer getApi_version() {
		return api_version;
	}

	public void setApi_version(Integer api_version) {
		this.api_version = api_version;
	}

	public String getApiName() {
		return apiName;
	}

	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	public Long getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(Long timeStamp) {
		this.timeStamp = timeStamp;
	}

	public Format getFormat() {
		return format;
	}

	public void setFormat(Format format) {
		this.format = format;
	}

	public String getEncode() {
		return encode;
	}

	public void setEncode(String encode) {
		this.encode = encode;
	}

	public Integer getPriority() {
		return priority;
	}

	public void setPriority(Integer priority) {
		this.priority = priority;
	}

	public List<ReqArg> getReqArgs() {
		return reqArgs;
	}

	public void setReqArgs(List<ReqArg> reqArgs) {
		this.reqArgs = reqArgs;
	}

	public List<RepArg> getRepArgs() {
		return repArgs;
	}

	public void setRepArgs(List<RepArg> repArgs) {
		this.repArgs = repArgs;
	}

	public String getRetFormatFinger() {
		return retFormatFinger;
	}

	public void setRetFormatFinger(String retFormatFinger) {
		this.retFormatFinger = retFormatFinger;
	}

	public String getRetDataFinger() {
		return retDataFinger;
	}

	public void setRetDataFinger(String retDataFinger) {
		this.retDataFinger = retDataFinger;
	}

	public Integer getIsSyn() {
		return isSyn;
	}

	public void setIsSyn(Integer isSyn) {
		this.isSyn = isSyn;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}

	public Integer getPageNum() {
		return pageNum;
	}

	public void setPageNum(Integer pageNum) {
		this.pageNum = pageNum;
	}

}
