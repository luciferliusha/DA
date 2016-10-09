package com.zjcds.da.view.bean;

import java.util.List;
import java.util.Map;

/**
 * excel信息类
 * 
 * @author yuzq created on 2013-11-1
 * @since CDS Framework 1.0
 */
public class ExcelInfo {

    private List<String> fields;

    private List<String> titles;

    private List<Integer> widths;

    private String excelName;

    private int sheetCount; // 包含sheet数目

    private int excelRows; // excel中记录数

    private int eachSheetShowRows; // excel中的每个sheet显示记录条数

    private List<Map<String, Object>> excelData; // excel数据

    private int currentSheetNo; // 当前sheet编号

    public List<String> getFields() {
        return fields;
    }

    public void setFields(List<String> fields) {
        this.fields = fields;
    }

    public List<String> getTitles() {
        return titles;
    }

    public void setTitles(List<String> titles) {
        this.titles = titles;
    }

    public String getExcelName() {
        return excelName;
    }

    public void setExcelName(String excelName) {
        this.excelName = excelName;
    }

    public List<Integer> getWidths() {
        return widths;
    }

    public void setWidths(List<Integer> widths) {
        this.widths = widths;
    }

    public int getSheetCount() {
        return sheetCount;
    }

    public void setSheetCount(int sheetCount) {
        this.sheetCount = sheetCount;
    }

    public int getExcelRows() {
        return excelRows;
    }

    public void setExcelRows(int excelRows) {
        this.excelRows = excelRows;
    }

    public int getEachSheetShowRows() {
        return eachSheetShowRows;
    }

    public void setEachSheetShowRows(int eachSheetShowRows) {
        this.eachSheetShowRows = eachSheetShowRows;
    }

    public List<Map<String, Object>> getExcelData() {
        return excelData;
    }

    public void setExcelData(List<Map<String, Object>> excelData) {
        this.excelData = excelData;
    }

    public int getCurrentSheetNo() {
        return currentSheetNo;
    }

    public void setCurrentSheetNo(int currentSheetNo) {
        this.currentSheetNo = currentSheetNo;
    }

}
