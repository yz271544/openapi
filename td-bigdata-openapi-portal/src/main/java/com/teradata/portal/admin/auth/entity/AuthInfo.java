package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;

/**
 * 权限信息
 * Created by Evan on 2016/7/4.
 */
@SuppressWarnings("serial")
public class AuthInfo implements Serializable{

    private int authId; //权限id

    private String authMain; //权限主体代码

    private int authMainValue;//权限主体值

    private String authDomain;//权限领域代码

    private int authDomainValue;//权限领域值

    public int getAuthId() {
        return authId;
    }

    public void setAuthId(int authId) {
        this.authId = authId;
    }

    public String getAuthMain() {
        return authMain;
    }

    public void setAuthMain(String authMain) {
        this.authMain = authMain;
    }

    public int getAuthMainValue() {
        return authMainValue;
    }

    public void setAuthMainValue(int authMainValue) {
        this.authMainValue = authMainValue;
    }

    public String getAuthDomain() {
        return authDomain;
    }

    public void setAuthDomain(String authDomain) {
        this.authDomain = authDomain;
    }

    public int getAuthDomainValue() {
        return authDomainValue;
    }

    public void setAuthDomainValue(int authDomainValue) {
        this.authDomainValue = authDomainValue;
    }
}
