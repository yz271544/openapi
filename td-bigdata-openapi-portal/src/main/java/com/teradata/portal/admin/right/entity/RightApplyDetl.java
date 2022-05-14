package com.teradata.portal.admin.right.entity;

import java.io.Serializable;

/**
 * Created by Evan on 2016/9/5.
 */
public class RightApplyDetl implements Serializable{


    private static final long serialVersionUID = -6130650772514929610L;

    private Integer applyId;

    private String rightMain;

    private Integer rightMainVal;

    private String rightField;

    private Integer rightFieldVal;

    private Integer eff_stat;

    public Integer getApplyId() {
        return applyId;
    }

    public void setApplyId(Integer applyId) {
        this.applyId = applyId;
    }

    public String getRightMain() {
        return rightMain;
    }

    public void setRightMain(String rightMain) {
        this.rightMain = rightMain;
    }

    public Integer getRightMainVal() {
        return rightMainVal;
    }

    public void setRightMainVal(Integer rightMainVal) {
        this.rightMainVal = rightMainVal;
    }

    public String getRightField() {
        return rightField;
    }

    public void setRightField(String rightField) {
        this.rightField = rightField;
    }

    public Integer getRightFieldVal() {
        return rightFieldVal;
    }

    public void setRightFieldVal(Integer rightFieldVal) {
        this.rightFieldVal = rightFieldVal;
    }

    public Integer getEff_stat() {
        return eff_stat;
    }

    public void setEff_stat(Integer eff_stat) {
        this.eff_stat = eff_stat;
    }
}
