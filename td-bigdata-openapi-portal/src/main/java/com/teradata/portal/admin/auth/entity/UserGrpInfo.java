package com.teradata.portal.admin.auth.entity;

/*import com.teradata.openapi.framework.model.ApiInfoRow;
import com.teradata.openapi.framework.model.SourceInfoRow;*/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * 用户组信息
 * Created by Evan on 2016/7/3.
 */
@SuppressWarnings("serial")
public class UserGrpInfo implements Serializable {

    private int id;

    private int parentId;

    private String parentName;

    private String name;

    private String desc;

    private int orderId;//排序代码

    private int isntLeafNode;//是否是叶子节点

    private List<UserGrpInfo> childrens = new ArrayList<UserGrpInfo>();

    //private Set<ApiInfoRow> apiInfos = new HashSet<ApiInfoRow>(); //用户所可以访问的api

    //private Set<SourceInfoRow> sourceInfos = new HashSet<SourceInfoRow>();//用户所可以访问的数据源

    private Set<SysMenuInfo> sysMenuInfos = new HashSet<SysMenuInfo>();//用户所可以访问的菜单

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getParentId() {
        return parentId;
    }

    public void setParentId(int parentId) {
        this.parentId = parentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getIsntLeafNode() {
        return isntLeafNode;
    }

    public void setIsntLeafNode(int isntLeafNode) {
        this.isntLeafNode = isntLeafNode;
    }


    public List<UserGrpInfo> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<UserGrpInfo> childrens) {
        this.childrens = childrens;
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

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
