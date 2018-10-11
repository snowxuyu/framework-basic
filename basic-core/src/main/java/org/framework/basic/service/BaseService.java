package org.framework.basic.service;

import org.framework.basic.entity.BaseEntity;
import org.framework.basic.mybatis.complexQuery.CustomQueryParam;
import org.framework.exception.BaseException;

import java.util.List;

/**
 * Created by snow on 2015/8/20.
 */
public interface BaseService<T extends BaseEntity> {
    /**
     * 获取所有列表对象，如果数据量较大，请勿使用
     *
     * @return
     */
    public List<T> getAll();

    /**
     * 根据主键Id获取对应对象
     *
     * @param id
     * @return
     */
    public T getById(String id);

    /**
     * 统计熟悉相同的数据个数
     *
     * @param params
     * @return
     */
    public Integer count(T params);

    /**
     * like统计
     *
     * @param findParams
     * @return
     */
    public Integer countLike(T findParams);

    /**
     * @param customQueryParams
     * @return
     */
    public Integer countQuery(List<CustomQueryParam> customQueryParams);

    /**
     * @param customQueryParams
     * @return
     */
    public List<T> query(List<CustomQueryParam> customQueryParams);


    public List<T> findByObj(T findParams);


    /**
     * 插入单条记录
     *
     * @param t
     * @throws BaseException
     */
    public void insert(T t) throws BaseException;

    /**
     * 插入单条记录
     *
     * @param list
     * @throws BaseException
     */
    public void insert(List<T> list) throws BaseException;

    /**
     * 批量插入多条记录
     *
     * @param
     * @return
     */
    public void insertBatch(List<T> list) throws BaseException;

    /**
     * 根据ID删除记录
     *
     * @param
     * @return
     */
    public void deleteById(String id) throws BaseException;

    /**
     * 根据ID删除记录
     *
     * @param
     * @return
     */
    public void deleteById(List<String> list) throws BaseException;

    /**
     * 根据单表对象删除记录
     *
     * @param
     * @return
     */
    public void delete(T t) throws BaseException;

    /**
     * 根据单表对象删除记录
     *
     * @param
     * @return
     */
    public void delete(List<T> list) throws BaseException;

    /**
     * 删除该表所有记录
     *
     * @param
     * @return
     */
    public void deleteAll() throws BaseException;

    /**
     * 更新表
     *
     * @param
     * @return
     */
    public void update(T t) throws BaseException;

    /**
     * 更新表
     *
     * @param
     * @return
     */
    public void update(List<T> list) throws BaseException;

    /**
     * 根据参数查询并获得结果集
     *
     * @param findParams
     * @return
     */
    public List<T> getByObj(T findParams);

    /**
     * 根据参数查询并获得一个结果
     *
     * @param findParams
     * @return
     */
    public T getOneByObj(T findParams);

}
