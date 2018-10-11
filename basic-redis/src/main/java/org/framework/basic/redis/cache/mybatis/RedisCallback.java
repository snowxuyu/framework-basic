package org.framework.basic.redis.cache.mybatis;

import redis.clients.jedis.ShardedJedis;

public interface RedisCallback {

    Object doWithRedis(ShardedJedis jedis);
}

