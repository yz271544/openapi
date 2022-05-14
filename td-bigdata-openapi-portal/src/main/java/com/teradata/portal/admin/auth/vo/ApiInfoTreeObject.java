package com.teradata.portal.admin.auth.vo;

import com.teradata.portal.admin.auth.util.TreeObject;

import java.sql.Timestamp;

/**
 * Created by Evan on 2016/7/22.
 */
public class ApiInfoTreeObject extends TreeObject {


    private Integer apiVersion;
    private Integer apiClsCode;
    private Integer apiStatCode;

    private String parentName;//对应api_sort_name
    private Integer dataStrctTypeCode;
    private Integer relseType;

    private String apiDesc;
    private Integer dataCycleType;
    private String relsePersn;
    private Timestamp relseTime;
    private Integer tabScaleType;
    private Integer examStat;
    private String apiClassName;
    private Integer triggerMethd;

    private Integer rightFieldValue; //权限领域值

    private String rightMain;

    public Integer getApiVersion() {
        return apiVersion;
    }

    public void setApiVersion(Integer apiVersion) {
        this.apiVersion = apiVersion;
    }

    public Integer getApiClsCode() {
        return apiClsCode;
    }

    public void setApiClsCode(Integer apiClsCode) {
        this.apiClsCode = apiClsCode;
    }

    public Integer getApiStatCode() {
        return apiStatCode;
    }

    public void setApiStatCode(Integer apiStatCode) {
        this.apiStatCode = apiStatCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Integer getDataStrctTypeCode() {
        return dataStrctTypeCode;
    }

    public void setDataStrctTypeCode(Integer dataStrctTypeCode) {
        this.dataStrctTypeCode = dataStrctTypeCode;
    }

    public Integer getRelseType() {
        return relseType;
    }

    public void setRelseType(Integer relseType) {
        this.relseType = relseType;
    }

    public String getApiDesc() {
        return apiDesc;
    }

    public void setApiDesc(String apiDesc) {
        this.apiDesc = apiDesc;
    }

    public Integer getDataCycleType() {
        return dataCycleType;
    }

    public void setDataCycleType(Integer dataCycleType) {
        this.dataCycleType = dataCycleType;
    }

    public String getRelsePersn() {
        return relsePersn;
    }

    public void setRelsePersn(String relsePersn) {
        this.relsePersn = relsePersn;
    }

    public Timestamp getRelseTime() {
        return relseTime;
    }

    public void setRelseTime(Timestamp relseTime) {
        this.relseTime = relseTime;
    }

    public Integer getTabScaleType() {
        return tabScaleType;
    }

    public void setTabScaleType(Integer tabScaleType) {
        this.tabScaleType = tabScaleType;
    }

    public Integer getExamStat() {
        return examStat;
    }

    public void setExamStat(Integer examStat) {
        this.examStat = examStat;
    }

    public String getApiClassName() {
        return apiClassName;
    }

    public void setApiClassName(String apiClassName) {
        this.apiClassName = apiClassName;
    }

    public Integer getTriggerMethd() {
        return triggerMethd;
    }

    public void setTriggerMethd(Integer triggerMethd) {
        this.triggerMethd = triggerMethd;
    }

    public Integer getRightFieldValue() {
        return rightFieldValue;
    }

    public void setRightFieldValue(Integer rightFieldValue) {
        this.rightFieldValue = rightFieldValue;
    }

    public String getRightMain() {
        return rightMain;
    }

    public void setRightMain(String rightMain) {
        this.rightMain = rightMain;
    }
}
