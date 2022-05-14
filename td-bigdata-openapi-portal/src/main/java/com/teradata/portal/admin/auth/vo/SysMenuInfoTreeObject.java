package com.teradata.portal.admin.auth.vo;

import com.teradata.portal.admin.auth.util.TreeObject;

/**
 * Created by Evan on 2016/7/7.
 */
public class SysMenuInfoTreeObject extends TreeObject{

    private String desc;

    private int orderId;

    private int isntLeafNode;

    private String resCode;

    private String parentName;

    private String link;

    private String icon;

    private String htmlCode;

    private String cssCode;

    private Integer ResType; // 1 菜单  2 页面button

    private Integer isntEff;

    private Integer rightFieldValue; //权限领域值

    private String rightMain;

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

    public String getRightMain() {
        return rightMain;
    }

    public void setRightMain(String rightMain) {
        this.rightMain = rightMain;
    }
}
