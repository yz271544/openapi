package com.teradata.openapi.access.biz.bean;

import java.io.Serializable;

/**
 * Created by John on 2016/8/23.
 */
public class DownLoadAttr implements Serializable {
    private static final long serialVersionUID = 6121633407041553907L;

    private String formCode;

    private String formatFileFinger;

    public String getFormCode() {
        return formCode;
    }

    public void setFormCode(String formCode) {
        this.formCode = formCode;
    }

    public String getFormatFileFinger() {
        return formatFileFinger;
    }

    public void setFormatFileFinger(String formatFileFinger) {
        this.formatFileFinger = formatFileFinger;
    }

}
