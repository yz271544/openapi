package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;

/**
 * API类别
 * Created by Evan on 2016/7/22.
 */
public class ApiSortCode implements Serializable {

    private Integer apiSort;
    private String apiSortName;
    private Integer apiSortLvl;
    private Integer apiSortFathrId;

    public Integer getApiSort() {
        return apiSort;
    }

    public void setApiSort(Integer apiSort) {
        this.apiSort = apiSort;
    }

    public String getApiSortName() {
        return apiSortName;
    }

    public void setApiSortName(String apiSortName) {
        this.apiSortName = apiSortName;
    }

    public Integer getApiSortLvl() {
        return apiSortLvl;
    }

    public void setApiSortLvl(Integer apiSortLvl) {
        this.apiSortLvl = apiSortLvl;
    }

    public Integer getApiSortFathrId() {
        return apiSortFathrId;
    }

    public void setApiSortFathrId(Integer apiSortFathrId) {
        this.apiSortFathrId = apiSortFathrId;
    }
}
