package com.zjcds.da.action;


import com.zjcds.da.service.kpi.DAKPIDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 指标板数据动作类
 * 
 * @author yuzq
 * 
 */
public class DAKPIDataAction extends PortalBaseAction {

    /**
     * 
     */
    private static final long serialVersionUID = 1L;

    private String data; // 前端请求参数

    private DAKPIDataService kpiDataService;

    @Override
    public String doExecute() throws Exception {
        return null;
    }


    /**
     * 查询表格数据
     * 
     * @return
     * @throws Exception
     */
    public String daKPIData() throws Exception {
        super.writeJson(kpiDataService.daKPIData(data));
        return null;
    }
    
    /**
     * 查询案件未处理数
     * @return
     */
    public String daKPIDataAj() throws Exception {
    	super.writeJson(kpiDataService.daKPIDataAj(data));
    	return null;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }


    
    public DAKPIDataService getKpiDataService() {
        return kpiDataService;
    }


    
    public void setKpiDataService(DAKPIDataService kpiDataService) {
        this.kpiDataService = kpiDataService;
    }


}
