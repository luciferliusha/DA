package com.zjcds.da.service.table;

import com.cds.framework.dao.paging.PageInfo;

/**
 * 表格数据服务接口
 * 
 * @author yuzq
 * 
 */
public interface DATableDataService {

    /**
     * 取得菜单(查询条件,表格框)
     * 
     * @param data
     */
    String daGetMenu(String data) throws Exception;

    /**
     * 查询数据
     * 
     * @param data
     * @return
     * @throws Exception
     */
    String daQueryData(String data, PageInfo pageInfo, String dateFormat) throws Exception;

    /**
     * 打印表格数据
     * 
     * @return
     * @throws Exception
     * @author yuzq created on 2013-10-29
     * @since CDS Framework 1.0
     */
    String daPrintTableData(String data) throws Exception;
}
