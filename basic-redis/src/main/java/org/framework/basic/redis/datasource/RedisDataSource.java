package org.framework.basic.redis.datasource;

import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;


public interface RedisDataSource {
    ShardedJedisPool getShardedJedisPool();

    abstract ShardedJedis getRedisClient();

    void returnResource(ShardedJedis shardedJedis);

    void returnResource(ShardedJedis shardedJedis, boolean broken);
}
