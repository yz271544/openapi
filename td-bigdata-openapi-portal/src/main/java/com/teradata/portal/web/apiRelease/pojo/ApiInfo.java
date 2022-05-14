package com.teradata.portal.web.apiRelease.pojo;

import java.sql.Timestamp;

public class ApiInfo {

	private String apiClassName;

	private Integer apiClsCode;

	private String apiClsDesc;

	private String apiDesc;

	private Integer apiId;

	private String apiName;

	private Integer apiSort;

	private String apiSortName;

	private Integer apiStatCode;

	private String apiStatDesc;

	private Integer apiVersion;

	private String apiVisitMethd;

	private String auditAdvc;

	private String dataCycleDesc;

	private Integer dataCycleType;

	private Integer dataStrctTypeCode;

	private Integer examStat;

	private String examStatDesc;

	private Integer newApiVersion; // api克隆时使用

	private String relsePersn;

	private Timestamp relseTime;

	private Integer relseType;

	private Integer tabScaleType;

	private Integer triggerMethd;

	private String triggerMethdDesc;

	/**
	 * 获取 apiClassName
	 * 
	 * @return apiClassName
	 */
	public String getApiClassName() {
		return apiClassName;
	}

	/**
	 * 获取 apiClsCode
	 * 
	 * @return apiClsCode
	 */
	public Integer getApiClsCode() {
		return apiClsCode;
	}

	/**
	 * 获取 apiClsDesc
	 * 
	 * @return apiClsDesc
	 */
	public String getApiClsDesc() {
		return apiClsDesc;
	}

	/**
	 * 获取 apiDesc
	 * 
	 * @return apiDesc
	 */
	public String getApiDesc() {
		return apiDesc;
	}

	/**
	 * 获取 apiId
	 * 
	 * @return apiId
	 */
	public Integer getApiId() {
		return apiId;
	}

	/**
	 * 获取 apiName
	 * 
	 * @return apiName
	 */
	public String getApiName() {
		return apiName;
	}

	/**
	 * 获取 apiSort
	 * 
	 * @return apiSort
	 */
	public Integer getApiSort() {
		return apiSort;
	}

	/**
	 * 获取 apiSortName
	 * 
	 * @return apiSortName
	 */
	public String getApiSortName() {
		return apiSortName;
	}

	/**
	 * 获取 apiStatCode
	 * 
	 * @return apiStatCode
	 */
	public Integer getApiStatCode() {
		return apiStatCode;
	}

	/**
	 * 获取 apiStatDesc
	 * 
	 * @return apiStatDesc
	 */
	public String getApiStatDesc() {
		return apiStatDesc;
	}

	/**
	 * 获取 apiVersion
	 * 
	 * @return apiVersion
	 */
	public Integer getApiVersion() {
		return apiVersion;
	}

	public String getApiVisitMethd() {
		return apiVisitMethd;
	}

	/**
	 * 获取 auditAdvc
	 * 
	 * @return auditAdvc
	 */
	public String getAuditAdvc() {
		return auditAdvc;
	}

	/**
	 * 获取 dataCycleDesc
	 * 
	 * @return dataCycleDesc
	 */
	public String getDataCycleDesc() {
		return dataCycleDesc;
	}

	/**
	 * 获取 dataCycleType
	 * 
	 * @return dataCycleType
	 */
	public Integer getDataCycleType() {
		return dataCycleType;
	}

	/**
	 * 获取 dataStrctTypeCode
	 * 
	 * @return dataStrctTypeCode
	 */
	public Integer getDataStrctTypeCode() {
		return dataStrctTypeCode;
	}

	/**
	 * 获取 examStat
	 * 
	 * @return examStat
	 */
	public Integer getExamStat() {
		return examStat;
	}

	/**
	 * 获取 examStatDesc
	 * 
	 * @return examStatDesc
	 */
	public String getExamStatDesc() {
		return examStatDesc;
	}

	/**
	 * 获取 newApiVersion
	 * 
	 * @return newApiVersion
	 */
	public Integer getNewApiVersion() {
		return newApiVersion;
	}

	/**
	 * 获取 relsePersn
	 * 
	 * @return relsePersn
	 */
	public String getRelsePersn() {
		return relsePersn;
	}

	/**
	 * 获取 relseTime
	 * 
	 * @return relseTime
	 */
	public String getRelseTime() {
		return relseTime.toString().substring(0, 19);
	}

	/**
	 * 获取 relseType
	 * 
	 * @return relseType
	 */
	public Integer getRelseType() {
		return relseType;
	}

	/**
	 * 获取 tabScaleType
	 * 
	 * @return tabScaleType
	 */
	public Integer getTabScaleType() {
		return tabScaleType;
	}

	/**
	 * 获取 triggerMethd
	 * 
	 * @return triggerMethd
	 */
	public Integer getTriggerMethd() {
		return triggerMethd;
	}

	/**
	 * 获取 triggerMethdDesc
	 * 
	 * @return triggerMethdDesc
	 */
	public String getTriggerMethdDesc() {
		return triggerMethdDesc;
	}

	/**
	 * 设置 apiClassName
	 * 
	 * @param apiClassName
	 *            apiClassName
	 */
	public void setApiClassName(String apiClassName) {
		this.apiClassName = apiClassName;
	}

	/**
	 * 设置 apiClsCode
	 * 
	 * @param apiClsCode
	 *            apiClsCode
	 */
	public void setApiClsCode(Integer apiClsCode) {
		this.apiClsCode = apiClsCode;
	}

	/**
	 * 设置 apiClsDesc
	 * 
	 * @param apiClsDesc
	 *            apiClsDesc
	 */
	public void setApiClsDesc(String apiClsDesc) {
		this.apiClsDesc = apiClsDesc;
	}

	/**
	 * 设置 apiDesc
	 * 
	 * @param apiDesc
	 *            apiDesc
	 */
	public void setApiDesc(String apiDesc) {
		this.apiDesc = apiDesc;
	}

	/**
	 * 设置 apiId
	 * 
	 * @param apiId
	 *            apiId
	 */
	public void setApiId(Integer apiId) {
		this.apiId = apiId;
	}

	/**
	 * 设置 apiName
	 * 
	 * @param apiName
	 *            apiName
	 */
	public void setApiName(String apiName) {
		this.apiName = apiName;
	}

	/**
	 * 设置 apiSort
	 * 
	 * @param apiSort
	 *            apiSort
	 */
	public void setApiSort(Integer apiSort) {
		this.apiSort = apiSort;
	}

	/**
	 * 设置 apiSortName
	 * 
	 * @param apiSortName
	 *            apiSortName
	 */
	public void setApiSortName(String apiSortName) {
		this.apiSortName = apiSortName;
	}

	/**
	 * 设置 apiStatCode
	 * 
	 * @param apiStatCode
	 *            apiStatCode
	 */
	public void setApiStatCode(Integer apiStatCode) {
		this.apiStatCode = apiStatCode;
	}

	/**
	 * 设置 apiStatDesc
	 * 
	 * @param apiStatDesc
	 *            apiStatDesc
	 */
	public void setApiStatDesc(String apiStatDesc) {
		this.apiStatDesc = apiStatDesc;
	}

	/**
	 * 设置 apiVersion
	 * 
	 * @param apiVersion
	 *            apiVersion
	 */
	public void setApiVersion(Integer apiVersion) {
		this.apiVersion = apiVersion;
	}

	public void setApiVisitMethd(String apiVisitMethd) {
		this.apiVisitMethd = apiVisitMethd;
	}

	/**
	 * 设置 auditAdvc
	 * 
	 * @param auditAdvc
	 *            auditAdvc
	 */
	public void setAuditAdvc(String auditAdvc) {
		this.auditAdvc = auditAdvc;
	}

	/**
	 * 设置 dataCycleDesc
	 * 
	 * @param dataCycleDesc
	 *            dataCycleDesc
	 */
	public void setDataCycleDesc(String dataCycleDesc) {
		this.dataCycleDesc = dataCycleDesc;
	}

	/**
	 * 设置 dataCycleType
	 * 
	 * @param dataCycleType
	 *            dataCycleType
	 */
	public void setDataCycleType(Integer dataCycleType) {
		this.dataCycleType = dataCycleType;
	}

	/**
	 * 设置 dataStrctTypeCode
	 * 
	 * @param dataStrctTypeCode
	 *            dataStrctTypeCode
	 */
	public void setDataStrctTypeCode(Integer dataStrctTypeCode) {
		this.dataStrctTypeCode = dataStrctTypeCode;
	}

	/**
	 * 设置 examStat
	 * 
	 * @param examStat
	 *            examStat
	 */
	public void setExamStat(Integer examStat) {
		this.examStat = examStat;
	}

	/**
	 * 设置 examStatDesc
	 * 
	 * @param examStatDesc
	 *            examStatDesc
	 */
	public void setExamStatDesc(String examStatDesc) {
		this.examStatDesc = examStatDesc;
	}

	/**
	 * 设置 newApiVersion
	 * 
	 * @param newApiVersion
	 *            newApiVersion
	 */
	public void setNewApiVersion(Integer newApiVersion) {
		this.newApiVersion = newApiVersion;
	}

	/**
	 * 设置 relsePersn
	 * 
	 * @param relsePersn
	 *            relsePersn
	 */
	public void setRelsePersn(String relsePersn) {
		this.relsePersn = relsePersn;
	}

	/**
	 * 设置 relseTime
	 * 
	 * @param relseTime
	 *            relseTime
	 */
	public void setRelseTime(Timestamp relseTime) {
		this.relseTime = relseTime;
	}

	/**
	 * 设置 relseType
	 * 
	 * @param relseType
	 *            relseType
	 */
	public void setRelseType(Integer relseType) {
		this.relseType = relseType;
	}

	/**
	 * 设置 tabScaleType
	 * 
	 * @param tabScaleType
	 *            tabScaleType
	 */
	public void setTabScaleType(Integer tabScaleType) {
		this.tabScaleType = tabScaleType;
	}

	/**
	 * 设置 triggerMethd
	 * 
	 * @param triggerMethd
	 *            triggerMethd
	 */
	public void setTriggerMethd(Integer triggerMethd) {
		this.triggerMethd = triggerMethd;
	}

	/**
	 * 设置 triggerMethdDesc
	 * 
	 * @param triggerMethdDesc
	 *            triggerMethdDesc
	 */
	public void setTriggerMethdDesc(String triggerMethdDesc) {
		this.triggerMethdDesc = triggerMethdDesc;
	}

}