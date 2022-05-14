package com.teradata.portal.web.apiRelease.pojo;

public class dataTargtTypeCode {
    private String fieldTargtType;

    private Integer fieldTargtTypeLen;

    private String fieldTargtTypeDesc;

    public String getFieldTargtType() {
        return fieldTargtType;
    }

    public void setFieldTargtType(String fieldTargtType) {
        this.fieldTargtType = fieldTargtType == null ? null : fieldTargtType.trim();
    }

    public Integer getFieldTargtTypeLen() {
        return fieldTargtTypeLen;
    }

    public void setFieldTargtTypeLen(Integer fieldTargtTypeLen) {
        this.fieldTargtTypeLen = fieldTargtTypeLen;
    }

    public String getFieldTargtTypeDesc() {
        return fieldTargtTypeDesc;
    }

    public void setFieldTargtTypeDesc(String fieldTargtTypeDesc) {
        this.fieldTargtTypeDesc = fieldTargtTypeDesc == null ? null : fieldTargtTypeDesc.trim();
    }
}