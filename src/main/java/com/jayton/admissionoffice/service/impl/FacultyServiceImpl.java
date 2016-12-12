package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {

    @Override
    public Faculty add(Faculty faculty) throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty get(long id) throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty update(Faculty faculty) throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            facultyDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Faculty> getAll() throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Faculty> getByUniversity(long universityId) throws ServiceException {
        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.getByUniversity(universityId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}