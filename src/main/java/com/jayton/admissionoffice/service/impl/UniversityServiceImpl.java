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
    public University get(long id) throws ServiceException {
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
    public void delete(long id) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            universityDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getAll(long offset, long count) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getAll(offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getByCity(String city, long offset, long count) throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getByCity(city, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long getTotalCount() throws ServiceException {
        UniversityDao universityDao = FactoryProducer.getInstance().getPostgresDaoFactory().getUniversityDao();
        try {
            return universityDao.getTotalCount();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}