package org.framework.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Created by snow on 2015/12/16.
 */
 @Component
public abstract class ContextUtils implements ApplicationContextAware {

    private static ApplicationContext applicationContext;

    private static final Logger logger = LoggerFactory.getLogger(ContextUtils.class);

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(Class<?> beanType) {
        return getApplicationContext().getBean(beanType);
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
        ContextUtils.applicationContext = applicationContext;
        logger.debug("ApplicationContext registed");
    }
}
