package com.zjcds.da.action;


import com.cds.framework.dao.paging.PageInfo;
import com.zjcds.da.service.table.DATableDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 表格数据动作类
 * 
 * @author yuzq
 * 
 */
public class DATableDataAction extends PortalBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String data; // 前端请求参数

    private Integer page; // 当前页

    private Integer rows; // 每页的行数

    private String dateFormat;// 是否要日期的格式化，如果需要则传递格式化的格式，如果不传则不进行内容日期格式化

    private DATableDataService tableDataService;

    private String returnData;// 返回数据

    private PageInfo pageInfo;

    @Override
    public String doExecute() throws Exception {
        return null;
    }

    /**
     * 取得菜单(查询条件,表格框)
     * 
     * @return
     * @throws Exception
     */
    public String daGetMenu() throws Exception {
        returnData = tableDataService.daGetMenu(data);
        super.writeJson(returnData);
        return null;
    }

    /**
     * 查询表格数据
     * 
     * @return
     * @throws Exception
     */
    public String daQueryData() throws Exception {
        if (pageInfo == null)
            pageInfo = new PageInfo();
        // pageInfo.setPageStart((page - 1) * rows);
        // pageInfo.setPageEnd(page * rows + 1);
        if (page != null)
            pageInfo.setPageNo(page);
        if (rows != null)
            pageInfo.setPageSize(rows);
        returnData = tableDataService.daQueryData(data, pageInfo, dateFormat);
        super.writeJson(returnData);
        return null;
    }

    /**
     * 打印表格数据
     * 
     * @return
     * @throws Exception
     * @author yuzq created on 2013-10-29
     * @since CDS Framework 1.0
     */
    public String daPrintTableData() throws Exception {
        returnData = tableDataService.daPrintTableData(data);
        super.writeJson(returnData);
        return null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public DATableDataService getTableDataService() {
        return tableDataService;
    }

    public void setTableDataService(DATableDataService tableDataService) {
        this.tableDataService = tableDataService;
    }

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getRows() {
        return rows;
    }

    public void setRows(Integer rows) {
        this.rows = rows;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }
}
