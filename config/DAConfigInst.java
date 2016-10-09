package com.zjcds.da.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/** 
 * 配置管理类
 * @author linj
 * @date 2012-7-9
 *
 */
public class DAConfigInst {
	
	private static DAConfigInst configInstance = null;
	
	private Log logger = LogFactory.getLog(DAConfigInst.class);
	/** 配置信息 */
	private Properties prop;

	/** 实例化 */
	public static DAConfigInst getInstance() {
		if (configInstance == null) {
		    configInstance = new DAConfigInst();
		}
		return configInstance;
	}

	/** 加载property文件 */
	public void loadDAConfig(String path) throws Exception {
	    prop = new Properties();
	    InputStream inStream = new FileInputStream(path);
        prop.load(inStream);
        logger.info("加载配置文件：" + path);
	}

	/**
	 * 根据key获取property的值
	 * @param key
	 * @return
	 * @author linj created on 2013-11-27 
	 * @since CDS Framework 1.0
	 */
	public String getConfigValue(String key) {
	    if (prop != null) {
	        return prop.getProperty(key);
	    }
	    else {
	        return null;
	    }
	}
}
