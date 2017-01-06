package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public class FacultyServiceImpl implements FacultyService {

    private FacultyDao facultyDao;

    public FacultyServiceImpl() {
    }

    public void setFacultyDao(FacultyDao facultyDao) {
        this.facultyDao = facultyDao;
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
    public List<Faculty> getByUniversity(long universityId, long offset, long count) throws ServiceException {
        try {
            return facultyDao.getByUniversity(universityId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public long getCount(long universityId) throws ServiceException {
        try {
            return facultyDao.getCount(universityId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}