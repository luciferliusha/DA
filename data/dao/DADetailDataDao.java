package com.zjcds.da.data.dao;

import java.util.List;
import java.util.Map;

public interface DADetailDataDao {
	public Map<String,Object> daGetDetailData(Map<String,String> sql);
	
    /**
     * 执行相应的SQL并返回数据
     * @param sqlMap
     * @return
     * @author majj created on 2014-04-14 
     * @since CDS Framework 1.0
     */
	public List<Map<String,Object>> querySql(Map<String,String> sql);
}
