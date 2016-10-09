package com.zjcds.da.service.export.impl;

import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.ObjectUtils;
import org.apache.log4j.Logger;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;
import com.cds.framework.dao.paging.PageInfo;
import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.data.dao.DATableDataDao;
import com.zjcds.da.service.export.DAExportDataService;
import com.zjcds.da.service.table.impl.DATableDataServiceImpl;
import com.zjcds.da.util.ParseSqlConfig;
import com.zjcds.da.view.bean.ExcelInfo;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 导出service实现
 * 
 * @author yuzq created on 2013-10-31
 * @since CDS Framework 1.0
 */
public class DAExportDataServiceImpl extends DATableDataServiceImpl implements DAExportDataService {

    private DATableDataDao tableDataDao;

    private Logger logger = Logger.getLogger(DAExportDataServiceImpl.class);

    /*
     * @see com.zjcds.da.service.export.DAExportDataService#daExportData(java.lang.String)
     */
    @Override
    public String daExportData(String data, HttpServletResponse response) throws Exception {
        String returnData = null;
        if (data != null && !"".equals(data.trim())) {
            long beginTime = System.currentTimeMillis();
            ExcelInfo excelInfo = getExcelHeads(data);
            long readFileFinish = System.currentTimeMillis();
            logger.info("读取json文件耗时:" + (readFileFinish - beginTime) + "ms");
            List<Map<String, Object>> excelData = getExcelData(data);
            long getDataFinish = System.currentTimeMillis();
            logger.info("获取数据耗时:" + (getDataFinish - readFileFinish) + "ms");
            writeExcel(excelInfo, excelData, response);
            logger.info("写出excel数据耗时:" + (System.currentTimeMillis() - getDataFinish) + "ms");
            returnData = super.getSuccessReturnData("导出成功!");
        }
        else {
            returnData = super.getFailReturnData("导出失败!");
        }
        return returnData;
    }

    /*** 取得Excel头信息 */
    private ExcelInfo getExcelHeads(String data) throws Exception {
        ExcelInfo excelInfo = new ExcelInfo();
        JSONObject bodyNodeValue = getJsonFileBodyValue(data);
        String excelName = getExcelName(bodyNodeValue);
        List<Map<String, Object>> excleColumnInfo = getExcelColumnInfo(bodyNodeValue);
        getExcelName(excelInfo, excelName);
        getExcelHeadNameAndWidth(excelInfo, excleColumnInfo);
        return excelInfo;
    }

    /*** 取得json文件body节点的value值 */
    private JSONObject getJsonFileBodyValue(String data) throws Exception {
        String jsonFileStr = super.getJsonFileContent(data);
        JSONObject json = JSONObject.fromObject(jsonFileStr);
        JSONArray dataNodeArray = json.getJSONArray(DAConstant.JSON_FILE_DATA);
        JSONObject bodyNode = (JSONObject) dataNodeArray.get(DAConstant.BODY_NODE_INDEX_IN_JSON_FILE);
        JSONObject bodyNodeValue = bodyNode.getJSONObject(DAConstant.JSON_FILE_DATA_BODY);
        return bodyNodeValue;
    }

    /*** 取得excel表格名称 */
    private String getExcelName(JSONObject bodyNodeValue) throws Exception {
        String excelName = null;
        if (bodyNodeValue.containsKey(DAConstant.JSON_FILE_DATA_BODY_EXCELNAME))
            excelName = bodyNodeValue.getString(DAConstant.JSON_FILE_DATA_BODY_EXCELNAME);
        return excelName;
    }

    /** 取得excel列信息 */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getExcelColumnInfo(JSONObject bodyNodeValue) throws Exception {
        String bodyDataNode = bodyNodeValue.getString(DAConstant.JSON_FILE_DATA_BODY_DATA);
        String bodyDataToJson = bodyDataNode.substring(1, bodyDataNode.length() - 1);
        List<Map<String, Object>> list = (List<Map<String, Object>>) JsonUtil.getDTOList(bodyDataToJson, Map.class);
        return list;
    }

    /** 获取excel名称 */
    private void getExcelName(ExcelInfo excelInfo, String excelName) throws Exception {
        if (excelName != null && !"".equals(excelName.trim())) {
            excelInfo.setExcelName(excelName);
        }
        else {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
            excelInfo.setExcelName(sdf.format(new Date()));
        }
    }

    /*** 获取excel表头名称,宽度 */
    private void getExcelHeadNameAndWidth(ExcelInfo excelInfo, List<Map<String, Object>> list) throws Exception {
        List<String> titleList = new ArrayList<String>();
        List<String> fields = new ArrayList<String>();
        List<Integer> widths = new ArrayList<Integer>();
        Integer formalWidth = DAConstant.EXCEL_DEFAULT_WIDTH;
        for (Map<String, Object> map : list) {
            titleList.add((String) map.get(DAConstant.JSON_FILE_DATA_BODY_DATA_TITLE));
            fields.add((String) map.get(DAConstant.JSON_FILE_DATA_BODY_DATA_FIELD));
            if (map.containsKey(DAConstant.JSON_FILE_DATA_BODY_DATA_WIDTH))
                formalWidth = (Integer) map.get(DAConstant.JSON_FILE_DATA_BODY_DATA_WIDTH);
            widths.add(formalWidth);

        }
        excelInfo.setTitles(titleList);
        excelInfo.setFields(fields);
        excelInfo.setWidths(widths);
    }

    /*** 取得Excel数据 */
    @SuppressWarnings("unchecked")
    private List<Map<String, Object>> getExcelData(String data) throws Exception {
        Map<String, Object> params = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        String configSQL = ParseSqlConfig.getSqlById(String.valueOf(params.get(DAConstant.SQL_CONFIG_ID)));
        String sql = this.getQuerySql(params, configSQL, null);
        sql = sql.replace("\\\\", "\\");
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("dataSql", sql);
        List<Map<String, Object>> result = tableDataDao.daQueryData(sqlMap);
        return result;
    }

    /*** 写出Excel数据 */
    private void writeExcel(ExcelInfo excelInfo, List<Map<String, Object>> excelData, HttpServletResponse response)
            throws Exception {
        setResponseHead(response);
        setExcelName(excelInfo, response);
        setOtherExcelInfo(excelInfo, excelData);
        writeLogic(response, excelInfo);
        response.getOutputStream().flush();
        response.getOutputStream().close();
    }

    /** 设置response信息 */
    private void setResponseHead(HttpServletResponse response) throws Exception {
        response.reset();// 清空输出流
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/vnd.ms-excel;charset=utf-8");// 以防乱码
    }

    /*** 创建excel对象 */
    private WritableWorkbook createWorkbook(HttpServletResponse response) throws Exception {
        OutputStream os = response.getOutputStream();// 取得输出流
        WritableWorkbook wwb = Workbook.createWorkbook(os); // 建立excel文件
        return wwb;
    }

    /** 设置excel名称 */
    private void setExcelName(ExcelInfo excelInfo, HttpServletResponse response) throws Exception {
        String excelName = URLEncoder.encode(excelInfo.getExcelName(), "UTF-8");// 弹窗关键代码 "Excel.xls"
        response.setHeader("Content-Disposition", "attachment;filename=" + excelName + ".xls");// 通过模版创建workbook
    }

    /** 设置excel其他信息(excel中记录数,每一个sheet显示的记录数,一共多少个sheet) */
    private void setOtherExcelInfo(ExcelInfo excelInfo, List<Map<String, Object>> excelData) throws Exception {
        int excelRows = excelData.size();
        int eachSheetShowRows = DAConstant.EACH_SHEET_SHOW_ROWS_COUNT;
        int sheets = (excelRows + eachSheetShowRows - 1) / eachSheetShowRows;
        if (sheets == 0) {
            sheets = 1;
        }
        excelInfo.setExcelRows(excelRows);
        excelInfo.setEachSheetShowRows(eachSheetShowRows);
        excelInfo.setSheetCount(sheets);
        excelInfo.setExcelData(excelData);
    }

    /*** excel写出逻辑 */
    private void writeLogic(HttpServletResponse response, ExcelInfo excelInfo) throws Exception {
        WritableWorkbook wwb = createWorkbook(response); // 建立excel文件
        for (int sheetNo = 0; sheetNo < excelInfo.getSheetCount(); sheetNo++) {
            WritableSheet ws = wwb.createSheet("第" + (sheetNo + 1) + "页", sheetNo); // 创建一个工作表
            writeHead(ws, excelInfo);
            excelInfo.setCurrentSheetNo(sheetNo);
            if (checkCurrentSheetIsLastOne(excelInfo)) {
                lastSheetWriteLogic(excelInfo, ws);
            }
            else {
                commonSheetWriteLogic(excelInfo, ws);
            }
        }
        wwb.write();
        wwb.close();
    }

    /*** 判断当前sheet是否为最后一个 */
    private boolean checkCurrentSheetIsLastOne(ExcelInfo excelInfo) throws Exception {
        return (excelInfo.getCurrentSheetNo() + 1) == excelInfo.getSheetCount();
    }

    /*** 最后一个sheet的处理逻辑 */
    private void lastSheetWriteLogic(ExcelInfo excelInfo, WritableSheet ws) throws Exception {
        List<String> colFields = excelInfo.getFields();
        int excelRow = 1;
        int currentSheetNo = excelInfo.getCurrentSheetNo();
        for (int currentRow = currentSheetNo * excelInfo.getEachSheetShowRows(); currentRow < excelInfo.getExcelRows(); currentRow++) {
            int excelCol = 0;
            List<Map<String, Object>> excelData = excelInfo.getExcelData();
            Map<String, Object> currentMap = excelData.get(currentRow);
            for (String colName : colFields) {
                String data = ObjectUtils.toString(currentMap.get(colName), "");
                Label label = new Label(excelCol, excelRow, data);
                ws.addCell(label);
                excelCol++;
            }
            excelRow++;
        }
    }

    /*** 一般的sheet的处理逻辑 */
    private void commonSheetWriteLogic(ExcelInfo excelInfo, WritableSheet ws) throws Exception {
        List<String> colFields = excelInfo.getFields();
        for (int currentRow = 1; currentRow <= excelInfo.getEachSheetShowRows(); currentRow++) {
            int currentCol = 0;
            int currentSheetNo = excelInfo.getCurrentSheetNo();
            List<Map<String, Object>> excelData = excelInfo.getExcelData();
            Map<String, Object> currentMap = excelData.get(excelInfo.getEachSheetShowRows() * currentSheetNo
                    + currentRow - 1);
            for (String colName : colFields) {
                String data = ObjectUtils.toString(currentMap.get(colName), "");   
                Label label = new Label(currentCol, currentRow, data);
                ws.addCell(label);
                currentCol++;
            }
        }
    }

    /*** 写头信息 */
    private void writeHead(WritableSheet ws, ExcelInfo excelInfo) throws Exception {
        List<String> heads = excelInfo.getTitles();
        List<Integer> widths = excelInfo.getWidths();
        for (int i = 0; i < heads.size(); i++) {
            Label label = new Label(i, 0, String.valueOf(heads.get(i)));
            ws.setColumnView(i, changUnit(widths.get(i)));
            ws.addCell(label);
        }
    }

    /*** 宽度转换: 将px转换为excel单位 */
    private Integer changUnit(Integer width) throws Exception {
        int excelWidth = (int) ((int) width * 0.35) / 2;
        return excelWidth;
    }

    protected String getQuerySql(Map<String, Object> params, String sql, PageInfo pageInfo) throws Exception {
        sql = appendQueryConditionSql(params, sql);
        sql = appendGroupBySql(params, sql);
        sql = appendOrderBySql(params, sql);
        // sql = appendRownumSql(sql);
        sql = appendTopCondition(params, sql);
        return sql;
    }

    public DATableDataDao getTableDataDao() {
        return tableDataDao;
    }

    public void setTableDataDao(DATableDataDao tableDataDao) {
        this.tableDataDao = tableDataDao;
    }

}
