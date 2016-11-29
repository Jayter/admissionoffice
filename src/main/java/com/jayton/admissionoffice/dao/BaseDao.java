package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.NamedEntity;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface BaseDao<T extends NamedEntity> {
    T get(Long id) throws DAOException;
    void add(T entity) throws DAOException;
    void update(T entity) throws DAOException;
    void delete(T entity) throws DAOException;
    void delete(Long id) throws DAOException;
    List<T> getAll() throws DAOException;
}
