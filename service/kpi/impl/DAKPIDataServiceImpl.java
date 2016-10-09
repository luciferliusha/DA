package com.zjcds.da.service.kpi.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import net.sf.json.JSONObject;

import com.alibaba.fastjson.JSON;
import com.zjcds.da.data.dao.DACommonDataDao;
import com.zjcds.da.data.dao.DATableDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.insert.impl.DAInsertDataServiceImpl;
import com.zjcds.da.service.kpi.DAKPIDataService;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 表格数据接口实现类
 * 
 * @author yuzq
 * 
 */
public class DAKPIDataServiceImpl extends DABaseService implements DAKPIDataService {

    private DATableDataDao tableDataDao;
    private DACommonDataDao commonDataDao;
    private Logger logger = Logger.getLogger(DAInsertDataServiceImpl.class);

    @SuppressWarnings("unchecked")
    @Override
    public String daKPIData(String data) throws Exception {
        Map<String, String> params = JSON.parseObject(data, Map.class);
        Iterator<String> it = params.keySet().iterator();
        String key = null;
        String querySql = null;
        JSONObject json = new JSONObject();
        while (it.hasNext()) {
            key = it.next();
            querySql = params.get(key);
            Map<String, String> sqlMap = getSqlMap(querySql);
            List<Map<String, Object>> tableDataList = tableDataDao.daQueryData(sqlMap);
            json.put(key, this.capsulationEasyUiData(tableDataList));
        }
        return json.toString();
    }
    
	@SuppressWarnings("unchecked")
	@Override
    public String daKPIDataAj(String data) throws Exception {
    	Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        String sql = ParseSqlConfig.getSql(data);
        String querySql = getAppendSqlWhereEquals(params, sql);
        //String querySql = getAppendSqlWhere(params, sql);
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("sql", querySql);
        logger.info("执行的sql：" + querySql);
		List<LinkedHashMap<String, Object>> detailData = commonDataDao.getCommonData(sqlMap);
		return getSuccessReturnData(detailData);
    }

    /** 将查询语句转换为map */
    private Map<String, String> getSqlMap(String querySql) throws Exception {
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("dataSql", querySql);
        return sqlMap;
    }

    /**
     * 组装数据用于EasyUi表格显示
     * 
     * @param tableDataList
     * @return
     * @throws Exception
     */
    private String capsulationEasyUiData(List<Map<String, Object>> tableDataList) throws Exception {
        JSONObject json = new JSONObject();
        json.put("total", tableDataList.size());
        json.put("rows", tableDataList);
        return json.toString();
    }

    public DATableDataDao getTableDataDao() {
        return tableDataDao;
    }

    public void setTableDataDao(DATableDataDao tableDataDao) {
        this.tableDataDao = tableDataDao;
    }

	public DACommonDataDao getCommonDataDao() {
		return commonDataDao;
	}

	public void setCommonDataDao(DACommonDataDao commonDataDao) {
		this.commonDataDao = commonDataDao;
	}
}
