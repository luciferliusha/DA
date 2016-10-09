package com.zjcds.da.data.dao;

import java.util.List;
import java.util.Map;

public interface DAChartsDataDao {
    List<Map<String, Object>> daGetData(Map<String,String> sql);
}
