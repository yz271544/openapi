package com.teradata.portal.admin.auth.vo;

import com.teradata.portal.admin.auth.entity.UserGrpInfo;
import com.teradata.portal.admin.auth.util.TreeObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Evan on 2016/7/7.
 */
public class UserGrpInfoTreeObject extends TreeObject<UserGrpInfo> {

    private String desc;

    private int orderId;

    private int isntLeafNode;

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


}
