package com.zjcds.da.data.dao;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;




/**
 * DA公共 DAO层
 * 
 * @author linj created on 2013-7-29
 * @since DA 6.0
 */
public interface DACommonDataDao {

    /**
     * 取得下拉框数据
     * @param sqlMap
     * @return
     * @author yuzq created on 2013-8-22 
     * @since CDS Framework 1.0
     */
    public List<LinkedHashMap<String,Object>> getCommonData(Map<String,String> sqlMap);
}
