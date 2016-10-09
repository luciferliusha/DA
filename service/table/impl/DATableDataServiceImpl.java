package com.zjcds.da.service.table.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.alibaba.fastjson.JSON;
import com.cds.framework.dao.paging.PageInfo;
import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.data.dao.DATableDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.table.DATableDataService;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 表格数据接口实现类
 * 
 * @author yuzq
 * 
 */
public class DATableDataServiceImpl extends DABaseService implements DATableDataService {

    private DATableDataDao tableDataDao;

    private Logger logger = Logger.getLogger(DATableDataServiceImpl.class);

    /**
     * @see com.zjcds.da.service.table.DATableDataService#daGetMenu(java.lang.String)
     */
    @Override
    public String daGetMenu(String data) throws Exception {
        if (data != null && !"".equals(data.trim())) {
            String jsonFileStr = super.getJsonFileContent(data);
            JSONObject jsonObject = JSONObject.fromObject(jsonFileStr);
            returnData = super.getSuccessReturnData(jsonObject.getString(DAConstant.JSON_FILE_DATA));
        }
        else {
            returnData = super.getFailReturnData("参数data不能为空!");
        }
        return returnData;
    }

    // /** 取得json文件内容 */
    // @SuppressWarnings("unchecked")
    // private String getJsonFileContent(String data) throws Exception {
    // Map<String, String> requestParams = (Map<String, String>) JsonUtil.getMapFromJson(data);
    // String jsonFileName = requestParams.get(DAConstant.JSON_FILE_ID);
    // logger.info("读取" + jsonFileName + "文件!");
    // return JsonUtil.getJSONString(ReadFile.getFileContent(jsonFileName));
    // }

    /**
     * @see com.zjcds.da.service.table.DATableDataService#daqueryData(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String daQueryData(String data, PageInfo pageInfo, String dateFormat) throws Exception {
        Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        String configSQL = ParseSqlConfig.getSqlById(String.valueOf(params.get("id")));
        String querySql = getQuerySql(params, configSQL, pageInfo);
        Map<String, String> sqlMap = getSqlMap(querySql);
        List<Map<String, Object>> tableDataList = tableDataDao.daQueryData(sqlMap, pageInfo);
        Map<String, Object> sumVal = getSumVal(params, configSQL);
        List<Map<String, Object>> footerList = getFooterList(params, configSQL);
        List<Map<String, Object>> sumList = new ArrayList<Map<String, Object>>();
        if (sumVal != null && sumVal.size() != 0) {
            sumList.add(sumVal);
        }
        return this.capsulationEasyUiData(tableDataList, pageInfo.getTotalCount(), sumList, dateFormat, footerList);
    }

    /** 表格注脚，包括求和、求平均值，以及自定义的计算功能  */
    @SuppressWarnings( { "unchecked" })
    private List<Map<String, Object>> getFooterList(Map<String, Object> params, String sql) throws Exception {	
    	String childSql = getSumValChildSql(params, sql);
    	Map<String, Object> footerMap = new HashMap<String, Object>();
    	List<Map<String, Object>> footerList = new ArrayList<Map<String, Object>>();
        JSONArray footer = (JSONArray) params.get("footer");
        if(footer != null){
	    	for (int i = 0; i < footer.size(); i++) {
	    		StringBuffer sb = new StringBuffer();
	    		String function = null;
				JSONObject object = (JSONObject)footer.get(i);
				for (Iterator<?> j = object.keys(); j.hasNext();) {
		                String key = (String) j.next();
		                String value = (String) object.get(key);
		                if(key.equals("type")){
	    					if(value.equals("sum")){//求和
	    						function = "sum(";
	    					}else if(value.equals("average")){//求平均值
	    						function = "avg(";
	    					}else if(value.equals("custom_defined")){//自定义的计算功能
	    						function = "(";
	    					}
	    				}
	    				if(key.equals("field")){//参与计算的字段
	    					function += value + ") ";			
	    				}
	    				if(key.equals("field_location")){//计算结果在表格中的展示位置
	    					function += "as " + value + ",";			
	    				}
				 }
				 sb.append(function);
				 String footerPart = sb.toString().substring(0, sb.toString().length() - 1);
		 		 String baseSql = "select " + footerPart + " from (table) ttttt";
		 		 String footerSql = baseSql.replaceAll("table", childSql);
		 		 Map<String, String> sqlMap = new HashMap<String, String>();
		 	     sqlMap.put("footerSql", footerSql);
		 	     footerMap = tableDataDao.daGetFooterVal(sqlMap);
		 	     if (footerMap != null && footerMap.size() != 0){
		        	 String locationKey = null;
		        	 String locationValue = null;
			 	     for (Iterator<?> j = object.keys(); j.hasNext();) {
			 	    	 String key = (String) j.next();
			             String value = (String) object.get(key);
			             if(key.equals("title")){//计算结果的意义名称
			            	 locationValue = value;
		 				 }
		 				 if(key.equals("title_location")){//意义名称在表格中的展示位置
		 					 locationKey = value;			
		 				 }
			 	     }
			 	     footerMap.put(locationKey, locationValue);
		 	     }
		 	     footerList.add(footerMap);
			}
        }
    	return footerList;
	}

	/**
     * @see com.zjcds.da.service.table.DATableDataService#daPrintTableData(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String daPrintTableData(String data) throws Exception {
        Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        String configSQL = ParseSqlConfig.getSqlById(String.valueOf(params.get(DAConstant.SQL_CONFIG_ID)));
        String querySql = getQuerySql(params, configSQL, null);
        Map<String, String> sqlMap = getSqlMap(querySql);
        List<Map<String, Object>> tableDataList = tableDataDao.daQueryData(sqlMap);
        // Map<String, Object> sumVal = getSumVal(params, configSQL);
        // List<Map<String, Object>> sumList = new ArrayList<Map<String, Object>>();
        // if (sumVal != null && sumVal.size() != 0) {
        // sumList.add(sumVal);
        // }
        return super.getSuccessReturnData(tableDataList);
    }

    /** 取得查询sql语句 */
    protected String getQuerySql(Map<String, Object> params, String sql, PageInfo pageInfo) throws Exception {
        sql = appendQueryConditionSql(params, sql);
        sql = appendGroupBySql(params, sql);
        sql = appendOrderBySql(params, sql);
        sql = appendRownumSql(sql);
        sql = appendTopCondition(params, sql);
        logger.info("SQL:" + sql);
        return sql;
    }

    /** 将查询语句转换为map */
    private Map<String, String> getSqlMap(String querySql) throws Exception {
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("dataSql", querySql);
        return sqlMap;
    }

    /** 增加查询条件 */
    protected String appendQueryConditionSql(Map<String, Object> params, String sql) throws Exception {
        sql = getAppendSqlWhere(params, sql);
        return sql;
    }

    /** 增加排序项 */
    protected String appendGroupBySql(Map<String, Object> params, String sql) throws Exception {
        String groupBy = (String) params.get("groupBy");
        if (groupBy != null && !"".equals(groupBy.trim())) {
            sql += " group by " + groupBy;
        }
        return sql;
    }

    /** 增加排序项 */
    @SuppressWarnings("unchecked")
    protected String appendOrderBySql(Map<String, Object> params, String sql) throws Exception {
        Map<String, Object> sort = (Map<String, Object>) params.get("sort");
        if (sort != null && sort.size() != 0) {
            String sortName = (String) sort.get("sortName");
            if (sortName != null && sortName != "") {
                sql += " order by " + sortName;
            }
        }
        return sql;
    }

    /** 增加行显示 */
    protected String appendRownumSql(String sql) throws Exception {
        String baseSql = "select rownum as RN,tt.* from (table) tt";
        String returnSql = baseSql.replaceAll("table", sql);
        return returnSql;
    }

    /** 增加top条件, 如显示前10记录 */
    @SuppressWarnings("unchecked")
    protected String appendTopCondition(Map<String, Object> params, String sql) throws Exception {
        Map<String, Object> sort = (Map<String, Object>) params.get("sort");
        String returnSql = null;
        if (sort != null && sort.size() != 0) {
            String top = (String) sort.get("top");
            if (top != null && top != "") {
                String baseSql = "select * from (table) ttt";
                baseSql += " where " + "RN<=" + Integer.parseInt(top);
                returnSql = baseSql.replaceAll("table", sql);
            }
            else {
                returnSql = sql;
            }
        }
        else {
            returnSql = sql;
        }
        return returnSql;
    }

    /** 对列求和 */
    @SuppressWarnings( { "unchecked" })
    private Map<String, Object> getSumVal(Map<String, Object> params, String sql) throws Exception {
        String childSql = getSumValChildSql(params, sql);
        Map<String, Object> sumMap = new HashMap<String, Object>();
        Map<String, Object> sums = (Map<String, Object>) params.get("sum");
        if (sums != null && sums.size() != 0) {
            StringBuffer sb = new StringBuffer();
            for (Entry<String, Object> entry : sums.entrySet()) {
                String sumFunction = "sum(";
                String sumField = (String) sums.get(entry.getKey());
                sumFunction += sumField + ") as " + sumField + ",";
                sb.append(sumFunction);
            }
            String sumPart = sb.toString().substring(0, sb.toString().length() - 1);
            String baseSql = "select " + sumPart + " from (table) ttttt";
            String sumSql = baseSql.replaceAll("table", childSql);
            Map<String, String> sqlMap = new HashMap<String, String>();
            sqlMap.put("sumSql", sumSql);
            sumMap = tableDataDao.daGetSumVal(sqlMap);
            if (sumMap != null && sumMap.size() != 0)
                sumMap.put("RN", "统计");
        }
        return sumMap;
    }

    private String getSumValChildSql(Map<String, Object> params, String sql) throws Exception {
        sql = appendQueryConditionSql(params, sql);
        sql = appendGroupBySql(params, sql);
        sql = appendOrderBySql(params, sql);
        sql = appendRownumSql(sql);
        sql = appendTopCondition(params, sql);
        return sql;
    }

    /** 取得增加查询条件语句 */
    @SuppressWarnings( { "unchecked", "unused" })
    private String getAppendConditionSql(String data, String sql) throws Exception {
        Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        for (Entry<String, Object> entry : params.entrySet()) {
            if (entry.getKey().equals("where")) {
                Map<String, Object> conditions = (Map<String, Object>) entry.getValue();
                for (Entry<String, Object> condition : conditions.entrySet()) {
                    sql = sql + "  and  " + condition.getKey() + " = " + "'" + condition.getValue() + "'";
                }
            }
        }
        return sql;
    }

    /**
     * 组装数据用于EasyUi表格显示
     * 
     * @param tableDataList
     * @return
     * @throws Exception
     */
    private String capsulationEasyUiData(List<Map<String, Object>> tableDataList, int size,
            List<Map<String, Object>> sum, String dateFormat, List<Map<String, Object>> footer) throws Exception {
        JSONObject json = new JSONObject();
        json.put("total", size);
        if (dateFormat != null && !dateFormat.isEmpty()) {
            json.put("rows", JSON.toJSONStringWithDateFormat(tableDataList, dateFormat));
        }
        else {
            json.put("rows", tableDataList);
        }
        if(sum != null && sum.size() != 0){
        	json.put("footer", sum);
        }else if(footer != null && footer.size() != 0){
        	json.put("footer", footer);
        }
        return json.toString();
    }

    public DATableDataDao getTableDataDao() {
        return tableDataDao;
    }

    public void setTableDataDao(DATableDataDao tableDataDao) {
        this.tableDataDao = tableDataDao;
    }
}
