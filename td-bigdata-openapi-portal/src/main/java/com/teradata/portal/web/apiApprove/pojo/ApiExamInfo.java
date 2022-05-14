package com.teradata.portal.web.apiApprove.pojo;

import java.util.Date;

public class ApiExamInfo {
    private Integer auditId;

    private Integer apiId;

    private Integer apiVersion;

    private String testSndboxIds;

    private String auditAdvc;

    private Integer auditStat;

    private Date auditTime;

    private String auditPersn;

    private Integer isntEff;

    public Integer getAuditId() {
        return auditId;
    }

    public void setAuditId(Integer auditId) {
        this.auditId = auditId;
    }

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

    public String getTestSndboxIds() {
        return testSndboxIds;
    }

    public void setTestSndboxIds(String testSndboxIds) {
        this.testSndboxIds = testSndboxIds == null ? null : testSndboxIds.trim();
    }

    public String getAuditAdvc() {
        return auditAdvc;
    }

    public void setAuditAdvc(String auditAdvc) {
        this.auditAdvc = auditAdvc == null ? null : auditAdvc.trim();
    }

    public Integer getAuditStat() {
        return auditStat;
    }

    public void setAuditStat(Integer auditStat) {
        this.auditStat = auditStat;
    }

    public Date getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
    }

    public String getAuditPersn() {
        return auditPersn;
    }

    public void setAuditPersn(String auditPersn) {
        this.auditPersn = auditPersn == null ? null : auditPersn.trim();
    }

    public Integer getIsntEff() {
        return isntEff;
    }

    public void setIsntEff(Integer isntEff) {
        this.isntEff = isntEff;
    }
}