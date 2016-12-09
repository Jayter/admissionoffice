package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.NamedEntity;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface UtilService {
    List<NamedEntity> getAllSubjects() throws ServiceException;
    SessionTerms getSessionTerms(Short year) throws ServiceException;
}
