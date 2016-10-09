package com.zjcds.da.data.dao;

import java.util.Map;

public interface DAInsertDataDao {
    int daInsertData(Map<String,String> sql);
    
    int daUpdateData(Map<String,String> sql);
    
    int daComInsertData(Map<String,String> sql);
    
    int daComUpdateData(Map<String,String> sql);
    
    int daComDelData(Map<String,String> sql);
    
    int checkIsOnly(Map<String,String> sql);
}
