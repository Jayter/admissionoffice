package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.NamedEntity;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public interface BaseService<T extends NamedEntity> {
    T get(Long id) throws ServiceException;
    boolean delete(Long id) throws ServiceException;
    List<T> getAll() throws ServiceException;
}
