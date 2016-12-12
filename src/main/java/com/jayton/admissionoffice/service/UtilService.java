package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface UtilService {
    List<Subject> getAllSubjects() throws ServiceException;
    SessionTerms getSessionTerms(short year) throws ServiceException;
}