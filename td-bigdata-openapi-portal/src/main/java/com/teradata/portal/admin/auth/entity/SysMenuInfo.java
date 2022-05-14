package com.teradata.portal.admin.auth.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 系统菜单信息
 * Created by Evan on 2016/7/4.
 */
@SuppressWarnings("serial")
public class SysMenuInfo implements Serializable {

    private int menuID; //菜单id

    private String menuCode; //菜单代码

    private int fathrMenuId;//菜单父id

    private String menuName;//菜单名称

    private int orderId;//排序代码

    private String link;//菜单链接

    private String icon;//菜单图标

    private int isntEff;//是否有效

    private int isntLeafNode;//是否是子菜单

    private List<SysMenuInfo> children = new ArrayList<SysMenuInfo>(); //子菜单

    private List<PageElemtInfo> elemtInfos = new ArrayList<PageElemtInfo>();//每个页面中的元素集


    public int getMenuID() {
        return menuID;
    }

    public void setMenuID(int menuID) {
        this.menuID = menuID;
    }

    public String getMenuCode() {
        return menuCode;
    }

    public void setMenuCode(String menuCode) {
        this.menuCode = menuCode;
    }

    public int getFathrMenuId() {
        return fathrMenuId;
    }

    public void setFathrMenuId(int fathrMenuId) {
        this.fathrMenuId = fathrMenuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
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

    public int getIsntEff() {
        return isntEff;
    }

    public void setIsntEff(int isntEff) {
        this.isntEff = isntEff;
    }

    public int getIsntLeafNode() {
        return isntLeafNode;
    }

    public void setIsntLeafNode(int isntLeafNode) {
        this.isntLeafNode = isntLeafNode;
    }

    public List<SysMenuInfo> getChildren() {
        return children;
    }

    public void setChildren(List<SysMenuInfo> children) {
        this.children = children;
    }

    public List<PageElemtInfo> getElemtInfos() {
        return elemtInfos;
    }

    public void setElemtInfos(List<PageElemtInfo> elemtInfos) {
        this.elemtInfos = elemtInfos;
    }
}
