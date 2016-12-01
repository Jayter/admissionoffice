package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcUniversityDaoImpl;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

import static com.jayton.admissionoffice.service.util.ServiceVerifier.*;

/**
 * Created by Jayton on 28.11.2016.
 */
public class UniversityServiceImpl implements UniversityService {

    @Override
    public University get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load university.", e);
        }
    }

    @Override
    public List<University> getByCity(String city) throws ServiceException {
        ServiceVerifier.verifyString(city);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.getByCity(city);
        } catch (DAOException e) {
            throw new ServiceException("Failed to load universities.", e);
        }
    }

    @Override
    public Long add(String name, String city, String address) throws ServiceException {
        verifyData(name, city, address);

        University university = new University(name, city, address);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.add(university);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add university.", e);
        }
    }

    @Override
    public void update(Long id, String name, String city, String address) throws ServiceException {
        ServiceVerifier.verifyId(id);
        verifyData(name, city, address);

        University university = new University(name, city, address);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            jdbcUniversityDao.update(university);
        } catch (DAOException e) {
            throw new ServiceException("Failed to add university.", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete university.", e);
        }
    }

    @Override
    public List<University> getAll() throws ServiceException {
        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to get universities.", e);
        }
    }

    private void verifyData(String name, String city, String address) throws ServiceException {
        if (ServiceVerifier.isNullOrEmpty(name, city, address)) {
            throw new ServiceVerificationException(NULL_OR_EMPTY);
        }
    }
}