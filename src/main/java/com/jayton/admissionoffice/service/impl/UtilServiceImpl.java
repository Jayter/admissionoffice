package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UtilDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.to.SessionTerms;
import com.jayton.admissionoffice.service.UtilService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ApplicationHandler;

import java.util.List;

public class UtilServiceImpl implements UtilService {

    @Override
    public List<Subject> getAllSubjects() throws ServiceException {
        UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();
        try {
            return utilDao.getAllSubjects();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public SessionTerms getSessionTerms(short year) throws ServiceException {
        UtilDao utilDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUtilDao();
        try {
            return utilDao.getSessionTerms(year);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void handleApplications() throws ServiceException {
        ApplicationHandler handler = ApplicationHandler.getInstance();
        handler.handleApplications();
    }
}