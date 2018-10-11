package org.framework.basic.redis.cache.mybatis;

import org.framework.basic.redis.template.RedisClientTemplate;
import org.framework.common.util.ContextUtils;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisConnectionException;

public class CachePool {
    private static ShardedJedisPool pool;
    private static CachePool cachePool = null;
    private static boolean flg = true;

    public static CachePool getInstance() {
        if (cachePool == null) {
            synchronized (CachePool.class) {
                if(flg) {
                    cachePool = new CachePool();
                    flg = false;
                }
            }
        }
        return cachePool;
    }

    private CachePool() {
        Object object = ContextUtils.getBean(RedisClientTemplate.class);
        if (object != null) {
            RedisClientTemplate redisClientTemplate = (RedisClientTemplate) object;
            redisClientTemplate.getRedisDataSource().getShardedJedisPool();
            pool = redisClientTemplate.getRedisDataSource().getShardedJedisPool();
        }
    }

    public ShardedJedis getJedis() {
        ShardedJedis jedis = null;
        boolean borrowOrOprSuccess = true;
        try {
            jedis = pool.getResource();
        } catch (JedisConnectionException e) {
            borrowOrOprSuccess = false;
            if (jedis != null) {
                pool.returnBrokenResource(jedis);
            }
        } finally {
            if (borrowOrOprSuccess) {
                pool.returnResource(jedis);
            }
        }
        jedis = pool.getResource();
        return jedis;
    }

    public ShardedJedisPool getJedisPool() {
        return pool;
    }

}