package com.teradata.portal.admin.right.entity;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * Created by Evan on 2016/9/5.
 */
public class RightApplyInfo implements Serializable{


    private static final long serialVersionUID = -5907495328961253984L;


    private Integer applyId;

    private Integer userId;

    private String loginAcct;

    private Long applyTime;

    private String applyTimestamp;

    private String auditPersn;

    private Long auditTime;

    private String auditTimestamp;

    private Integer auditStat;

    private String auditStatDesc;

    private String auditAdvc;

    public String getAuditStatDesc() {
        return auditStatDesc;
    }

    public String getApplyTimestamp() {
        return applyTimestamp;
    }

    public void setApplyTimestamp(String applyTimestamp) {
        this.applyTimestamp = applyTimestamp;
    }

    public String getAuditTimestamp() {
        return auditTimestamp;
    }

    public void setAuditTimestamp(String auditTimestamp) {
        this.auditTimestamp = auditTimestamp;
    }

    public void setAuditStatDesc(String auditStatDesc) {
        this.auditStatDesc = auditStatDesc;
    }

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLoginAcct() {
        return loginAcct;
    }

    public void setLoginAcct(String loginAcct) {
        this.loginAcct = loginAcct;
    }

    public Long getApplyTime() {
        return applyTime;
    }

    public void setApplyTime(Long applyTime) {
        this.applyTime = applyTime;
    }

    public String getAuditPersn() {
        return auditPersn;
    }

    public void setAuditPersn(String auditPersn) {
        this.auditPersn = auditPersn;
    }

    public Long getAuditTime() {
        return auditTime;
    }

    public void setAuditTime(Long auditTime) {
        this.auditTime = auditTime;
    }

    public Integer getAuditStat() {
        return auditStat;
    }

    public void setAuditStat(Integer auditStat) {
        this.auditStat = auditStat;
    }

    public String getAuditAdvc() {
        return auditAdvc;
    }

    public void setAuditAdvc(String auditAdvc) {
        this.auditAdvc = auditAdvc;
    }
}
