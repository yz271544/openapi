package com.teradata.portal.web.sandbox.pojo;

import java.io.Serializable;
import java.util.List;

import org.springframework.util.MultiValueMap;

public class DynamicApiArg implements Serializable {

	/** serialVersionUID */
	private static final long serialVersionUID = 5130872873105534119L;

	private Integer apiId;

	private Integer apiVersion;

	private String fields;

	private List<StructApiArg> responseArgList;// 响应参数列表

	private List<StructApiArg> requiredArgList;// 必选

	private MultiValueMap<String, StructApiArg> chooseOneArgMap;// 必选其一 key值是group分类

	private List<String> chooseOneGroupList;// 必选其一 group值列表

	private List<StructApiArg> choosableArgList;// 可选

	public Integer getApiId() {
		return apiId;
	}

	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	public Integer getApiVersion() {
		return apiVersion;
	}

	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getFields() {
		return fields;
	}

	public void setFields(String fields) {
		this.fields = fields;
	}

	public List<StructApiArg> getResponseArgList() {
		return responseArgList;
	}

	public void setResponseArgList(List<StructApiArg> responseArgList) {
		this.responseArgList = responseArgList;
	}

	public List<StructApiArg> getRequiredArgList() {
		return requiredArgList;
	}

	public void setRequiredArgList(List<StructApiArg> requiredArgList) {
		this.requiredArgList = requiredArgList;
	}

	public List<StructApiArg> getChoosableArgList() {
		return choosableArgList;
	}

	public MultiValueMap<String, StructApiArg> getChooseOneArgMap() {
		return chooseOneArgMap;
	}

	public void setChooseOneArgMap(MultiValueMap<String, StructApiArg> chooseOneArgMap) {
		this.chooseOneArgMap = chooseOneArgMap;
	}

	public void setChoosableArgList(List<StructApiArg> choosableArgList) {
		this.choosableArgList = choosableArgList;
	}

	public List<String> getChooseOneGroupList() {
		return chooseOneGroupList;
	}

	public void setChooseOneGroupList(List<String> chooseOneGroupList) {
		this.chooseOneGroupList = chooseOneGroupList;
	}

}
