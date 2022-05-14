package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;

/**
 * 系统页面元素信息
 * Created by Evan on 2016/7/4.
 */
@SuppressWarnings("serial")
public class PageElemtInfo implements Serializable{

    private int elemtId; //元素id

    private int menuId;//菜单id

    private String elemtCode;//元素代码

    private String elemtName;//元素名称

    private String elemtCss;//元素样式

    private String elemtImg;//元素图标

    private String elemtJs;//元素js

    private int elemtType;//元素类型

    private String initStat;//元素状态

    private int orderId;//排序id

    private String elemtHtml;//元素html代码


    public int getElemtId() {
        return elemtId;
    }

    public void setElemtId(int elemtId) {
        this.elemtId = elemtId;
    }

    public int getMenuId() {
        return menuId;
    }

    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }


    public String getElemtName() {
        return elemtName;
    }

    public void setElemtName(String elemtName) {
        this.elemtName = elemtName;
    }

    public String getElemtCss() {
        return elemtCss;
    }

    public void setElemtCss(String elemtCss) {
        this.elemtCss = elemtCss;
    }

    public String getElemtImg() {
        return elemtImg;
    }

    public void setElemtImg(String elemtImg) {
        this.elemtImg = elemtImg;
    }

    public String getElemtJs() {
        return elemtJs;
    }

    public void setElemtJs(String elemtJs) {
        this.elemtJs = elemtJs;
    }

    public int getElemtType() {
        return elemtType;
    }

    public void setElemtType(int elemtType) {
        this.elemtType = elemtType;
    }

    public String getInitStat() {
        return initStat;
    }

    public void setInitStat(String initStat) {
        this.initStat = initStat;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getElemtCode() {
        return elemtCode;
    }

    public void setElemtCode(String elemtCode) {
        this.elemtCode = elemtCode;
    }

    public String getElemtHtml() {
        return elemtHtml;
    }

    public void setElemtHtml(String elemtHtml) {
        this.elemtHtml = elemtHtml;
    }
}
