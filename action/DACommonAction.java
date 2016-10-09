package com.zjcds.da.action;

import java.util.Map;

import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.service.common.DACommonService;
import com.zjcds.framework.action.PortalBaseAction;
import com.zjcds.framework.common.util.JsonUtil;
import com.zjcds.framework.common.util.ReturnInfoUtil;

public class DACommonAction extends PortalBaseAction {

    /**
	 * 公共接口：取得下拉框
	 */
    private static final long serialVersionUID = 1L;

    private String data;

    private DACommonService commonService;
    
    @Override
    public String doExecute() throws Exception {
        return null;
    }

    /**
     * 获取下拉框数据
     * 传递参数data={"sql":"select t.bianhao,t.xingzhengqh from t_d_xingzhengqh_zj t where t.shangjibh = '330500'", "showEmpty":"true"}
     * showEmpty 表示是否添加第一项为空的请选择
     * @return
     * @throws Exception
     * @author yuzq created on 2013-8-22
     * @since CDS Framework 1.0
     */
    @SuppressWarnings("unchecked")
    public String daGeCombo() throws Exception {
        if (data != null && !"".equals(data.trim())) {
            Map<String, String> paramMap = (Map<String, String>) JsonUtil.getMapFromJson(data);
            if (checkComboRequestData(paramMap)) {
                boolean showEmpty = false;
                if ("true".equals(paramMap.get("showEmpty"))) {
                    showEmpty = true;
                }
                super.writeJson(commonService.daGetCombo(paramMap.get(DAConstant.EASYUI_COMBO_SQL), showEmpty));
            }
            else {
                super.writeJson(ReturnInfoUtil.getFailReturnData("请传递参数sql!"));
            }
        }
        else {
            super.writeJson(ReturnInfoUtil.getFailReturnData("请传递参数data!"));
        }
        return null;
    }

    private boolean checkComboRequestData(Map<String, String> paramMap) throws Exception {
        String sql = paramMap.get(DAConstant.EASYUI_COMBO_SQL);
        boolean flag = (sql != null && !"".equals(sql.trim()));
        return flag;
    }

    /**
     * 获取下拉框树数据
     * 
     * @return
     * @throws Exception
     * @author yuzq created on 2013-8-22
     * @since CDS Framework 1.0
     */
    @SuppressWarnings("unchecked")
    public String daGeComboTree() throws Exception {
        if (data != null && !"".equals(data.trim())) {
            Map<String, Object> paramMap = (Map<String, Object>) JsonUtil.getMapFromJson(data);
            if (checkComboTreeRequestData(paramMap)) {
                super.writeJson(commonService.daGeComboTree(data));
            }
            else {
                super.writeJson(ReturnInfoUtil.getFailReturnData("请传递完整参数列表!"));
            }
        }
        else {
            super.writeJson(ReturnInfoUtil.getFailReturnData("请传递参数data!"));
        }
        return null;
    }

    private boolean checkComboTreeRequestData(Map<String, Object> paramMap) throws Exception {
        String sql = (String) paramMap.get(DAConstant.EASYUI_COMBO_SQL);
        String parentKey = (String) paramMap.get(DAConstant.EASYUI_COMBO_TREE_PARENTKEY);
        Integer parentValue = (Integer) paramMap.get(DAConstant.EASYUI_COMBO_TREE_PARENTVALUE);
        String isAjax = (String) paramMap.get(DAConstant.EASYUI_COMBO_TREE_ISAJAX);
        String key = (String) paramMap.get(DAConstant.EASYUI_COMBO_KEY);
        boolean flag = (sql != null && !"".equals(sql.trim())
                && parentKey != null && !"".equals(parentKey.trim()) && parentValue != null && isAjax != null
                && !"".equals(isAjax.trim()) && key != null && !"".equals(key.trim()));
        return flag;
    }

    public DACommonService getCommonService() {
        return commonService;
    }

    public void setCommonService(DACommonService commonService) {
        this.commonService = commonService;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

}
