package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2016/7/13.
 */
public class ResourceInfo implements Serializable{

    private Integer id;

    private Integer parentId;

    private String name;

    private String desc;

    private Integer isntLeafNode;

    private Integer orderId;

    private List<ResourceInfo> children = new ArrayList<ResourceInfo>();



    private String resCode;

    private String parentName;

    private String link;

    private String icon;

    private String htmlCode;

    private String cssCode;

    private Integer ResType; // 1 菜单  2 页面button

    private Integer isntEff;

    private Integer rightFieldValue; //权限领域值

    private String  rightMain;//权限主体


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getParentId() {
        return parentId;
    }

    public void setParentId(Integer parentId) {
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

    public Integer getIsntLeafNode() {
        return isntLeafNode;
    }

    public void setIsntLeafNode(Integer isntLeafNode) {
        this.isntLeafNode = isntLeafNode;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public List<ResourceInfo> getChildren() {
        return children;
    }

    public void setChildren(List<ResourceInfo> children) {
        this.children = children;
    }

    public String getResCode() {
        return resCode;
    }

    public void setResCode(String resCode) {
        this.resCode = resCode;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getHtmlCode() {
        return htmlCode;
    }

    public void setHtmlCode(String htmlCode) {
        this.htmlCode = htmlCode;
    }

    public String getCssCode() {
        return cssCode;
    }

    public void setCssCode(String cssCode) {
        this.cssCode = cssCode;
    }

    public Integer getResType() {
        return ResType;
    }

    public void setResType(Integer resType) {
        ResType = resType;
    }

    public Integer getIsntEff() {
        return isntEff;
    }

    public void setIsntEff(Integer isntEff) {
        this.isntEff = isntEff;
    }

    public Integer getRightFieldValue() {
        return rightFieldValue;
    }

    public void setRightFieldValue(Integer rightFieldValue) {
        this.rightFieldValue = rightFieldValue;
    }

    public String getRightMain() {
        return rightMain;
    }

    public void setRightMain(String rightMain) {
        this.rightMain = rightMain;
    }
}
