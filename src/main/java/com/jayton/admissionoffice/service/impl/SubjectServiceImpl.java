package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.SubjectDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.service.SubjectService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

/**
 * Created by Jayton on 01.12.2016.
 */
public class SubjectServiceImpl implements SubjectService {

    @Override
    public Long add(String name) throws ServiceException {
        ServiceVerifier.verifyString(name);

        Subject subject = new Subject(name);

        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            return subjectDao.add(subject);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Subject get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            return subjectDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name) throws ServiceException {
        ServiceVerifier.verifyId(id);

        Subject subject = new Subject(id, name);

        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            subjectDao.update(subject);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            return subjectDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Subject> getAll() throws ServiceException {
        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            return subjectDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
