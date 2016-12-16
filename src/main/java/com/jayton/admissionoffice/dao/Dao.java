package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;

import java.util.List;

public interface Dao<T> {
    T get(long id) throws DAOException;
    T add(T entity) throws DAOException;
    T update(T entity) throws DAOException;
    void delete(long id) throws DAOException;
}