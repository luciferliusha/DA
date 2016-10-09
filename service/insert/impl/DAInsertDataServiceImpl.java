package com.zjcds.da.service.insert.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.log4j.Logger;
import com.alibaba.fastjson.JSON;
import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.data.dao.DAInsertDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.insert.DAInsertDataService;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 获取数据服务Service实现类
 * 
 * @author xinyf
 * @date 2013-6-20
 */
public class DAInsertDataServiceImpl extends DABaseService implements DAInsertDataService {

    private DAInsertDataDao insertDataDao;

    private Logger logger = Logger.getLogger(DAInsertDataServiceImpl.class);

    @Override
    public String daInsertData(String data) throws Exception {
        try {
            // json解析配置信息
            Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
            //String fileName = (String) params.get(DAConstant.ID);
            // 执行sql
            String sql = ParseSqlConfig.getSql(data);
            String valueStr = (String) params.get(DAConstant.VALUES);
            StringBuffer bf = new StringBuffer(sql);
            StringBuffer tempBf = new StringBuffer();
            if(valueStr != null && !"".equals(valueStr.trim()) ){
            	String[] values = valueStr.split(",");
                for(int i=0; i<values.length; i++){
                	tempBf.append(",'").append(values[i]).append("'");
                }
            }
            if (tempBf.length() > 0) {
                sql = bf.append(" values (").append(tempBf.toString().replaceFirst(",", "")).append(")").toString();
            }
            Map<String, String> sqlParam = new HashMap<String, String>();
            sqlParam.put(DAConstant.SQLNAME, sql);
            logger.info("执行的sql：" + sql);
            insertDataDao.daInsertData(sqlParam);
            return getSuccessReturnData("新增数据成功!");
        }
        catch (Exception e) {
            logger.info(e, e);
            return getFailReturnData(e.getMessage());
        }
    }

    @Override
	public String daUpdateData(String data) throws Exception {
    	 try {
             // json解析配置信息
             Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
             //String fileName = (String) params.get(DAConstant.ID);
             // 执行sql
             String sql = ParseSqlConfig.getSql(data);
             String setString = (String) params.get(DAConstant.SET);
             String whereString = (String) params.get(DAConstant.WHERE);
             
             sql += " set " + setString + " where " + whereString;
             
             Map<String, String> sqlParam = new HashMap<String, String>();
             sqlParam.put(DAConstant.SQLNAME, sql);
             logger.info("执行的sql：" + sql);
             insertDataDao.daUpdateData(sqlParam);
             return getSuccessReturnData("修改数据成功!");
         }
         catch (Exception e) {
             logger.info(e, e);
             return getFailReturnData(e.getMessage());
         }
	}

    @Override
    public String daComInsertData(String data) throws Exception {
        try {
            // json解析配置信息
            Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
            String sql = ParseSqlConfig.getSql(data);
            Object valueObj =  params.get(DAConstant.VALUES);
            List<Map> valuesList = JSON.parseArray(valueObj.toString(), Map.class);
            String names = " (";
            String values = " values(";
            for (Map valueData : valuesList) {
                if(!valueData.get("value").toString().trim().equals("") && !valueData.get("value").toString().trim().equals("undefined") && valueData.get("value") != null){
                    names += valueData.get("name").toString()+",";
                    if(valueData.get("type").toString().equals("string")){
                        values += "'"+valueData.get("value").toString()+"',";
                    }
                    else if(valueData.get("type").toString().equals("num")){
                        values += valueData.get("value").toString()+",";
                    }
                    else if(valueData.get("type").toString().equals("year")){
                        //values += "to_date('"+valueData.get("value").toString()+"','YYYY'),";
                    	values += "'"+valueData.get("value").toString()+"',";//对于年份字段，常用字符串格式
                    }
                    else if(valueData.get("type").toString().equals("month")){
                        //values += "to_date('"+valueData.get("value").toString()+"','YYYY-MM'),";
                    	values += "'"+valueData.get("value").toString()+"',";//对于月份字段，常用字符串格式
                    }
                    else if(valueData.get("type").toString().equals("date")){
                        values += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD'),";
                    }
                    else if(valueData.get("type").toString().equals("hour")){
                        values += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24'),";
                    }
                    else if(valueData.get("type").toString().equals("minute")){
                        values += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24:MI'),";
                    }
                    else if(valueData.get("type").toString().equals("second")){
                        values += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24:MI:SS'),";
                    }
                    else{
                        values += "'"+valueData.get("value").toString()+"',";
                    }
                }
            }
            names = names.substring(0,names.length()-1) + ")";
            values = values.substring(0, values.length()-1) + ")";
            sql += names +values;
            Map<String, String> sqlParam = new HashMap<String, String>();
            sqlParam.put(DAConstant.SQLNAME, sql);
            logger.info("执行的sql：" + sql);
            insertDataDao.daComInsertData(sqlParam);
            return getSuccessReturnData("新增数据成功!");
        }
        catch (Exception e) {
            logger.info(e, e);
            return getFailReturnData(e.getMessage());
        }
    }
    
    @Override
	public String daComUpdateData(String data) throws Exception {
    	 try {
             Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
             String sql = ParseSqlConfig.getSql(data);
             String whereString = (String) params.get(DAConstant.WHERE);
             
             Object valueObj =  params.get(DAConstant.SET);
             List<Map> valuesList = JSON.parseArray(valueObj.toString(), Map.class);
             String sets = " set ";
             for (Map valueData : valuesList) {
                 if(!valueData.get("value").toString().trim().equals("") && !valueData.get("value").toString().trim().equals("undefined") && valueData.get("value") != null){
                     sets += valueData.get("name").toString()+"=";
                     if(valueData.get("type").toString().equals("string")){
                         sets += "'"+valueData.get("value").toString()+"',";
                     }
                     else if(valueData.get("type").toString().equals("num")){
                         sets += valueData.get("value").toString()+",";
                     }
                     else if(valueData.get("type").toString().equals("year")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY'),";
                     }
                     else if(valueData.get("type").toString().equals("month")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY-MM'),";
                     }
                     else if(valueData.get("type").toString().equals("date")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD'),";
                     }
                     else if(valueData.get("type").toString().equals("hour")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24'),";
                     }
                     else if(valueData.get("type").toString().equals("minute")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24:MI'),";
                     }
                     else if(valueData.get("type").toString().equals("second")){
                         sets += "to_date('"+valueData.get("value").toString()+"','YYYY-MM-DD HH24:MI:SS'),";
                     }
                    
                     else{
                         sets += "'"+valueData.get("value").toString()+"',";
                     }
                 }
             }
             sets = sets.substring(0,sets.length()-1);
             sql += sets + " where " + whereString;
             Map<String, String> sqlParam = new HashMap<String, String>();
             sqlParam.put(DAConstant.SQLNAME, sql);
             logger.info("执行的sql：" + sql);
             insertDataDao.daComUpdateData(sqlParam);
             return getSuccessReturnData("修改数据成功!");
         }
         catch (Exception e) {
             logger.info(e, e);
             return getFailReturnData(e.getMessage());
         }
	}
    
    @Override
    public String daComDelData(String data) throws Exception {
        try {        	
			Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
			JSONArray jsonArray = JSONArray.fromObject((String) params.get(DAConstant.WHERE));
			String sql = ParseSqlConfig.getSql(data);
			String wheres = "";
			for (Object object : jsonArray) {//组装批量删除where条件
				JSONObject jsonObject = (JSONObject) object;
				String where = "";
				for (Iterator iter = jsonObject.keys(); iter.hasNext();) {
					String key = (String) iter.next();
					if (where.equals("")) {
						where = key + "='" + jsonObject.get(key) + "'";
					} else {
						where += " and " + key + "='" + jsonObject.get(key) + "'";
					}
				}
				where = "(" + where + ")";
				if (wheres.equals("")) {
					wheres = where;
				} else {
					wheres += " or " + where;
				}
			}
        	
        	sql += " where " + wheres;        	 
            Map<String, String> sqlParam = new HashMap<String, String>();
            sqlParam.put(DAConstant.SQLNAME, sql);
            logger.info("执行的sql：" + sql);
            insertDataDao.daComDelData(sqlParam);
            return getSuccessReturnData("删除数据成功!");
        }
        catch (Exception e) {
            logger.info(e, e);
            return getFailReturnData(e.getMessage());
        }
   }

    @Override
    public String checkIsOnly(String data) throws Exception {
        String returnData = null;
        if (data != null && !"".equals(data.trim())) {
            Map<String, String> sqlParam = (Map<String, String>) JsonUtil.getMapFromJson(data);
            logger.info("执行的sql：" + sqlParam.get("sql"));
            int count = insertDataDao.checkIsOnly(sqlParam);
            if (count != 0) {
                returnData = getSuccessReturnData("该值已存在");
            }
            else {
                returnData = getFailReturnData("该值可以填写");
            }
        }
        return returnData;
    }

	public DAInsertDataDao getInsertDataDao() {
        return insertDataDao;
    }

    
    public void setInsertDataDao(DAInsertDataDao insertDataDao) {
        this.insertDataDao = insertDataDao;
    }

}
