package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface Service<T> {
    T get(long id) throws ServiceException;
    void delete(long id) throws ServiceException;
    List<T> getAll() throws ServiceException;
}