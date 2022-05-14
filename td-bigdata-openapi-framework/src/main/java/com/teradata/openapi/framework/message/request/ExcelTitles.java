package com.teradata.openapi.framework.message.request;

import java.io.Serializable;
import java.util.List;

/**
 * Created by John on 2016/9/28.
 */
public class ExcelTitles implements Serializable {
    private static final long serialVersionUID = 7702954533839644647L;

    private Integer rowIndex;
    private List<CellList> cellList;

    public Integer getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(Integer rowIndex) {
        this.rowIndex = rowIndex;
    }

    public List<CellList> getCellList() {
        return cellList;
    }

    public void setCellList(List<CellList> cellList) {
        this.cellList = cellList;
    }
}
