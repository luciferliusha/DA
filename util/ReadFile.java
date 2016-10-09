package com.zjcds.da.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

import com.zjcds.da.config.DAConfigInst;
import com.zjcds.da.constant.DAConstant;
import com.zjcds.framework.common.config.ClassPathHelper;

/**
 * 读取文件工具类
 * 
 * @author yuzq
 * 
 */
public class ReadFile {


    /**
     * 读取文件内容
     * 
     * @param filePath 文件路径 在classpath之下
     * @return
     * @throws Exception
     */
    public static String getFileContent(String fileName) throws Exception {
        String webRootRealPath = ClassPathHelper.getClassPath();
        
        String dir = DAConfigInst.getInstance().getConfigValue(DAConstant.CONFIG_DIR);
        if (dir != null && !dir.isEmpty()) {
            fileName = dir + File.separator + fileName; 
        }
        
        String filePath = webRootRealPath+"jsonFile"+File.separator+fileName+".json";
        File f = new File(filePath);
        InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
        BufferedReader in = new BufferedReader(isr);
        String s;
        StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null) {
            sb.append(s+"\n");
        }
        in.close();
        return sb.toString();
    }
  
    /**
     * 读取classpath之下的文件内容
     * @param fileName 读取classpath之下的文件全路径
     * @return
     * @throws Exception
     */
    public static String getAllFileContent(String fileName) throws Exception{
    	String webRootRealPath = ClassPathHelper.getClassPath();
    	String filePath = webRootRealPath + fileName;
    	File f = new File(filePath);
    	InputStreamReader isr = new InputStreamReader(new FileInputStream(f), "UTF-8");
    	BufferedReader in = new BufferedReader(isr);
    	String s;
    	StringBuilder sb = new StringBuilder();
        while ((s = in.readLine()) != null) {
            sb.append(s+"\n");
        }
        in.close();
        return sb.toString();
    }
}
