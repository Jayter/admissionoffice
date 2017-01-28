package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.service.exception.ServiceException;

public interface Service<T> {
    T get(long id) throws ServiceException;
    boolean delete(long id) throws ServiceException;
}