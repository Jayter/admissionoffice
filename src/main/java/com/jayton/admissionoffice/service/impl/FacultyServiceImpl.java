package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcFacultyDaoImpl;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public class FacultyServiceImpl implements FacultyService {

    @Override
    public Long add(String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException {
        ServiceVerifier.verifyStrings(name, officePhone, address);
        ServiceVerifier.verifyEmail(officeEmail);
        ServiceVerifier.verifyId(universityId);

        Faculty faculty = new Faculty(name, officePhone, officeEmail, address, universityId);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException {
        ServiceVerifier.verifyIds(id, universityId);
        ServiceVerifier.verifyStrings(name, officePhone, officeEmail, address);

        Faculty faculty = new Faculty(name, officePhone, officeEmail, address, universityId);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            jdbcFacultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Faculty> getAll() throws ServiceException {
        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Faculty> getByUniversity(Long universityId) throws ServiceException {
        ServiceVerifier.verifyId(universityId);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.getByUniversity(universityId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
