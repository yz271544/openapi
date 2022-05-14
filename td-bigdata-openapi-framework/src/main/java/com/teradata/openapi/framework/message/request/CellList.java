package com.teradata.openapi.framework.message.request;

import java.io.Serializable;

/**
 * Created by John on 2016/9/28.
 */
public class CellList implements Serializable {
    private static final long serialVersionUID = 7702954533839644647L;
    private String cellName;
    private Integer rowIndex;
    private Integer colIndex;
    private Integer rowSpan;
    private Integer colSpan;

    public String getCellName() {
        return cellName;
    }

    public void setCellName(String cellName) {
        this.cellName = cellName;
    }

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public Integer getColIndex() {
        return colIndex;
    }

    public void setColIndex(Integer colIndex) {
        this.colIndex = colIndex;
    }

    public Integer getRowSpan() {
        return rowSpan;
    }

    public void setRowSpan(Integer rowSpan) {
        this.rowSpan = rowSpan;
    }

    public Integer getColSpan() {
        return colSpan;
    }

    public void setColSpan(Integer colSpan) {
        this.colSpan = colSpan;
    }
}
