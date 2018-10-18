package org.framework.basic.dao;

import org.apache.ibatis.annotations.DeleteProvider;
import org.apache.ibatis.annotations.InsertProvider;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.ResultMap;
import org.apache.ibatis.annotations.SelectProvider;
import org.apache.ibatis.annotations.UpdateProvider;
import org.framework.basic.entity.BaseEntity;
import org.framework.basic.mybatis.MyBatisProvider;
import org.framework.basic.mybatis.Sort;
import org.framework.basic.mybatis.complexQuery.CustomQueryParam;

import java.util.List;

/**
 * Created by snow on 2015/7/25.
 */

public abstract interface BaseDao<T extends BaseEntity> {
    @SelectProvider(type = MyBatisProvider.class, method = "getAll")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> getAll();

    @SelectProvider(type = MyBatisProvider.class, method = "getById")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public T getById(Long id);

    @SelectProvider(type = MyBatisProvider.class, method = "count")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public Integer count(T params);

    @SelectProvider(type = MyBatisProvider.class, method = "countLike")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public Integer countLike(T findParams);

    @SelectProvider(type = MyBatisProvider.class, method = "countQuery")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    public Integer countQuery(@Param("queryParams") List<CustomQueryParam> customQueryParams);

    @SelectProvider(type = MyBatisProvider.class, method = "get")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public T getOne(T findParams);

    @SelectProvider(type = MyBatisProvider.class, method = "query")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> query(@Param("queryParams") List<CustomQueryParam> customQueryParams, @Param("sortList") List<Sort> sortList);

    @SelectProvider(type = MyBatisProvider.class, method = "get")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> get(T findParams);

    @SelectProvider(type = MyBatisProvider.class, method = "find")
    @Options(flushCache = Options.FlushCachePolicy.FALSE, useCache = true)
    @ResultMap("getMap")
    public List<T> find(T findParams);

    @InsertProvider(type = MyBatisProvider.class, method = "insert")
    @Options(keyProperty = "id", flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true)
    public int insert(T t);

    @InsertProvider(type = MyBatisProvider.class, method = "insertBatch")
    @Options(keyProperty = "id", flushCache = Options.FlushCachePolicy.TRUE, useGeneratedKeys = true)
    public int insertBatch(@Param("list") List<T> list);

    @UpdateProvider(type = MyBatisProvider.class, method = "update")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int update(T t);

    @DeleteProvider(type = MyBatisProvider.class, method = "deleteById")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int deleteById(Long id);

    @DeleteProvider(type = MyBatisProvider.class, method = "deleteByPrimaryKey")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int deleteByPrimaryKey(T t);

    @DeleteProvider(type = MyBatisProvider.class, method = "deleteAll")
    @Options(flushCache = Options.FlushCachePolicy.TRUE)
    public int deleteAll();
}
