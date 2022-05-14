package com.teradata.portal.admin.auth.entity;

import com.teradata.portal.admin.auth.plugin.mybatis.plugin.PageView;

import java.io.Serializable;

/**
 * Created by Evan on 2016/7/5.
 */
public class Page implements Serializable {

    private Integer start;

    private Integer end;

    public Integer getEnd() {
        return end;
    }

    public void setEnd(Integer end) {
        this.end = end;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }
}
