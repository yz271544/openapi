package com.teradata.openapi.framework.message.request;

import java.io.Serializable;
import java.util.List;

/**
 * Created by John on 2016/9/28.
 */
public class FormDetail implements Serializable {
    private static final long serialVersionUID = 7702954533839644647L;

    private String rptName;
    private List<ExcelTitles> excelTitles;

    public String getRptName() {
        return rptName;
    }

    public void setRptName(String rptName) {
        this.rptName = rptName;
    }

    public List<ExcelTitles> getExcelTitles() {
        return excelTitles;
    }

    public void setExcelTitles(List<ExcelTitles> excelTitles) {
        this.excelTitles = excelTitles;
    }
}
