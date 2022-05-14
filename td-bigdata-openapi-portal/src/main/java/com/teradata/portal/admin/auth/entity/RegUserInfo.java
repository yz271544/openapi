package com.teradata.portal.admin.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/*import com.teradata.openapi.framework.model.ApiInfoRow;
import com.teradata.openapi.framework.model.SourceInfoRow;
import com.teradata.portal.admin.auth.util.CustomDateDeserialize;
import org.codehaus.jackson.map.annotate.JsonDeserialize;*/

/**
 * 用户信息
 * Created by Evan on 2016/7/3.
 */
@SuppressWarnings("serial")
public class RegUserInfo extends Page implements java.io.Serializable {

    private int userId; //用户ID

    private String loginAcct; //登录账号

    private String userName; //用户姓名

    private String phone; //电话

    private String registPersn; //注册人

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registTime; //注册时间

    private int userTypeCode; //用户类型 0 api开发者   1 api调用者  2平台维护者

    private String userTypeDesc;

    private int userStatCode; //用户状态 0 表示待审核  1表示生效  2表示驳回 3表示失效

    private String userStatDesc;

    private String auditAdvc;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLoginAcct() {
        return loginAcct;
    }

    public void setLoginAcct(String loginAcct) {
        this.loginAcct = loginAcct;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getRegistPersn() {
        return registPersn;
    }

    public void setRegistPersn(String registPersn) {
        this.registPersn = registPersn;
    }

    public Date getRegistTime() {
        return registTime;
    }

    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public int getUserTypeCode() {
        return userTypeCode;
    }

    public void setUserTypeCode(int userTypeCode) {
        this.userTypeCode = userTypeCode;
    }

    public String getUserTypeDesc() {
        return userTypeDesc;
    }

    public void setUserTypeDesc(String userTypeDesc) {
        this.userTypeDesc = userTypeDesc;
    }

    public int getUserStatCode() {
        return userStatCode;
    }

    public void setUserStatCode(int userStatCode) {
        this.userStatCode = userStatCode;
    }

    public String getUserStatDesc() {
        return userStatDesc;
    }

    public void setUserStatDesc(String userStatDesc) {
        this.userStatDesc = userStatDesc;
    }

    public String getAuditAdvc() {
        return auditAdvc;
    }

    public void setAuditAdvc(String auditAdvc) {
        this.auditAdvc = auditAdvc;
    }
}
