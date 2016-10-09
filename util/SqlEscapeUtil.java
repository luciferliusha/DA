package com.zjcds.da.util;
/**
 * 替换sql语句中的特殊字符
 * @author songd
 *
 */
public class SqlEscapeUtil {
	
	/**
	 * 替换查询条件值中的'
	 * @param str
	 * @return
	 */
	public static String sqlDataReplace(String str){
		str = str.replace("'", "''");
		return str;
	}
	
	/**
	 * 替换like情况下where 条件中的% _ \    (替换之后再调用replaceAll函数 在jsava中组装字符串时配置where条件)
	 * @param str
	 * @return
	 */
	public static String sqlWhereEscape(String str){
		str = str.replace("\\", "\\\\").replace("%", "\\\\%").replace("_", "\\\\_");
		return str;
	}
	
	/**
	 * 替换like情况下where 条件中的% _ \ ' (替换之后不调用replaceAll函数 在mybatis xml文件中配置where条件)
	 * @param str
	 * @return
	 */
	public static String sqlWhereEscape2(String str){
		str = str.replace("\\", "\\\\").replace("%", "\\\\%").replace("_", "\\\\_");
		return str;
	}
}
