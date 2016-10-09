package com.zjcds.da.view.bean;

/**
 * 分页信息
 * 
 * @author yuzq
 * 
 */
public class PageInfo {

    private Integer pageStart; // 每页的开始记录

    private Integer pageEnd; // 每页的结束记录

    public Integer getPageStart() {
        return pageStart;
    }

    public void setPageStart(Integer pageStart) {
        this.pageStart = pageStart;
    }

    public Integer getPageEnd() {
        return pageEnd;
    }

    public void setPageEnd(Integer pageEnd) {
        this.pageEnd = pageEnd;
    }

}
