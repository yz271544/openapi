package com.teradata.portal.admin.auth.plugin.mybatis.plugin;

import com.xiaoleilu.hutool.db.Page;

import java.util.List;

/**
 * 分页封装函数
 * Created by Evan on 2016/7/4.
 */
public class PageView {

    private List records; //分页数据

    private Long pageCount;//总页数，这个数是计算出来的

    private int pageSize = 10;//每页显示几条记录

    private int pageNow = 1;//默认当前页为第一页，这个数是计算出来的

    private long rowCount;//总记录数

    private int startPage;//从第几条记录开始

    private int pageCode = 10;//规定显示5个页码


    public PageView(){

    }

    /**
     * 使用构造函数，强制必须输入，每页显示数量和当前页
     * @param pageSize 每页显示数量
     * @param pageNow 当前页
     */
    public PageView(int pageSize,int pageNow){

        this.pageSize = pageSize;
        this.pageNow = pageNow;

    }

    /**
     * 使用构造函数，强制必须输入当前页
     * @param pageNow
     */
    public PageView(int pageNow){
        this.pageNow = pageNow;
        startPage = (this.pageNow - 1 ) * this.pageSize;
    }

    /**
     * 要获得记录的开始索引，即 开始页面
     * @return
     */
    public int getFirstResult(){
        return (this.pageNow - 1) * this.pageSize;
    }

    public int getPageCode(){
        return pageCode;
    }

    public void setPageCode(int pageCode){
        this.pageCode = pageCode;
    }

    /**
     * 查询结果方法，把记录数 结果集 放入到 PageView对象中
     * @param rowCount 总记录数
     * @param records 结果集
     */

    public void setQueryResult(long rowCount,List records){

        setRowCount(rowCount);
        setRecords(records);

    }

    public void setRowCount(long rowCount) {
        this.rowCount = rowCount;
        setPageCount(this.rowCount % this.pageSize == 0 ? this.rowCount / this.pageSize : this.rowCount / this.pageSize + 1);
    }


    public List getRecords() {
        return records;
    }

    public void setRecords(List records) {
        this.records = records;
    }

    public int getPageNow() {
        return pageNow;
    }

    public void setPageNow(int pageNow) {
        this.pageNow = pageNow;
    }

    public long getPageCount() {
        return pageCount;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public long getRowCount() {
        return rowCount;
    }

    public int getStartPage() {
        return startPage;
    }

    public void setStartPage(int startPage) {
        this.startPage = startPage;
    }

    public void setPageCount(long pageCount) {
        this.pageCount = pageCount;
    }


}
