package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;

public interface Dao<T> {
    T get(long id) throws DAOException;
    long add(T entity) throws DAOException;
    boolean update(T entity) throws DAOException;
    boolean delete(long id) throws DAOException;
}