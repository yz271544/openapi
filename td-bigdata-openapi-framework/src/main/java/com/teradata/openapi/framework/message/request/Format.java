package com.teradata.openapi.framework.message.request;


import java.io.Serializable;

/**
 * Created by John on 2016/9/28.
 */
public class Format implements Serializable {
    private static final long serialVersionUID = 7702954533839644647L;

    private String formType;
    private FormDetail formDetail;

    public String getFormType() {
        return formType;
    }

    public void setFormType(String formType) {
        this.formType = formType;
    }

    public FormDetail getFormDetail() {
        return formDetail;
    }

    public void setFormDetail(FormDetail formDetail) {
        this.formDetail = formDetail;
    }
}