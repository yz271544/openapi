package com.teradata.portal.admin.right.entity;

import java.io.Serializable;

/**
 * Created by Evan on 2016/9/5.
 */
public class Apply2RightDetl extends RightApplyDetl implements Serializable{


    private static final long serialVersionUID = -6130650772514929610L;

    private Integer rightId;

    public Integer getRightId() {
        return rightId;
    }

    public void setRightId(Integer rightId) {
        this.rightId = rightId;
    }
}
