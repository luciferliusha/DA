package com.zjcds.da.service;

import java.util.Iterator;
import java.util.Map;

import net.sf.json.JSONObject;

import org.apache.log4j.Logger;

import com.zjcds.da.constant.DAConstant;
import com.zjcds.da.util.ReadFile;
import com.zjcds.da.util.SqlEscapeUtil;
import com.zjcds.framework.common.constant.Constants;
import com.zjcds.framework.common.util.JsonUtil;

/**
 * 服务基类
 * 
 * @author yuzq
 * 
 */
public abstract class DABaseService {

    private JSONObject json = new JSONObject();

    private Logger logger = Logger.getLogger(DABaseService.class);

    /** 返回数据 */
    protected String returnData = null;

    /** 操作成功是返回的数据 */
    protected final String getSuccessReturnData(Object data) {
        json.put(Constants.RETURN_FLAG, Constants.RETURN_FLAG_SUCCESS);
        json.put(Constants.RETURN_DATA, data);
        return json.toString();
    }

    /** 操作失败时返回的数据 */
    protected final String getFailReturnData(String errorMsg) {
        json.put(Constants.RETURN_FLAG, Constants.RETURN_FLAG_FAIL);
        json.put(Constants.RETURN_DATA, errorMsg);
        return json.toString();
    }

    /** 取得json文件内容 */
    @SuppressWarnings("unchecked")
    protected String getJsonFileContent(String data) throws Exception {
        Map<String, String> requestParams = (Map<String, String>) JsonUtil.getMapFromJson(data);
        String jsonFileName = requestParams.get(DAConstant.JSON_FILE_ID);
        logger.info("读取" + jsonFileName + "文件!");
        return JsonUtil.getJSONString(ReadFile.getFileContent(jsonFileName));
    }

    /** 组装sql语句 */
    protected String getAppendSql(Map<String, Object> params, String sql) throws Exception {
        StringBuffer sb = new StringBuffer(sql);
        JSONObject where = JSONObject.fromObject(params.get("where"));
        if (!where.isNullObject()) {
            for (Iterator<?> i = where.keys(); i.hasNext();) {
                String key = (String) i.next();
                Object value = where.get(key);
                if (value != null && key != null && !value.toString().trim().equals("")) {
                    String ope = " = ";
                    key = key.toLowerCase();// 转成小写
                    if (key.matches(".*_max")) {
                        ope = " <= ";
                    }
                    else if (key.matches(".*_min")) {
                        ope = " >= ";
                    }
                    key = key.replaceAll("(_max)|(_min)", "");// 去掉key中的无效字符
                    if (sb.toString().matches(".* where .*")) {
                        sb.append(" and ");
                    }
                    else {
                        sb.append(" where ");
                    }
                    sb.append(key).append(ope).append(value);
                }
            }
        }
        return sb.toString();
    }

    /** 增加查询条件,模糊查询 */
    protected final String getAppendSqlWhere(Map<String, Object> params, String sql) throws Exception {
        sql = sql.trim();// 去掉空格
        StringBuffer sqlSb = new StringBuffer(/*sql*/);
        StringBuffer sb = new StringBuffer();
        JSONObject where = JSONObject.fromObject(params.get("where"));
        if (!where.isNullObject()) {
            if (sql.endsWith("and")) {
                sb.append(" and ");
            }
            else {
                sb.append(" where ");
            }
            for (Iterator<?> i = where.keys(); i.hasNext();) {
                String key = (String) i.next();
                JSONObject value = (JSONObject) where.get(key);
                if (value != null && key != null && !value.isNullObject() && value.getString("data") != null) {
                    String ope = null;
                    key = key.toLowerCase();// 转成小写
                    String data = value.getString("data");
                    data = SqlEscapeUtil.sqlDataReplace(data);
                    Object isReplaceObj = value.get("isReplace");//是否是替换参数的
                    boolean isReplace = false;
                    if (isReplaceObj != null && isReplaceObj.toString().equalsIgnoreCase("true")) {
                        isReplace = true;
                    }
                    String dataType = null;
                    if (value.containsKey("dataType")) {
                        dataType = value.getString("dataType");//数据类型String：字符串类型，后台会加上'；Number：数字类型。默认是String类型
                    }
                    if(key.matches(".*_between_.*")){
                    	String[] querys = key.split("_between_");
                    	if (data != null && !"".equals(data)) {
                    	    if (isReplace) {//替换参数
                    	        sql = sql.replace("[" + querys[0] + "]", data).replace("[" + querys[1] + "]", data);
                    	    }
                    	    else {
                    	        if ("number".equalsIgnoreCase(dataType)) {//数字类型
                                    sb.append(" and ").append(querys[0]).append(" ").append("<=").append(" ").append(data);
                                    sb.append(" and ").append(querys[1]).append(" ").append(">=").append(" ").append(data);
                                }
                                else {
                                    sb.append(" and ").append(querys[0]).append(" ").append("<=").append(" '").append(data).append("'");
                                    sb.append(" and ").append(querys[1]).append(" ").append(">=").append(" '").append(data).append("'");
                                }
                    	    }
                    	}
                    }
                    else{
                        if (isReplace) {//替换参数
                            sql = sql.replace("[" + key + "]", data);
                            //需要判断和下面一样的各种情况，目前暂不实现
                        }
                        else {
                            if (key.matches(".*_max")) {
                                ope = " <= ";
                                if (!"number".equalsIgnoreCase(dataType) && data != null && !data.isEmpty()) {//数字类型
                                    data = "'" + data + "'";
                                }
                            }
                            else if (key.matches(".*_min")) {
                                ope = " >= ";
                                if (!"number".equalsIgnoreCase(dataType) && data != null && !data.isEmpty()) {//数字类型
                                    data = "'" + data + "'";
                                }
                            }
                            else if (key.matches(".*_equal")) {
                                ope = " = ";
                                if (!"number".equalsIgnoreCase(dataType) && data != null && !data.isEmpty()) {//数字类型
                                    data = "'" + data + "'";
                                }
                            }
                            if (ope == null) {// 无，则从where中取
                                ope = value.getString("where");
                                if (ope == null || ope.isEmpty()) {
                                    if (data != null && !"".equals(data))
                                        data = "='" + data + "'";
                                }
                                else if (ope.trim().toLowerCase().equals("like")) {
                                    if (data != null && !"".equals(data))
                                    	 data = "'%" + SqlEscapeUtil.sqlWhereEscape(data) + "%' ESCAPE '\\\\'";
                                }
                                else if (ope.trim().toLowerCase().equals("or")) {
                                    if (data != null && !"".equals(data)) {
                                        String[] datas = data.split(",");
                                        data = "";
                                        for (int j = 0; j < datas.length; j++) {
                                            data = data + " or " + key + "='" + datas[j] + "'";
                                        }
                                        data = data.replaceFirst("or", "");
                                    }
                                }
                                else if (ope.trim().toLowerCase().equals("orlike")) {
                                    if (data != null && !"".equals(data)) {
                                        String[] datas = data.split(",");
                                        data = "";
                                        for (int j = 0; j < datas.length; j++) {
                                        	data = data + " or " + key + " like '%" + SqlEscapeUtil.sqlWhereEscape(datas[j]) + "%' ESCAPE '\\\\'";
                                        }
                                        data = data.replaceFirst("or", "");
                                    }
                                }
                                else {
                                    if (data != null && !"".equals(data))
                                        data = "'" + data + "'";
                                }
                            }
                            else {
                                key = key.replaceAll("(_max)|(_min)|(_equal)", "");// 去掉key中的无效字符
                            }
                       
                            if (data != null && !"".equals(data)) {
                                if (ope.trim().toLowerCase().equals("or") || ope.trim().toLowerCase().equals("orlike")) {
                                    sb.append(" and (").append(data).append(")");
                                }
                                else {
                                    sb.append(" and ").append(key).append(" ").append(ope).append(" ").append(data);
                                }
                            }
                        }
                    }
                }
            }
        }
        sqlSb.append(sql);
        if (sb.toString().equals(" where ")) {
            return sqlSb.toString();
        }
        else if (sb.toString().equals(" and ")) {
            int index = sql.lastIndexOf("and");
            return sqlSb.delete(index, index + 3).toString();
        }
        else {
            if (sql.endsWith("and")) {
                int index = sql.lastIndexOf("and");
                sqlSb.delete(index, index + 3);
            }
            return sqlSb.append(sb.toString().replaceFirst("and", "")).toString();
        }

    }

    /** 增加查询条件,非模糊查询 */
    protected final String getAppendSqlWhereEquals(Map<String, Object> params, String sql) throws Exception {
        StringBuffer sb = new StringBuffer(sql);
        JSONObject where = JSONObject.fromObject(params.get("where"));
        if (!where.isNullObject()) {
            for (Iterator<?> i = where.keys(); i.hasNext();) {
                String key = (String) i.next();
                Object value = where.get(key);
                if (value != null && key != null && !value.toString().trim().equals("")) {
                    if (sb.toString().matches(".* where .*")) {
                        if (sb.toString().endsWith(")")) {
                            sb.append(" where ");
                        }
                        else {
                            sb.append(" and ");
                        }
                    }
                    else {
                        sb.append(" where ");
                    }
                    key = key.toLowerCase();
                    if (key.matches(".*_max")) {
                        String ope = " <= ";
                        key = key.replaceAll("(_max)|(_min)", "");
                        sb.append(key).append(ope).append(value);
                    }
                    else if (key.matches(".*_min")) {
                        String ope = ">=";
                        key = key.replaceAll("(_max)|(_min)", "");
                        sb.append(key).append(ope).append(value);
                    }
                    else {
                        sb.append(key).append(" = '").append(value).append("'");
                    }
                }
            }
        }
        return sb.toString();
    }
}
