package com.zjcds.da.service.kpi;


/**
 * 表格数据服务接口
 * 
 * @author yuzq
 * 
 */
public interface DAKPIDataService {

    /**
     * 查询数据
     * 
     * @param data
     * @return
     * @throws Exception
     */
    String daKPIData(String data) throws Exception;
    
    String daKPIDataAj(String data) throws Exception;

}
