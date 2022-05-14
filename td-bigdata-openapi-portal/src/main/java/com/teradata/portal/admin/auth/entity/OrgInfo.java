package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;

/**
 * 组织机构信息
 * Created by Evan on 2016/7/3.
 */
@SuppressWarnings("serial")
public class OrgInfo implements Serializable{

    private int orgId; //组织机构id

    private String orgName; //组织机构名称

    private String orgDesc; //组织机构描述

    public int getOrgId() {
        return orgId;
    }

    public void setOrgId(int orgId) {
        this.orgId = orgId;
    }

    public String getOrgName() {
        return orgName;
    }

    public void setOrgName(String orgName) {
        this.orgName = orgName;
    }

    public String getOrgDesc() {
        return orgDesc;
    }

    public void setOrgDesc(String orgDesc) {
        this.orgDesc = orgDesc;
    }

    @Override
    public String toString() {
        return "OrgInfo{" +
                "orgId=" + orgId +
                ", orgName='" + orgName + '\'' +
                ", orgDesc='" + orgDesc + '\'' +
                '}';
    }
}
