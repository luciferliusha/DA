package com.zjcds.da.service.charts.impl;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.data.dao.DAChartsDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.charts.DAChartsDataService;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.da.util.ReadFile;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 获取数据服务Service实现类
 * 
 * @author xinyf
 * @date 2013-6-20
 */
public class DAChartsDataServiceImpl extends DABaseService implements DAChartsDataService {

    private DAChartsDataDao chartsDataDao;

    private Logger logger = Logger.getLogger(DAChartsDataServiceImpl.class);

    @Override
    public String daGetData(String data) throws Exception {
        try {
            // json解析配置信息
            Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
            String fileName = (String) params.get(DAConstant.ID);
            String configStr = ReadFile.getFileContent(fileName);
            JSONObject dataConfig = JSONObject.fromObject(configStr);
            JSONObject chartConfig = dataConfig.getJSONArray(DAConstant.DATA).getJSONObject(DAConstant.CHARTS_INDEX);
            // 执行sql
            String sql = ParseSqlConfig.getSql(data);
            sql = getAppendSqlWhere(params, sql);
            sql = addGroupBy(params, sql);
            sql = addSort(params, sql);
            sql = sql.replace("\\\\", "\\");
            Map<String, String> sqlParam = new HashMap<String, String>();
            sqlParam.put(DAConstant.SQLNAME, sql);
            logger.info("执行的sql：" + sql);
            List<Map<String, Object>> seriesData = getChartsDataDao().daGetData(sqlParam);
            // 根据配置转换数据
            chartConfig = parseRecord(seriesData, chartConfig, params);
            return getSuccessReturnData(chartConfig);
        }
        catch (Exception e) {
            logger.info(e, e);
            return getFailReturnData(e.getMessage());
        }
    }

   
    /**
     * 添加group by
     * 
     * @param params
     * @param sql
     * @return
     */
    private String addGroupBy(Map<String, Object> params, String sql) {
        String groupBy = (String) params.get("groupBy");
        if (groupBy != null && !"".equals(groupBy.trim())) {
            return sql + " group by " + groupBy;
        }
        return sql;
    }

    /**
     * 添加排序
     * 
     * @param params
     * @param sql
     * @return
     */
    private String addSort(Map<String, Object> params, String sql) {
        String sort = (String) params.get("sort");
        if (sort != null && !"".equals(sort.trim())) {
            return sql + " order by " + sort;
        }
        return sql;
    }

    /**
     * 将数据解析为highcharts能识别的格式
     * 
     * @param seriesData
     * @param chartConfig
     * @return
     */
    private JSONObject parseRecord(List<Map<String, Object>> seriesData, JSONObject chartConfig,
            Map<String, Object> params) {
        JSONArray seriesValue = chartConfig.getJSONArray(DAConstant.SERIES);
        JSONObject columnMapping = chartConfig.getJSONObject(DAConstant.COLUMNMAPPING);
        String xAxisName = (String) columnMapping.get(DAConstant.XAXIS);// xAxis对于的字段名
        String xAxisSuffix = (String) columnMapping.get(DAConstant.XAXIS_SUFFIX);// xAxis后缀
        seriesValue = parseSeriesName(params, seriesValue, columnMapping);
        chartConfig.remove(DAConstant.COLUMNMAPPING);// 移除映射信息
        
        //toolTip
        JSONArray toolTip = null;
        JSONArray toolTipValue = null;
        if(chartConfig.get(DAConstant.TOOLTIP) != null){
        	toolTip = chartConfig.getJSONArray(DAConstant.TOOLTIP);
        }
        if(chartConfig.get(DAConstant.TOOLTIPVALUE) != null){
        	toolTipValue = chartConfig.getJSONArray(DAConstant.TOOLTIPVALUE);
        }
        
        JSONArray xHiddenAxis = null;//X轴隐藏的数据，方面后续比如钻取用
        if (chartConfig.containsKey("xHiddenAxis")) {
            xHiddenAxis = chartConfig.getJSONArray("xHiddenAxis");
        }
        
        // 遍历数据并转换
        int singleSeriesIndex = 0;//用来标识柱状图只有一组数据时，每条数据的位置
        for (Iterator<Map<String, Object>> iterator = seriesData.iterator(); iterator.hasNext();) {
            Map<String, Object> item = iterator.next();// 获取一条记录
            // 给xAxis添加数据
            if (xAxisName != null) {
                JSONArray xCategories = chartConfig.getJSONArray(DAConstant.XAXIS).getJSONObject(0).getJSONArray(
                        DAConstant.CATEGORIES);
                String xAxis =String.valueOf( item.get(xAxisName));
                if (xAxisSuffix != null) {
                    xAxis = xAxis + xAxisSuffix;
                }
                xCategories.add(xAxis);// 转换为字符串处理
            }
            for (int i = 0; i < seriesValue.size(); i++) {// 遍历放入记录
                seriesValue = parseRecordToData(seriesValue, item, singleSeriesIndex, i, columnMapping);
            }
            if(toolTipValue != null){
            	parseToolTipValueToData(toolTipValue, item, toolTip);
            }
            if (xHiddenAxis != null) {
                parseToolTipValueToData(xHiddenAxis, item, null);
            }
            singleSeriesIndex++;
        }
        return chartConfig;
    }

    /**
     * 根据配置文件给series的name进行动态操作
     * 
     * @param params
     * @param seriesValue
     * @param columnMapping
     * @return
     */
    private JSONArray parseSeriesName(Map<String, Object> params, JSONArray seriesValue, JSONObject columnMapping) {
        JSONArray namePrefix = (JSONArray) columnMapping.get(DAConstant.NAME_PREFIX);
        JSONObject where = JSONObject.fromObject(params.get("where"));/**/
        if (namePrefix != null && namePrefix.size() > 0 && !where.isNullObject()) {
            for (int i = 0; i < namePrefix.size(); i++) {
                JSONObject prefix = (JSONObject) namePrefix.get(i);
                if (prefix != null && !prefix.isNullObject() && !prefix.isEmpty()
                        && prefix.containsKey(DAConstant.NAME_PREFIX_TYPE)) {
                    if ("minus".equals(prefix.getString(DAConstant.NAME_PREFIX_TYPE))) {
                        // 从where参数中获取指定参数进行动态操作
                        int param = Integer.parseInt(where
                                .getJSONObject(prefix.getString(DAConstant.NAME_PREFIX_PARAM)).getString("data"));
                        int value = prefix.getInt(DAConstant.NAME_PREFIX_VALUE);
                        int result = param - value;
                        JSONObject seriesItem = seriesValue.getJSONObject(i);
                        seriesItem.put(DAConstant.SERIES_NAME, result + seriesItem.getString(DAConstant.SERIES_NAME));
                    }
                }
            }
        }
        return seriesValue;
    }

    /**
     * 解析一条数据解析到highcharts的data中
     * 
     * @param seriesValue 放入到的目标
     * @param item 数据
     * @param singleSeriesIndex 标识柱状图只有一组数据时，每条数据的位置
     * @param i 数据索引
     * @param columnMapping 相关配置信息
     */
    private JSONArray parseRecordToData(JSONArray seriesValue, Map<String, Object> item,int singleSeriesIndex, int i, JSONObject columnMapping) {
        JSONArray dataColumnValue = columnMapping.getJSONArray(DAConstant.DATACOLUMN);// data中对应的字段名数组
        JSONArray dataColumnColor = null;
        if (columnMapping.containsKey(DAConstant.DATACOLUMN_COLOR)) {
            dataColumnColor = columnMapping.getJSONArray(DAConstant.DATACOLUMN_COLOR);// data中对应的颜色数组
        }
        String labelSuffix = (String) columnMapping.get(DAConstant.LABEL_SUFFIX);
        JSONObject seriesItem = seriesValue.getJSONObject(i);// 获取一个图形
        String itemType = (String) seriesItem.get(DAConstant.TYPE);// 获取类型
        JSONArray itemData = seriesItem.getJSONArray(DAConstant.DATA);// 获取存放数据的对象
        if ("pie".equals(itemType)) { // 饼状图特殊处理
            JSONArray one = new JSONArray();
            for (Iterator<?> k = dataColumnValue.iterator(); k.hasNext();) {
                String columnName = (String) k.next();
                one.add(item.get(columnName));
            }
            String label = one.get(0).toString();
            if (labelSuffix != null) {
                label = label + labelSuffix;
            }
            one.set(0, label);// 第一个默认是标示符，作为字符串
            itemData.add(one);
        }else if("scatter".equals(itemType)){//散点图，目前仅支持一组散点，当为组合图时，在json文件的"series"属性中应配置在最后
        	JSONArray scatterArray = new JSONArray();
        	scatterArray.add(item.get(dataColumnValue.get(i)));
        	scatterArray.add(item.get(dataColumnValue.get(i+1)));
        	itemData.add(scatterArray);	
        }
        else {// 柱状和折线图
        	if("column".equals(itemType) && seriesValue.size() == 1){//柱状图，并且只有一组柱状图
        		String chart = "";
        		JSONObject chartObject = new JSONObject();
        		JSONArray colors = null;
        		try {
        			chart = JsonUtil.getJSONString(ReadFile.getAllFileContent(DAConstant.CHART_COLOR_JSON));
        			chartObject = JSONObject.fromObject(chart);
        			colors = chartObject.getJSONArray(DAConstant.COLOR);
				} catch (Exception e) {
					e.printStackTrace();
				}
        		
        		JSONObject seriesDataObject = new JSONObject();
        		seriesDataObject.put(DAConstant.Y, item.get(dataColumnValue.get(i)));
        		if(colors == null){
        			seriesDataObject.put(DAConstant.COLOR, DAConstant.CHART_DEFAULT_COLOR);
        		}
        		else{
        			seriesDataObject.put(DAConstant.COLOR, colors.get(singleSeriesIndex%colors.size()));
        		}
        		itemData.add(seriesDataObject);
        	}
        	else{
        	    if (dataColumnColor != null && dataColumnColor.size() > 0) {
        	        JSONObject seriesDataObject = new JSONObject();
                    seriesDataObject.put(DAConstant.Y, item.get(dataColumnValue.get(i)));
                    if(i < dataColumnColor.size()){
                        seriesDataObject.put(DAConstant.COLOR, dataColumnColor.get(i));
                    }
                    itemData.add(seriesDataObject);
        	    }
        	    else {
        	        itemData.add(item.get(dataColumnValue.get(i)));
        	    }
        	}
        }
        return seriesValue;
    }

    /**
     * 解析一条数据解析到highcharts的toolTipValue中
     * 
     * @param seriesValue 放入到的目标
     * @param item 数据
     * @param i 数据索引
     * @param columnMapping 相关配置信息
     */
    private void parseToolTipValueToData(JSONArray toolTipValue, Map<String, Object> item, JSONArray toolTip) {
    	
    	int len = toolTipValue.size();
    	String key = "";
        for(int i=0; i<len; i++){
        	key = (String)toolTipValue.getJSONObject(i).get(DAConstant.TOOLTIPVALUE_NAME);
        	toolTipValue.getJSONObject(i).getJSONArray(DAConstant.TOOLTIPVALUE_DATA).add(item.get(key));
        }
    }
    
    public void setChartsDataDao(DAChartsDataDao chartsDataDao) {
        this.chartsDataDao = chartsDataDao;
    }

    public DAChartsDataDao getChartsDataDao() {
        return chartsDataDao;
    }
}
