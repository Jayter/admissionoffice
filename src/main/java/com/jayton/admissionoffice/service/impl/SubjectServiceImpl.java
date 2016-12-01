package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcSubjectDaoImpl;
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

        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();
        try {
            return jdbcSubjectDao.add(subject);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load subject.", e);
        }
    }

    @Override
    public Subject get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();

        try {
            return jdbcSubjectDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load subject.", e);
        }
    }

    @Override
    public List<Subject> getAll() throws ServiceException {
        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();
        try {
            return jdbcSubjectDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to load subjects.", e);
        }
    }

    @Override
    public void update(Long id, String name) throws ServiceException {
        ServiceVerifier.verifyId(id);

        Subject subject = new Subject(id, name);

        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();

        try {
            jdbcSubjectDao.update(subject);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update subject.", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();

        try {
            return jdbcSubjectDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete subject.", e);
        }
    }
}
