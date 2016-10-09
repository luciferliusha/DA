package com.zjcds.da.service.common;

/**
 * 公共
 * 
 * @author yuzq created on 2013-8-22
 * @since CDS Framework 1.0
 */
public interface DACommonService {

    /**
     * 获取下拉框数据
     * @param data
     * @return
     * @throws Exception
     * @author yuzq created on 2013-8-22 
     * @since CDS Framework 1.0
     */
    public String daGetCombo(String data, boolean showEmpty) throws Exception;
    /**
     * 获取下拉框树数据
     * @param data
     * @return
     * @throws Exception
     * @author yuzq created on 2013-8-22 
     * @since CDS Framework 1.0
     */
    public String daGeComboTree(String data) throws Exception;
}
