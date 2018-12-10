package com.dashu.fk.test.tools.system;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;

/**
 * Created by zhf2015 on 18/3/30.
 */
public class SystemInfo {

    private static final Logger logger = LoggerFactory.getLogger(SystemInfo.class);

    public static String getOSInfo() {
        Properties props = System.getProperties(); //获得系统属性集
        String osName = props.getProperty("os.name"); //操作系统名称
        String osArch = props.getProperty("os.arch"); //操作系统构架
        String osVersion = props.getProperty("os.version"); //操作系统版本

        logger.info("操作系统:{} , 系统版本:{}", osName, osVersion);
        return osName;
    }

    public static void main(String[] arg) {
//        System.out.println(getOSInfo());
    }
}
