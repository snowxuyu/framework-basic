package org.framework.basic.system;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Copyright @ 2017QIANLONG.
 * All right reserved.
 * Class Name : org.framework.basic.system
 * Description : framework-basic
 * Author : snowxuyu
 * Date : 2017/2/16
 */

public class PropertyPlaceholderConfigurer extends org.springframework.beans.factory.config.PropertyPlaceholderConfigurer {
    private static Map ctxPropertiesMap;
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props)
            throws BeansException {
        super.processProperties(beanFactoryToProcess,props);
        ctxPropertiesMap = new HashMap();
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String value = props.getProperty(keyStr);
            ctxPropertiesMap.put(keyStr, value);
        }
    }
    public static Object getContextProperty(String name) {
        return ctxPropertiesMap.get(name);
    }
}
