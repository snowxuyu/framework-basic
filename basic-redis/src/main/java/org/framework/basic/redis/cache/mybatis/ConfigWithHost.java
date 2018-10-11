package org.framework.basic.redis.cache.mybatis;

import redis.clients.jedis.JedisPoolConfig;

public class ConfigWithHost extends JedisPoolConfig {

    private String host;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

}
