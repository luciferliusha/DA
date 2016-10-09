package com.zjcds.da.service.detailData;


/**
 * 详细信息接口
 * @author xinyf
 * @date   2013-6-18
 */
public interface DADetailDataService {
	/**
	 * 获取详细信息，返回json字符串
	 * @param data
	 * @return
	 */
	String daGetDetailData(String data) throws Exception;
	
    /**
     * 执行相应的SQL并返回数据
     * @param data
     * @return
     */
    String querySql(String data) throws Exception;
}
