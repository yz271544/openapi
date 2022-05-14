package com.teradata.portal.web.apiRelease.pojo;

public class StructApiArgKey {
    private Integer apiId;

    private Integer apiVersion;

    private String fieldAlias;

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

    public String getFieldAlias() {
        return fieldAlias;
    }

    public void setFieldAlias(String fieldAlias) {
        this.fieldAlias = fieldAlias == null ? null : fieldAlias.trim();
    }
}