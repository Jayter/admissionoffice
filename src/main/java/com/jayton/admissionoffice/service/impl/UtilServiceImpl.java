package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ApplicationHandler;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.List;

public class UtilServiceImpl implements UtilService {

    @Injected
    private UtilDao utilDao;
    @Injected
    private ApplicationHandler applicationHandler;

    public UtilServiceImpl() {
    }

    @Override
    public List<Subject> getAllSubjects() throws ServiceException {
        try {
            return utilDao.getAllSubjects();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SessionTerms getSessionTerms(short year) throws ServiceException {
        try {
            return utilDao.getSessionTerms(year);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void updateSessionTerms(SessionTerms terms) throws ServiceException {
        try {
            utilDao.updateSessionTerms(terms);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void createSessionTerms(SessionTerms terms) throws ServiceException {
        try {
            utilDao.createSessionTerms(terms);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void handleApplications() throws ServiceException {
        applicationHandler.handleApplications();
    }
}