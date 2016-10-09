package com.zjcds.da.service.detailData.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.log4j.Logger;
import com.zjcds.da.data.dao.DADetailDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.detailData.DADetailDataService;
import com.zjcds.da.service.insert.impl.DAInsertDataServiceImpl;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 详细信息实现类
 * @author xinyf
 * @date   2013-6-18
 */
public class DADetailDataServiceImpl extends DABaseService implements DADetailDataService {
	
	private DADetailDataDao detailDataDao;
	private Logger logger = Logger.getLogger(DAInsertDataServiceImpl.class);
	
	public String daGetDetailData(String data) throws Exception {
		/**
		Map<String,String> params = (Map<String, String>) JsonUtil.getMapFromJson(data);

		String fileName = params.get(DAConstant.ID);
		
		String itemId = params.get(DAConstant.ITEMID);
		String typeId = params.get(DAConstant.TYPEID);
		if(fileName==null||itemId==null){
			return getFailReturnData(DAConstant.ID+"或"+DAConstant.ITEMID+"为空！");
		}
		
		//读取json配置文件，并加载信息
		String configStr = ReadFile.getFileContent(fileName);
		JSONObject config = JSONObject.fromObject(configStr);
		JSONArray bodysValue = config.getJSONArray(DAConstant.BODYS);

		boolean hasAllBody = false;		//是否需要返回allBody
		if (typeId == null || typeId.equals("")) {//无tabId表示头一次加载，需要allBody数据
			hasAllBody = true;
			typeId = bodysValue.getJSONObject(0).getString(DAConstant.TYPEID);//默认获取第一个Tab的内容
			params.put(DAConstant.TYPEID, typeId);
		}
		JSONObject typeConfig = getTypeConfig(typeId, bodysValue);
		JSONArray columnValue = typeConfig.getJSONArray(DAConstant.COLUMNS);//getColumnsOrder(typeId, bodysValue);//列顺序
		
		String sqlKey = null;
		try{
			sqlKey = typeConfig.getString(DAConstant.SQLKEY);	//获取对于的sqlId
		}catch(Exception e){
			return getFailReturnData("没有对应的sqlKey！");
		}

		//获取动态sql并执行
		String sql = ParseSqlConfig.getSql(params,sqlKey);
		Map<String, String> sqlParam = new HashMap<String, String>();
		sqlParam.put(DAConstant.SQLNAME, sql);
		Map<String,Object> bodyMap = detailDataDao.daGetDetailData(sqlParam);
		
		JSONArray dataArr = jointDataArr(bodysValue, hasAllBody, columnValue, bodyMap);
		**/
		Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        String sql = ParseSqlConfig.getSql(data);
        String querySql = getAppendSqlWhereEquals(params, sql);
        //String querySql = getAppendSqlWhere(params, sql);
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("sql", querySql);
        logger.info("执行的sql：" + querySql);
        Map<String,Object> detailData = detailDataDao.daGetDetailData(sqlMap);
		return getSuccessReturnData(detailData);
		
	}
	
	@Override
    public String querySql(String data) throws Exception {
        String returnData = null;
        if (data != null && !"".equals(data.trim())) {
            Map<String, String> sqlParam = (Map<String, String>) JsonUtil.getMapFromJson(data);
            logger.info("执行的sql：" + sqlParam.get("sql"));
            List<Map<String,Object>> queryData = detailDataDao.querySql(sqlParam);
            return getSuccessReturnData(queryData);
        }
        return returnData;
    }

	/**
	 * 将结果信息拼接为dataArr
	 * @param bodysValue allBody所需信息
	 * @param hasAllBody 是否包含allBody
	 * @param columnValue 列顺序信息
	 * @param bodyMap    详细信息
	 * @return
	 */
/*	private JSONArray jointDataArr(JSONArray bodysValue, boolean hasAllBody, JSONArray columnValue, Map<String, Object> bodyMap) {
		//拼接字符串返回
		JSONArray dataArr = new JSONArray();
		JSONObject body = new JSONObject();
		body.put(DAConstant.BODY, JSONArray.fromObject(rankColumns(columnValue, bodyMap)));
		if(hasAllBody){ //判断是否放入allBody
			dataArr.add(getAllBody(bodysValue));
		}
		dataArr.add(body);//放入body，即详细信息
		return dataArr;
	}*/

	/**
	 * 拼接获取allBody
	 * @param bodysValue
	 * @return
	 */
/*	private JSONObject getAllBody(JSONArray bodysValue) {
		bodysValue = getBodysValue(bodysValue);
		JSONObject allBody = new JSONObject();
		JSONObject allBodyValue = new JSONObject(); //allBody的值
		allBodyValue.put(DAConstant.NUMBER, bodysValue.size());
		allBodyValue.put(DAConstant.BODYS, bodysValue);
		allBody.put(DAConstant.ALLBODY, allBodyValue);
		return allBody;
	}*/
	
	/**
	 * 找到指定typeId对于的配置信息
	 * @param typeId
	 * @param bodysValue
	 * @return
	 */
/*	private JSONObject getTypeConfig(String typeId, JSONArray bodysValue) {
		JSONObject typeConfig = null;
		//遍历获取指定typeid的列顺序
		for (Iterator<?> i = bodysValue.iterator(); i.hasNext();) {
			JSONObject obj = (JSONObject) i.next();
			if(typeId.equals(obj.getString(DAConstant.TYPEID))){
				typeConfig = obj;
				break;
			}
		}
		return typeConfig;
	}*/
	
	/**
	 * 由于前台bodys只需要name和typeId，所以提取出只包含name和typeId的bodysValue
	 * @param bodysValue
	 * @return
	 */
/*	private JSONArray getBodysValue(JSONArray bodysValue){
		JSONArray _bodysValue = new JSONArray();
		for (Iterator<?> i = bodysValue.iterator(); i.hasNext();) {
			JSONObject object = (JSONObject) i.next();
			JSONObject _object = new JSONObject();
			_object.put(DAConstant.NAME, object.get(DAConstant.NAME));
			_object.put(DAConstant.TYPEID, object.get(DAConstant.TYPEID));
			_bodysValue.add(_object);
		}
		return _bodysValue;
	}*/

	/**
	 * 按照指定的列顺序对数据进行排序处理存放
	 * @param columnValue
	 * @param bodyMap
	 * @return
	 */
/*	private List<Map<String, Object>> rankColumns(JSONArray columnValue, Map<String, Object> bodyMap) {
		List<Map<String, Object>> bodyList = new ArrayList<Map<String, Object>>();
		//遍历获得指定显示的顺序
		for (Iterator<?> i = columnValue.iterator(); i.hasNext();) {
			JSONObject column = (JSONObject) i.next();
			String text = column.getString(DAConstant.TEXT);
			String columnName = column.getString(DAConstant.COLUMNNAME);
			Map<String,Object> map = new HashMap<String, Object>();
			map.put(DAConstant.NAME, text);
			map.put(DAConstant.VALUE, bodyMap.get(columnName));
			bodyList.add(map);
		}
		return bodyList;
	}*/
	
	public void setDetailDataDao(DADetailDataDao detailDataDao) {
		this.detailDataDao = detailDataDao;
	}
	public DADetailDataDao getDetailDataDao() {
		return detailDataDao;
	}
}
