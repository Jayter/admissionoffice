package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.NamedEntity;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface Service<T extends NamedEntity> {
    T get(Long id) throws ServiceException;
    void delete(Long id) throws ServiceException;
    List<T> getAll() throws ServiceException;
}