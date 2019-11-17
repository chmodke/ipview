package org.chmodke.ipview.common.core.config;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/****************************************************************  
 * <p>Filename:    Config.java 
 * <p>Description:	 
 * <p>
 * <p>Create at:   2019/10/20 23:48
 * <p> 
 * <p>Modification History:  
 * <p>Date          Author  Version   Description  
 * <p>------------------------------------------------------------------  
 * <p>2019/10/20    kehao     1.0        
 * <p>------------------------------------------------------------------
 *
 * @author kehao
 * @version 1.0
 * @since
 *******************************************************************/

public class GlobalConfig {
    private static final Log logger = LogFactory.getLog(GlobalConfig.class);
    private static Properties global = null;

    private GlobalConfig() {
    }

    public static void setProperties(String key, String value) {
        if (null != key)
            global.setProperty(key, value);
    }

    public static String getProperties(String key) {
        return getProperties(key, null);
    }

    public static String getProperties(String key, String def) {
        if (null != global && StringUtils.isNotBlank(global.getProperty(key)))
            return global.getProperty(key);
        return def;

    }

    public static int getInteger(String key) {
        return getInteger(key, 0);

    }

    public static int getInteger(String key, int def) {
        try {
            return Integer.parseInt(getProperties(key));
        } catch (Exception e) {
            logger.warn(String.format("get %s fail,use default value :%s", key, def));
            return def;
        }
    }

    public static boolean getBoolean(String key) {
        return getBoolean(key, false);
    }

    public static boolean getBoolean(String key, boolean def) {
        try {
            return Boolean.parseBoolean(getProperties(key));
        } catch (Exception e) {
            logger.warn(String.format("get %s fail,use default value :%s", key, def));
            return def;
        }
    }


    static {

        InputStream in = GlobalConfig.class.getClassLoader().getResourceAsStream("global.properties");
        global = new Properties();
        try {
            global.load(in);
        } catch (IOException e) {
            logger.error("GlobalConfig initializer->Exception:", e);
            System.exit(-1);
        }
    }
}
