package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcUniversityDaoImpl;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public class UniversityServiceImpl implements UniversityService {

    @Override
    public Long add(String name, String city, String address) throws ServiceException {
        ServiceVerifier.verifyStrings(name, city, address);

        University university = new University(name, city, address);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.add(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, String city, String address) throws ServiceException {
        ServiceVerifier.verifyId(id);
        ServiceVerifier.verifyStrings(name, city, address);

        University university = new University(name, city, address);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            jdbcUniversityDao.update(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getAll() throws ServiceException {
        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getByCity(String city) throws ServiceException {
        ServiceVerifier.verifyString(city);

        JdbcUniversityDaoImpl jdbcUniversityDao = JdbcUniversityDaoImpl.getInstance();
        try {
            return jdbcUniversityDao.getByCity(city);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}