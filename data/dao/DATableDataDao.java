package com.zjcds.da.data.dao;

import java.util.List;
import java.util.Map;

import com.cds.framework.dao.paging.PageInfo;

/**
 * 表格数据接口 dao层
 * 
 * @author yuzq
 * 
 */
public interface DATableDataDao {

    /**
     * 取得表格分页数据
     * 
     * @param sql
     * @return
     */
    List<Map<String, Object>> daQueryData(Map<String, String> sql, PageInfo pageInfo);

    /**
     * 取得数据数量
     * 
     * @param sql
     * @return
     */
    int daGetTotalSql(Map<String, String> sql);

    /**
     * 对列求和
     * 
     * @param sql
     * @return
     */
    Map<String, Object> daGetSumVal(Map<String, String> sql);

    /**
     * 取得所有数据
     * 
     * @param sql
     * @return
     * @author yuzq created on 2013-10-31
     * @since CDS Framework 1.0
     */
    List<Map<String, Object>> daQueryData(Map<String, String> sql);

    /**
     * 表格注脚
     * 
     * @param sql
     * @return
     */
	Map<String, Object> daGetFooterVal(Map<String, String> sql);
}
