package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.util.Objects;

public class FacultyServiceImpl implements FacultyService {

    @Injected
    private FacultyDao facultyDao;

    public FacultyServiceImpl() {
    }

    @Override
    public long add(Faculty faculty) throws ServiceException {
        try {
            Objects.requireNonNull(faculty);
            return facultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
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
    public boolean update(Faculty faculty) throws ServiceException {
        try {
            Objects.requireNonNull(faculty);
            return facultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        } catch (NullPointerException e) {
            throw new ServiceVerificationException("Nullable input parameter.");
        }
    }

    @Override
    public boolean delete(long id) throws ServiceException {
        try {
            return facultyDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDto<Faculty> getWithCountByUniversity(long universityId, long offset, long count) throws ServiceException {
        try {
            return facultyDao.getWithCountByUniversity(universityId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}