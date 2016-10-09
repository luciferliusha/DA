package com.zjcds.da.service.export;

import javax.servlet.http.HttpServletResponse;

/**
 * 导出
 * 
 * @author yuzq created on 2013-10-31
 * @since CDS Framework 1.0
 */
public interface DAExportDataService {

    public String daExportData(String data,HttpServletResponse response) throws Exception;
}
