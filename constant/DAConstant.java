package com.zjcds.da.constant;

/**
 * 常量类
 * 
 * @author yuzq
 * @date 2013-06-17
 * 
 */
public class DAConstant {

    /** 返回标识ReturnFlag,成功0 */
    public final static String RETURN_FLAG_SUCCESS = "0";

    /** 返回标识ReturnFlag,失败-1 */
    public final static String RETURN_FLAG_FAIL = "-1";

    /** 返回标识returnFlag */
    public final static String RETURN_FLAG = "returnFlag";

    /** 返回信息标识data */
    public final static String RETURN_DATA = "data";

    /** .json后缀 */
    public final static String SUFFIX_JSON = ".json";

    /** mybatis动态sql的参数名 */
    public final static String SQLNAME = "sql";
   
    /** json文件和sqlConfig文件的目录 */
    public final static String CONFIG_DIR = "da.config.fileDir";

    // /------------DADetailData 相关常量---start
    /**
     * 详细信息Tab的内容
     */
    public final static String BODY = "body";

    /**
     * 详细信息Tab标题相关信息
     */
    public final static String ALLBODY = "allBody";

    /**
     * 详细信息Tab标题信息
     */
    public final static String BODYS = "bodys";

    /**
     * Tab数目
     */
    public final static String NUMBER = "number";

    /**
     * 数据的顺序
     */
    public final static String COLUMNS = "columns";

    /**
     * 字段的中文描述
     */
    public final static String TEXT = "text";

    /**
     * 数据库字段名字
     */
    public final static String COLUMNNAME = "columnName";

    /**
     * sqlKey，获取配置文件中sqlKey用于执行指定sql
     */
    public final static String SQLKEY = "sqlKey";

    /**
     * 传递到前台的name标识，表示中文描述
     */
    public final static String NAME = "name";

    /**
     * 传递到前台的value标识，表示值
     */
    public final static String VALUE = "value";

    /**
     * 传递的ID参数名
     */
    public final static String ID = "id";

    /**
     * 传递的记录ID参数名
     */
    public final static String ITEMID = "itemId";

    /**
     * 传递的Tab的ID参数名
     */
    public final static String TYPEID = "typeId";

    // /------------DADetailData 相关常量---end

    // /----------DAChartsData 相关常量---start
    /**
     * highcharts的series名
     */
    public static final String SERIES = "series";

    /**
     * 映射信息
     */
    public static final String COLUMNMAPPING = "columnMapping";

    /**
     * data的列映射
     */
    public static final String DATACOLUMN = "dataColumn";
    public static final String DATACOLUMN_COLOR = "dataColumnColor";


    /**
     * highcharts配置
     */
    public static final String XAXIS = "xAxis";

    /**
     * highcharts配置
     */
    public static final String TYPE = "type";

    /**
     * highcharts配置
     */
    public static final String DATA = "data";

    /**
     * highcharts配置
     */
    public static final String CATEGORIES = "categories";

    /**
     * highcharts配置
     */
    public static final String TOOLTIP = "toolTip";

    /**
     * highcharts配置
     */
    public static final String TOOLTIPVALUE = "toolTipValue";

    public static final String TOOLTIPVALUE_NAME = "name";

    public static final String TOOLTIPVALUE_DATA = "data";

    /**
     * charts在json配置文件中的索引
     */
    public static final int CHARTS_INDEX = 2;

    /**
     * name前缀标识
     */
    public static final String NAME_PREFIX = "name_prefix";

    public static final String NAME_PREFIX_PARAM = "param";

    public static final String NAME_PREFIX_TYPE = "type";

    public static final String NAME_PREFIX_VALUE = "value";

    public static final String SERIES_NAME = "name";

    /**
     * 用于饼状图显示时，字符标签的后缀
     */
    public static final String LABEL_SUFFIX = "label_suffix";

    public static final String XAXIS_SUFFIX = "xAxis_suffix";

    // /----------DAChartsData 相关常量---end
    /** json配置文件标识,匹配/conf/json/..目录下json文件 */
    public static final String JSON_FILE_ID = "id";
    
    /** SQLConfig配置文件标识,匹配/conf/sqlConfig/..目录下json文件 */
    public static final String SQL_CONFIG_ID = "sqlKey";
    
    /** json文件data部分 */
    public static final String JSON_FILE_DATA = "data";

    /** json文件data下的body节点 */
    public static final String JSON_FILE_DATA_BODY = "body";

    /** json文件 data-->body-->data节点 */
    public static final String JSON_FILE_DATA_BODY_DATA = "data";

    /** json文件 data-->body-->excelName节点 (导出excel时的表格名称) */
    public static final String JSON_FILE_DATA_BODY_EXCELNAME = "excelName";

    /** json文件 data-->body-->data-->field节点 */
    public static final String JSON_FILE_DATA_BODY_DATA_FIELD = "field";

    /** json文件 data-->body-->data-->title节点 */
    public static final String JSON_FILE_DATA_BODY_DATA_TITLE = "title";

    /** json文件 data-->body-->data-->width节点 */
    public static final String JSON_FILE_DATA_BODY_DATA_WIDTH = "width";
    
    /** chart color文件路径 */
    public static final String CHART_COLOR_JSON = "/chart/color.json";
    
    /** chart series color*/
    public static final String COLOR = "color";
    
    /** chart series y*/
    public static final String Y = "y";
    
    /** chart图默认颜色 */
    public static final String CHART_DEFAULT_COLOR = "#ed561b";

    /** excel中每一个sheet显示的记录数目 */
    public static final int EACH_SHEET_SHOW_ROWS_COUNT = 65000;

    /*** excel默认宽度 */
    public static final int EXCEL_DEFAULT_WIDTH = 70;

    /** json文件中body节点的索引值 */
    public static final int BODY_NODE_INDEX_IN_JSON_FILE = 2;

    public static final String VALUES = "values";

    public static final String SET = "set";

    public static final String WHERE = "where";

    /** 用于组装easyui combo返回数据 */
    public final static String EASYUI_COMBO_ID = "id";

    public final static String EASYUI_COMBO_TEXT = "text";
    
    public final static String EASYUI_COMBO_PARENT = "parent";

    /** easyui combo请求参数 **/
    public final static String EASYUI_COMBO_SQL = "sql";

    public final static String EASYUI_COMBO_TREE_PARENTKEY = "parentKey";

    public final static String EASYUI_COMBO_TREE_PARENTVALUE = "parentValue";

    public final static String EASYUI_COMBO_TREE_ISAJAX = "isAjax";

    public final static String EASYUI_COMBO_KEY = "key";
}
