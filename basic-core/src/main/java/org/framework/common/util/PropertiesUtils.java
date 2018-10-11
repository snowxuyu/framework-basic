package org.framework.common.util;

import com.alibaba.druid.util.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright @ 2016QIANLONG.
 * All right reserved.
 * Class Name : org.framework.common.util
 * Description :
 * Author : snowxuyu
 * Date : 2016/11/29
 */

public class PropertiesUtils {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private Properties prop = null;

    private PropertiesUtils() {
    }

    public PropertiesUtils(String path) {
        if (prop == null) {
            prop = new Properties();
        }
        try {
            prop.load(new InputStreamReader(PropertiesUtils.class.getClassLoader().getResourceAsStream(path), "UTF-8"));
        } catch (IOException e) {
            logger.error("failed to load properties:", e);
        }
    }

    /**
     * 根据key得到value的值
     */
    public String getStringValue(String key) {
        return prop.getProperty(key);
    }

    public Integer getIntegerValue(String key) {
        String property = prop.getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        return Integer.parseInt(property.trim());
    }


    public Long getLongValue(String key) {
        String property = prop.getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        return Long.parseLong(property.trim());
    }


    public Boolean getBooleanValue(String key) {
        String property = prop.getProperty(key);
        if (StringUtils.isEmpty(property)) {
            return null;
        }
        if ("true".equalsIgnoreCase(property)) {
            return true;
        } else if ("false".equalsIgnoreCase(property)) {
            return false;
        }
        throw new RuntimeException("The value can not parse to Boolean : " + property);
    }


    public Map<String, Object> getAllValues() {
        Map<String, Object> propertiesMap = new HashMap<>();

        Enumeration<?> propertyNames = prop.propertyNames();
        while (propertyNames.hasMoreElements()) {
            String key = (String) propertyNames.nextElement();
            Object value = prop.get(key);
            propertiesMap.put(key, value);
        }

        return propertiesMap;
    }
}
