package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.service.exception.ServiceException;

/**
 * Created by Jayton on 01.12.2016.
 */
public interface SubjectService extends BaseService<Subject> {
    void update(Long id, String name) throws ServiceException;
    Long add(String name) throws ServiceException;
}
