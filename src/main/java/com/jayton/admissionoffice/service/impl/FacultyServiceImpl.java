package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {

    @Injected
    private FacultyDao facultyDao;

    public FacultyServiceImpl() {
    }

    @Override
    public Faculty add(Faculty faculty) throws ServiceException {
        try {
            return facultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty get(long id) throws ServiceException {
        try {
            return facultyDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty update(Faculty faculty) throws ServiceException {
        try {
            return facultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            facultyDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDTO<Faculty> getWithCountByUniversity(long universityId, long offset, long count) throws ServiceException {
        try {
            return facultyDao.getWithCountByUniversity(universityId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}