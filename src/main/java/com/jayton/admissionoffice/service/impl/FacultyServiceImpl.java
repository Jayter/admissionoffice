package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcFacultyDaoImpl;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

import static com.jayton.admissionoffice.service.util.ServiceVerifier.*;

/**
 * Created by Jayton on 28.11.2016.
 */
public class FacultyServiceImpl implements FacultyService {

    @Override
    public Long add(String name, String officePhone, String officeEmail, String address, Long universityId) throws ServiceException {
        verifyData(name, officePhone, officeEmail, address, universityId);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        Faculty faculty = new Faculty(name, officePhone, officeEmail, address, universityId);
        try {
            return jdbcFacultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add faculty", e);
        }
    }

    @Override
    public Faculty get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load faculty.", e);
        }
    }

    @Override
    public List<Faculty> getByUniversity(Long universityId) throws ServiceException {
        ServiceVerifier.verifyId(universityId);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.getByUniversity(universityId);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load faculties.", e);
        }
    }

    @Override
    public void update(Long id, String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException {
        verifyData(name, officePhone, officeEmail, address, universityId);
        ServiceVerifier.verifyId(id);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        Faculty faculty = new Faculty(name, officePhone, officeEmail, address, universityId);
        try {
            jdbcFacultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add faculty", e);
        }
    }

    @Override
    public List<Faculty> getAll() throws ServiceException {
        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to load faculties.", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcFacultyDaoImpl jdbcFacultyDao = JdbcFacultyDaoImpl.getInstance();
        try {
            return jdbcFacultyDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete faculty.", e);
        }
    }

    private void verifyData(String name, String phone, String email, String address, Long universityId)
            throws ServiceException {
        if(ServiceVerifier.isNullOrEmpty(name, phone, email, address)) {
            throw new ServiceVerificationException(NULL_OR_EMPTY);
        }
        if(!ServiceVerifier.isCorrectId(universityId)) {
            throw new ServiceVerificationException(INCORRECT_ID);
        }
        if(!ServiceVerifier.isPhoneCorrect(phone)) {
            throw new ServiceVerificationException(INCORRECT_PHONE);
        }
        if(!ServiceVerifier.isEmailCorrect(email)) {
            throw new ServiceVerificationException(INCORRECT_EMAIL);
        }
    }
}
