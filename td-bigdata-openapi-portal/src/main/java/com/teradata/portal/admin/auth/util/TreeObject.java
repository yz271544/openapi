package com.teradata.portal.admin.auth.util;

import com.teradata.portal.admin.auth.entity.UserGrpInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * 这个是列表树形式显示的实体,
 * 这里的字段是在前台显示所有的,可修改
 * Created by Evan on 2016/7/7.
 */
public class TreeObject<T> {

    private int id;

    private int parentId;

    private String name;

    private List<T> childrens = new ArrayList<T>();

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

    public List<T> getChildrens() {
        return childrens;
    }

    public void setChildrens(List<T> childrens) {
        this.childrens = childrens;
    }
}
