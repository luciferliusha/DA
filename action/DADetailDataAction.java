package com.zjcds.da.action;

import com.zjcds.da.service.detailData.DADetailDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 详细信息Action
 * @author xinyf
 * @date   2013-6-18
 */
public class DADetailDataAction extends PortalBaseAction{
	
    /**
     * 前台传递过来的json字符串
     */
	private String data;
	
	private DADetailDataService detailDataService;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 获取详细信息并返回json
	 * @return
	 * @throws Exception 
	 */
	public String daGetDetailData() throws Exception{
		
		String json = detailDataService.daGetDetailData(data);
		writeJson(json);
		
		return null;
	}
	
	/**
     * 执行相应的SQL并返回数据
     * @return
     * @throws Exception 
     */
    public String querySql() throws Exception{
        String json = detailDataService.querySql(data);
        writeJson(json);
        return null;
    }

	public void setData(String data) {
		this.data = data;
	}

	public String getData() {
		return data;
	}

	@Override
	public String doExecute() throws Exception {
		return null;
	}

	public void setDetailDataService(DADetailDataService detailDataService) {
		this.detailDataService = detailDataService;
	}

	public DADetailDataService getDetailDataService() {
		return detailDataService;
	}
}
