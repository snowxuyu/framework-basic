package org.framework.basic.service.impl;

import org.framework.basic.dao.BaseDao;
import org.framework.basic.entity.BaseEntity;
import org.framework.basic.mybatis.complexQuery.CustomQueryParam;
import org.framework.basic.service.BaseService;
import org.framework.exception.BaseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by snow on 2015/8/20.
 */
public class BaseServiceImpl<T extends BaseEntity> implements BaseService<T> {

    @Autowired
    private BaseDao<T> baseDao;

    @Override
    public List<T> getAll() {
        return baseDao.getAll();
    }

    @Override
    public T getById(String id) {
        return baseDao.getById(id);
    }


    @Override
    public Integer count(T params) {
        return baseDao.count(params);
    }

    @Override
    public Integer countLike(T findParams) {
        return baseDao.countLike(findParams);
    }

    @Override
    public Integer countQuery(List<CustomQueryParam> customQueryParams) {
        return baseDao.countQuery(customQueryParams);
    }

    @Override
    public List<T> query(List<CustomQueryParam> customQueryParams) {
        return baseDao.query(customQueryParams, null);
    }


    @Override
    public List<T> findByObj(T findParams) {
        return baseDao.find(findParams);
    }


    @Override
    @Transactional(readOnly = false)
    public void insert(T t) throws BaseException {
        if (baseDao.insert(t) != 1) {
            throw new BaseException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void insert(List<T> list) throws BaseException {
        for (T t : list) {
            insert(t);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void insertBatch(List<T> list) throws BaseException {
        baseDao.insertBatch(list);
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(String id) throws BaseException {
        if (baseDao.deleteById(id) != 1) {
            throw new BaseException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteById(List<String> list) throws BaseException {
        for (String id : list) {
            deleteById(id);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(T t) throws BaseException {
        if (baseDao.deleteByPrimaryKey(t) != 1) {
            throw new BaseException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void delete(List<T> list) throws BaseException {
        for (T t : list) {
            delete(t);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void deleteAll() throws BaseException {
        baseDao.deleteAll();
    }

    @Override
    @Transactional(readOnly = false)
    public void update(T t) throws BaseException {
        if (baseDao.update(t) != 1) {
            throw new BaseException();
        }
    }

    @Override
    @Transactional(readOnly = false)
    public void update(List<T> list) throws BaseException {
        for (T t : list) {
            update(t);
        }
    }

    @Override
    public List<T> getByObj(T findParams) {
        return baseDao.get(findParams);
    }

    @Override
    public T getOneByObj(T findParams) {
        return baseDao.getOne(findParams);
    }
}
