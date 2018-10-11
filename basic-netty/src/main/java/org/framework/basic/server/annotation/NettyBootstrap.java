package org.framework.basic.server.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface NettyBootstrap {
    String applicationContext() default "classpath*:/*-application.xml";
    String servletContext() default "classpath*:/*-servlet.xml";
    String serverConfig() default "configs/server.properties";
}
