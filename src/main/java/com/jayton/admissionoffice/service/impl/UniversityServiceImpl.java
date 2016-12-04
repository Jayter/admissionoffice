package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
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

        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.add(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, String city, String address) throws ServiceException {
        ServiceVerifier.verifyId(id);
        ServiceVerifier.verifyStrings(name, city, address);

        University university = new University(id, name, city, address);

        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            universityDao.update(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getAll() throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getByCity(String city) throws ServiceException {
        ServiceVerifier.verifyString(city);

        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getByCity(city);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}