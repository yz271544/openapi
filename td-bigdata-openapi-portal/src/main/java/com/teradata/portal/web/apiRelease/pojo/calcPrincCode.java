package com.teradata.portal.web.apiRelease.pojo;

public class calcPrincCode {
    private Integer calcPrincId;

    private String calcPrincDesc;

    private String calcFormula;

    private String subhdType;

    private String argForm;

    private String argSamp;

    public Integer getCalcPrincId() {
        return calcPrincId;
    }

    public void setCalcPrincId(Integer calcPrincId) {
        this.calcPrincId = calcPrincId;
    }

    public String getCalcPrincDesc() {
        return calcPrincDesc;
    }

    public void setCalcPrincDesc(String calcPrincDesc) {
        this.calcPrincDesc = calcPrincDesc == null ? null : calcPrincDesc.trim();
    }

    public String getCalcFormula() {
        return calcFormula;
    }

    public void setCalcFormula(String calcFormula) {
        this.calcFormula = calcFormula == null ? null : calcFormula.trim();
    }

    public String getSubhdType() {
        return subhdType;
    }

    public void setSubhdType(String subhdType) {
        this.subhdType = subhdType == null ? null : subhdType.trim();
    }

    public String getArgForm() {
        return argForm;
    }

    public void setArgForm(String argForm) {
        this.argForm = argForm == null ? null : argForm.trim();
    }

    public String getArgSamp() {
        return argSamp;
    }

    public void setArgSamp(String argSamp) {
        this.argSamp = argSamp == null ? null : argSamp.trim();
    }
}