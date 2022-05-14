package com.teradata.portal.web.apiRelease.pojo;

public class SourceInfo {
    private Integer sourceId;

    private String sourceTypeCode;

    private Integer drvCode;

    private String sourceDesc;

    private String ipAddr;

    private Integer port;

    private String defltSchema;

    private String userName;

    private String pwd;

    private Integer priority;

    private Integer syncStrategyId;

    private Integer asynStrategyId;

    public Integer getSourceId() {
        return sourceId;
    }

    public void setSourceId(Integer sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceTypeCode() {
        return sourceTypeCode;
    }

    public void setSourceTypeCode(String sourceTypeCode) {
        this.sourceTypeCode = sourceTypeCode == null ? null : sourceTypeCode.trim();
    }

    public Integer getDrvCode() {
        return drvCode;
    }

    public void setDrvCode(Integer drvCode) {
        this.drvCode = drvCode;
    }

    public String getSourceDesc() {
        return sourceDesc;
    }

    public void setSourceDesc(String sourceDesc) {
        this.sourceDesc = sourceDesc == null ? null : sourceDesc.trim();
    }

    public String getIpAddr() {
        return ipAddr;
    }

    public void setIpAddr(String ipAddr) {
        this.ipAddr = ipAddr == null ? null : ipAddr.trim();
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public String getDefltSchema() {
        return defltSchema;
    }

    public void setDefltSchema(String defltSchema) {
        this.defltSchema = defltSchema == null ? null : defltSchema.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd == null ? null : pwd.trim();
    }

    public Integer getPriority() {
        return priority;
    }

    public void setPriority(Integer priority) {
        this.priority = priority;
    }

    public Integer getSyncStrategyId() {
        return syncStrategyId;
    }

    public void setSyncStrategyId(Integer syncStrategyId) {
        this.syncStrategyId = syncStrategyId;
    }

    public Integer getAsynStrategyId() {
        return asynStrategyId;
    }

    public void setAsynStrategyId(Integer asynStrategyId) {
        this.asynStrategyId = asynStrategyId;
    }
}