package com.zjcds.da.service.insert;

/**
 * 插入数据Service接口
 * 
 * @author linj
 * @date 2013-6-20
 */
public interface DAInsertDataService {

    String daInsertData(String data) throws Exception;
    
    String daUpdateData(String data) throws Exception;

    String daComInsertData(String data) throws Exception;
    
    String daComUpdateData(String data) throws Exception;
    
    String daComDelData(String data) throws Exception;
    
    String checkIsOnly(String data) throws Exception;
}
