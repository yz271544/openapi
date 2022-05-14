package com.teradata.portal.admin.auth.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
/*import com.teradata.openapi.framework.model.ApiInfoRow;
import com.teradata.openapi.framework.model.SourceInfoRow;
import com.teradata.portal.admin.auth.util.CustomDateDeserialize;
import org.codehaus.jackson.map.annotate.JsonDeserialize;*/

import java.security.Timestamp;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * 用户信息
 * Created by Evan on 2016/7/3.
 */
@SuppressWarnings("serial")
public class UserInfo extends Page implements java.io.Serializable {


    private int userId; //用户id

    private String loginAcct; //登录账号

    private String loginPwd; //密码

    private String userName; //用户姓名

    private String userStat; //用户状态 0 表示待审核  1表示生效  2表示驳回 3表示失效

    private int orgCode; //所属厂商组织机构代码

    private String email; //邮箱

    private String phone; //电话

    private int gender; //性别  0 男性  1 女性

    private String position; //职位

    private int userType; //用户类型 0 api开发者   1 api调用者  2平台维护者

    private String regionCode; //地市代码

    private String cityCode; //区县代码

    private String siteCode; //营业厅代码

    private String registPersn; //注册人

    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date registTime; //注册时间

    private int userGrade; //用户级别 1 表示等级1 2 表示等级2

    private int priority; //优先级 1 表示优先级最高，逐渐递减

    private String auditPersn; //审批人

    private String auditAdvc;  //审批意见

    /***=================================**/

    private String userGrpName;//用户所属用户组名称

    //private Set<ApiInfoRow> apiInfos = new HashSet<ApiInfoRow>(); //用户所可以访问的api

    //private Set<SourceInfoRow> sourceInfos = new HashSet<SourceInfoRow>();//用户所可以访问的数据源

    private Set<SysMenuInfo> sysMenuInfos = new HashSet<SysMenuInfo>();//用户所可以访问的菜单



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

    public String getLoginPwd() {
        return loginPwd;
    }

    public void setLoginPwd(String loginPwd) {
        this.loginPwd = loginPwd;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserStat() {
        return userStat;
    }

    public void setUserStat(String userStat) {
        this.userStat = userStat;
    }

    public int getOrgCode() {
        return orgCode;
    }

    public void setOrgCode(int orgCode) {
        this.orgCode = orgCode;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }

    public String getRegionCode() {
        return regionCode;
    }

    public void setRegionCode(String regionCode) {
        this.regionCode = regionCode;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }

    public String getSiteCode() {
        return siteCode;
    }

    public void setSiteCode(String siteCode) {
        this.siteCode = siteCode;
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

    //@JsonDeserialize(using = CustomDateDeserialize.class)
    public void setRegistTime(Date registTime) {
        this.registTime = registTime;
    }

    public int getUserGrade() {
        return userGrade;
    }

    public void setUserGrade(int userGrade) {
        this.userGrade = userGrade;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public String getUserGrpName() {
        return userGrpName;
    }

    public void setUserGrpName(String userGrpName) {
        this.userGrpName = userGrpName;
    }

    public String getAuditPersn() {
        return auditPersn;
    }

    public void setAuditPersn(String auditPersn) {
        this.auditPersn = auditPersn;
    }

    public String getAuditAdvc() {
        return auditAdvc;
    }

    public void setAuditAdvc(String auditAdvc) {
        this.auditAdvc = auditAdvc;
    }
/*public Set<ApiInfoRow> getApiInfos() {
        return apiInfos;
    }

    public void setApiInfos(Set<ApiInfoRow> apiInfos) {
        this.apiInfos = apiInfos;
    }

    public Set<SourceInfoRow> getSourceInfos() {
        return sourceInfos;
    }

    public void setSourceInfos(Set<SourceInfoRow> sourceInfos) {
        this.sourceInfos = sourceInfos;
    }*/

    public Set<SysMenuInfo> getSysMenuInfos() {
        return sysMenuInfos;
    }

    public void setSysMenuInfos(Set<SysMenuInfo> sysMenuInfos) {
        this.sysMenuInfos = sysMenuInfos;
    }

    /*@Override
    public String toString() {
        return "UserInfo{" +
                "userId=" + userId +
                ", loginAcct='" + loginAcct + '\'' +
                ", loginPwd='" + loginPwd + '\'' +
                ", userName='" + userName + '\'' +
                ", userStat='" + userStat + '\'' +
                ", orgCode=" + orgCode +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", gender=" + gender +
                ", position='" + position + '\'' +
                ", userType=" + userType +
                ", regionCode='" + regionCode + '\'' +
                ", cityCode='" + cityCode + '\'' +
                ", siteCode='" + siteCode + '\'' +
                ", registPersn='" + registPersn + '\'' +
                ", registTime=" + registTime +
                ", userGrade=" + userGrade +
                ", priority=" + priority +
                ", userGrpName='" + userGrpName + '\'' +
                ", apiInfos=" + apiInfos +
                ", sourceInfos=" + sourceInfos +
                ", sysMenuInfos=" + sysMenuInfos +
                '}';
    }*/
}
