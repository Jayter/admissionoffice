package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public class UniversityServiceImpl implements UniversityService {

    @Override
    public University add(University university) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.add(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University get(Long id) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University update(University university) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.update(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(Long id) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            universityDao.delete(id);
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
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getByCity(city);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}