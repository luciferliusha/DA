package com.zjcds.da.service.common.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.data.dao.DACommonDataDao;
import com.zjcds.da.service.DABaseService;
import com.zjcds.da.service.common.DACommonService;
import com.zjcds.framework.common.util.JsonUtil;

public class DACommonServiceImpl extends DABaseService implements DACommonService {

    private DACommonDataDao commonDataDao;

    public DACommonDataDao getCommonDataDao() {
        return commonDataDao;
    }

    public void setCommonDataDao(DACommonDataDao commonDataDao) {
        this.commonDataDao = commonDataDao;
    }

    /**
     * @see com.zjcds.da.service.common.DACommonService#daGeComboTree(java.lang.String)
     */
    @SuppressWarnings("unchecked")
    @Override
    public String daGeComboTree(String data) throws Exception {
        Map<String, Object> paramMap = (Map<String, Object>) JsonUtil.getMapFromJson(data);
        return getComboTreeData(paramMap);
    }

    /** 取得下拉框树形数据 */
    private String getComboTreeData(Map<String, Object> paramMap) throws Exception {
        Integer parentValue = (Integer) paramMap.get(DAConstant.EASYUI_COMBO_TREE_PARENTVALUE);
        return super.getSuccessReturnData(getComboTreeChildrenData(paramMap, parentValue));
    }

    private String getQuerySql(Map<String, Object> paramMap, Integer parentValue) throws Exception {
        StringBuffer baseSql = new StringBuffer((String) paramMap.get(DAConstant.EASYUI_COMBO_SQL));
        String parentKey = (String) paramMap.get(DAConstant.EASYUI_COMBO_TREE_PARENTKEY);
        String resultSql = baseSql.append(" where ").append(parentKey).append(" = ").append(parentValue).toString();
        return resultSql;
    }

    private List<LinkedHashMap<String, Object>> getComboTreeChildrenData(Map<String, Object> paramMap,
            Integer parentValue) throws Exception {
        String querySql = getQuerySql(paramMap, parentValue);
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("sql", querySql);
        List<LinkedHashMap<String, Object>> result = new ArrayList<LinkedHashMap<String,Object>>();
        List<LinkedHashMap<String, Object>> list = commonDataDao.getCommonData(sqlMap);
        for (LinkedHashMap<String, Object> map : list) {
            map = (LinkedHashMap<String, Object>) setIdAndText(map);
            if (getAllData((String) paramMap.get(DAConstant.EASYUI_COMBO_TREE_ISAJAX))) {
                Integer id = Integer.valueOf(map.get(DAConstant.EASYUI_COMBO_ID).toString());
                map.put("children", getComboTreeChildrenData(paramMap, id));
            }
            result.add(map);
        }
        return result;
    }

    private boolean getAllData(String isAjax) throws Exception {
        return !"true".equals(isAjax.trim());
    }

    /**
     * @see com.zjcds.da.service.common.DACommonService#daGetCombo(java.lang.String)
     */
    @Override
    public String daGetCombo(String data, boolean showEmpty) throws Exception {
        Map<String, String> sqlMap = new HashMap<String, String>();
        sqlMap.put("sql", data);
        List<LinkedHashMap<String, Object>> list = commonDataDao.getCommonData(sqlMap);
        return JsonUtil.getJSONString(easyUiComboData(list, showEmpty));//直接返回数据，不封装returnFlag
    }

    /**
     * 组装easyui combo所需数据格式
     */
    private List<Map<String, Object>> easyUiComboData(List<LinkedHashMap<String, Object>> list, boolean showEmpty) throws Exception {
        List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
        if (showEmpty) {
            LinkedHashMap<String, Object> emptyMap = new LinkedHashMap<String, Object>();
            emptyMap.put(DAConstant.EASYUI_COMBO_ID, "");
            emptyMap.put(DAConstant.EASYUI_COMBO_TEXT, "全部");
            result.add(emptyMap);
        }
        for (LinkedHashMap<String, Object> lhMap : list) {
            Map<String, Object> each = setIdAndText(lhMap);
            // each.put(DAConstant.EASYUI_COMBO_ID, map.get("ID"));
            // each.put(DAConstant.EASYUI_COMBO_TEXT, map.get("NAME"));
            result.add(each);
        }
        return result;
    }

    private LinkedHashMap<String, Object> setIdAndText(LinkedHashMap<String, Object> lhMap) throws Exception {
        int i = 0;
        LinkedHashMap<String, Object> result = new LinkedHashMap<String, Object>();
        for (Entry<String, Object> entry : lhMap.entrySet()) {
            if (i == 0) {
                result.put(DAConstant.EASYUI_COMBO_ID, entry.getValue().toString());
                i++;
            }
            else if (i == 1) {
                result.put(DAConstant.EASYUI_COMBO_TEXT, entry.getValue().toString());
                i++;
            }
            else if (i == 2){
            	result.put(DAConstant.EASYUI_COMBO_PARENT, entry.getValue().toString());
            }
        }
        return result;
    }
}
