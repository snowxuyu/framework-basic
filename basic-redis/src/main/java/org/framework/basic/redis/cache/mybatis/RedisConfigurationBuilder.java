package org.framework.basic.redis.cache.mybatis;

import org.apache.ibatis.cache.CacheException;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.reflection.SystemMetaObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

final class RedisConfigurationBuilder {

    /**
     * This class instance.
     */
    private static final RedisConfigurationBuilder INSTANCE = new RedisConfigurationBuilder();

    private static final String SYSTEM_PROPERTY_REDIS_PROPERTIES_FILENAME = "redis.properties.filename";

    private static final String REDIS_RESOURCE = "redis.properties";

    private final String redisPropertiesFilename;

    /**
     * Hidden constructor, this class can't be instantiated.
     */
    private RedisConfigurationBuilder() {
        redisPropertiesFilename = System.getProperty(SYSTEM_PROPERTY_REDIS_PROPERTIES_FILENAME, REDIS_RESOURCE);
    }

    /**
     * Return this class instance.
     *
     * @return this class instance.
     */
    public static RedisConfigurationBuilder getInstance() {
        return INSTANCE;
    }

    /**
     * Parses the Config and builds a new {@link ConfigWithHost}.
     *
     * @return the converted {@link ConfigWithHost}.
     */
    public ConfigWithHost parseConfiguration() {
        return parseConfiguration(getClass().getClassLoader());
    }

    /**
     * Parses the Config and builds a new {@link ConfigWithHost}.
     *
     * @param classLoader
     *            {@link ClassLoader} used to load the
     *            {@code memcached.properties} file in classpath.
     * @return the converted {@link ConfigWithHost}.
     */
    public ConfigWithHost parseConfiguration(ClassLoader classLoader) {
        Properties config = new Properties();

        InputStream input = classLoader.getResourceAsStream(redisPropertiesFilename);
        if (input != null) {
            try {
                config.load(input);
            } catch (IOException e) {
                throw new RuntimeException(
                        "An error occurred while reading classpath property '"
                                + redisPropertiesFilename
                                + "', see nested exceptions", e);
            } finally {
                try {
                    input.close();
                } catch (IOException e) {
                    // close quietly
                }
            }
        }

        ConfigWithHost jedisConfig = new ConfigWithHost();
        jedisConfig.setHost("localhost");
        setConfigProperties(config, jedisConfig);
        return jedisConfig;
    }

    private void setConfigProperties(Properties properties,
                                     ConfigWithHost jedisConfig) {
        if (properties != null) {
            MetaObject metaCache = SystemMetaObject.forObject(jedisConfig);
            for (Map.Entry<Object, Object> entry : properties.entrySet()) {
                String name = (String) entry.getKey();
                String value = (String) entry.getValue();
                if (metaCache.hasSetter(name)) {
                    Class<?> type = metaCache.getSetterType(name);
                    if (String.class == type) {
                        metaCache.setValue(name, value);
                    } else if (int.class == type || Integer.class == type) {
                        metaCache.setValue(name, Integer.valueOf(value));
                    } else if (long.class == type || Long.class == type) {
                        metaCache.setValue(name, Long.valueOf(value));
                    } else if (short.class == type || Short.class == type) {
                        metaCache.setValue(name, Short.valueOf(value));
                    } else if (byte.class == type || Byte.class == type) {
                        metaCache.setValue(name, Byte.valueOf(value));
                    } else if (float.class == type || Float.class == type) {
                        metaCache.setValue(name, Float.valueOf(value));
                    } else if (boolean.class == type || Boolean.class == type) {
                        metaCache.setValue(name, Boolean.valueOf(value));
                    } else if (double.class == type || Double.class == type) {
                        metaCache.setValue(name, Double.valueOf(value));
                    } else {
                        throw new CacheException("Unsupported property type: '"
                                + name + "' of type " + type);
                    }
                }
            }
        }
    }

}

