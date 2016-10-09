package com.zjcds.da.config;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import com.zjcds.framework.common.config.ClassPathHelper;
import com.zjcds.framework.common.constant.Constants;
import com.zjcds.framework.common.util.FileUtil;
import com.zjcds.framework.common.util.LicenceUtil;
import com.zjcds.framework.view.bean.CdsLicence;

/**
 * 配置信息解析类
 * 
 * @author linj
 * @date 2012-07-09
 */
public class DAConfigureHolder extends PropertyPlaceholderConfigurer {

    private Log logger = LogFactory.getLog(DAConfigureHolder.class);

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        logger.info("ConfigureHolder starting...");
        this.readConfigInfo();
        super.postProcessBeanFactory(beanFactory);
        logger.info("ConfigureHolder end.");
    }

    /**
     * 读取配置信息
     * 
     * @author linj created on 2013-3-1
     * @since CDT Framework 1.0
     */
    private void readConfigInfo() {
        logger.info("读取licence!");
        String webRootRealPath = ClassPathHelper.getClassPath();
        String fileUrl = webRootRealPath + "licence.lrc";
        try {
            String lrcStr = FileUtil.readFileToString(fileUrl, "");
            CdsLicence cdsLrc = LicenceUtil.readLicence(lrcStr,Constants.LICENCE_KEY);
            if(!LicenceUtil.compareLicence(cdsLrc)){
                logger.error("licence已过期，请联系管理员!");
            }
        }
        catch (Exception e) {
            logger.info("读取licence失败!" + fileUrl);
            e.printStackTrace();
        }
        logger.info("开始加载系统配置!");
        try {
            DAConfigInst.getInstance().loadDAConfig(webRootRealPath + "config.properties");
        }
        catch (Exception e) {
            logger.error("加载系统配置失败!" + e.getMessage());
            return;
        }
        logger.info("系统配置加载完成!");
    }

}
