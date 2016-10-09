package com.zjcds.da.action;

import com.zjcds.da.service.charts.DAChartsDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 获取数据服务Action
 * @author xinyf
 * @date   2013-6-20
 */
public class DAChartsDataAction extends PortalBaseAction {

	private static final long serialVersionUID = 1L;
    /**
     * 前台传递过来的json字符串
     */
	private String data;
	
	private DAChartsDataService chartsDataService;
	

	/**
	 * 返回数据服务信息
	 * @return
	 * @throws Exception 
	 */
	public String daGetData() throws Exception{
		String json = chartsDataService.daGetData(data);
		writeJson(json);
		return null;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
	
	@Override
	public String doExecute() throws Exception {
		return null;
	}

	public void setChartsDataService(DAChartsDataService chartsDataService) {
		this.chartsDataService = chartsDataService;
	}

	public DAChartsDataService getChartsDataService() {
		return chartsDataService;
	}

}
