package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.UniversityDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.UniversityService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.List;

public class UniversityServiceImpl implements UniversityService {

    @Injected
    private UniversityDao universityDao;

    public UniversityServiceImpl() {
    }

    @Override
    public University add(University university) throws ServiceException {
        try {
            return universityDao.add(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University get(long id) throws ServiceException {
        try {
            return universityDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public University update(University university) throws ServiceException {
        try {
            return universityDao.update(university);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            universityDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDTO<University> getWithCount(long offset, long count) throws ServiceException {
        try {
            return universityDao.getWithCount(offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<University> getByCity(String city, long offset, long count) throws ServiceException {
        try {
            return universityDao.getByCity(city, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}