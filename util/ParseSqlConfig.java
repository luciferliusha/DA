package com.zjcds.da.util;

import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import com.zjcds.da.config.DAConfigInst;
import com.zjcds.da.constant.DAConstant;
import com.zjcds.framework.common.config.ConfigParseHelper;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 解析sql配置文件
 * 
 * @author yuzq
 * 
 */
public class ParseSqlConfig {

    /**
     * 根据参数取得sql语句
     */
    @SuppressWarnings("unchecked")
    public static String getSql(String data) throws Exception {
        String fileName = "sqlConfig.xml";
        String dir = DAConfigInst.getInstance().getConfigValue(DAConstant.CONFIG_DIR);
        if (dir != null && !dir.isEmpty()) {
            fileName = "sqlConfig" + File.separator + dir + File.separator + fileName;
        }
        Map<String, String> params = (Map<String, String>) JsonUtil.getMapFromJson(data);
        //String xmlName = "sqlConfig"+File.separator+params.get("id") + ".xml";
        Map<String, String> sqls = ConfigParseHelper.getConfigs(fileName, "/configs/mapper", "key", "value");
        String sql = getSql(params, sqls);
        return sql;
    }
    /**
     * 根据参数取得sql语句
     * @param params
     * @return
     * @throws Exception
     * @author Administrator created on 2013-9-9 
     * @since CDS Framework 1.0
     */
    public static String getSqlById(String id) throws Exception {
        String fileName = "sqlConfig.xml";
        String dir = DAConfigInst.getInstance().getConfigValue(DAConstant.CONFIG_DIR);
        if (dir != null && !dir.isEmpty()) {
            fileName = "sqlConfig" + File.separator + dir + File.separator + fileName;
        }
        Map<String, String> sqls = ConfigParseHelper.getConfigs(fileName, "/configs/mapper", "key", "value");
        String sql = sqls.get(id);
        return sql;
    }

    /** 根据参数取得sql语句 */
    private static String getSql(Map<String, String> params, Map<String, String> sqls) throws Exception {
        String key = params.get("id");
        String sql = sqls.get(key);
//        for (Entry<String, String> entry : params.entrySet()) {
//            String param = "\\$\\{" + entry.getKey() + "\\}";
//            sql = sql.replaceAll(param, entry.getValue());
//        }
        return sql;
    }
    
    /**
     * 根据参数取得sql语句
     * @param params
     * @param sqlIdKey 
     * @return
     * @throws Exception
     */
    public static String getSql(Map<String, String> params, String sqlIdKey) throws Exception {
        String xmlName = "sqlConfig"+File.separator+params.get("id") + ".xml";
        Map<String, String> sqls = ConfigParseHelper.getConfigs(xmlName, "/configs/mapper", "key", "value");
        String sql = getSql(params, sqls, sqlIdKey);
        return sql;
    }

    /**
     * 根据参数取得sql语句
     * @param params
     * @param sqls
     * @param sqlIdKey
     * @return
     * @throws Exception
     */
    private static String getSql(Map<String, String> params, Map<String, String> sqls, String sqlIdKey) throws Exception {
        String sql = sqls.get(sqlIdKey);
        for (Entry<String, String> entry : params.entrySet()) {
            String param = "\\$\\{" + entry.getKey() + "\\}";
            sql = sql.replaceAll(param, entry.getValue());
        }
        return sql;
    }
}
