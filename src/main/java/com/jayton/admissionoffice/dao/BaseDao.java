package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.NamedEntity;

import java.util.List;

public interface BaseDao<T extends NamedEntity> {
    T get(Long id) throws DAOException;
    T add(T entity) throws DAOException;
    T update(T entity) throws DAOException;
    void delete(Long id) throws DAOException;
    List<T> getAll() throws DAOException;
}
