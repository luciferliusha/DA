package com.zjcds.da.action;

import com.zjcds.da.service.insert.DAInsertDataService;
import com.zjcds.framework.action.PortalBaseAction;

/**
 * 插入数据库Action
 * @author xinyf
 * @date   2013-6-18
 */
public class DAInsertDataAction extends PortalBaseAction{
	
    /**
     * 前台传递过来的json字符串
     */
	private String data;
	
	private DAInsertDataService insertlDataService;

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1L;
	
	/**
	 * 插入数据
	 * @return
	 * @throws Exception
	 */
	public String daInsertData() throws Exception{
		
		String result = insertlDataService.daInsertData(data);
		writeJson(result);
		
		return null;
	}

	/**
	 * 更新数据
	 * @return
	 * @throws Exception
	 */
	public String daUpdateData() throws Exception{
		
		String result = insertlDataService.daUpdateData(data);
		writeJson(result);
		
		return null;
	}
	
	/**
	 * 根据配置文件中配置插入数据
	 * @return
	 * @throws Exception
	 */
	public String daComInsertData() throws Exception{
		String result = insertlDataService.daComInsertData(data);
		writeJson(result);
		return null;
	}

	/**
	 * 根据配置文件中配置更新数据
	 * @return
	 * @throws Exception
	 */
	public String daComUpdateData() throws Exception{
		String result = insertlDataService.daComUpdateData(data);
		writeJson(result);
		return null;
	}
	
	/**
     * 删除数据
     * @return
     * @throws Exception
     */
    public String daComDleData() throws Exception{
        String result = insertlDataService.daComDelData(data);        
        writeJson(result);
        return null;
    }
    
    /**
     * 检查唯一性
     * @return
     * @throws Exception
     */
    public String checkIsOnly() throws Exception{
        String result = insertlDataService.checkIsOnly(data);
        writeJson(result);
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

	public DAInsertDataService getInsertlDataService() {
		return insertlDataService;
	}

	public void setInsertlDataService(DAInsertDataService insertlDataService) {
		this.insertlDataService = insertlDataService;
	}

}
