package com.zjcds.da.action;

import com.zjcds.da.service.export.DAExportDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 导出Action
 * 
 * @author yuzq created on 2013-10-31
 * @since CDS Framework 1.0
 */
public class DAExportDataAction extends PortalBaseAction {

    /**  */
    private static final long serialVersionUID = 1L;

    private DAExportDataService exportService;

    private String data;

    @Override
    public String doExecute() throws Exception {
        return null;
    }

    /**
     * 导出Excel
     * 
     * @return
     * @throws Exception
     * @author yuzq created on 2013-10-31
     * @since CDS Framework 1.0
     */
    public String daExportData() throws Exception {
        exportService.daExportData(data,response);
//        String returnData = exportService.daExportData(data,response);
//        super.writeJson(returnData);
        return null;
    }

    public DAExportDataService getExportService() {
        return exportService;
    }

    public void setExportService(DAExportDataService exportService) {
        this.exportService = exportService;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
