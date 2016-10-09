package com.zjcds.da.service.charts;

/**
 * 获取数据服务Service接口
 * @author xinyf
 * @date   2013-6-20
 */
public interface DAChartsDataService {
	
	/**
	 * 获取数据服务json
	 * @param data
	 * @return
	 */
    String daGetData(String data) throws Exception;
}
