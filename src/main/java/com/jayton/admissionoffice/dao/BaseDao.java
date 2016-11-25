package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.model.BaseEntity;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface BaseDao<T extends BaseEntity> {
    T get(Long id);
    void add(T entity);
    void update(T entity);
    void delete(T entity);
    void delete(Long id);
}
