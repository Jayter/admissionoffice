package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.FacultyDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.FacultyService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public class FacultyServiceImpl implements FacultyService {

    @Override
    public Long add(String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException {
        ServiceVerifier.verifyStrings(name, officePhone, address);
        ServiceVerifier.verifyEmail(officeEmail);
        ServiceVerifier.verifyId(universityId);

        Faculty faculty = new Faculty(name, officePhone, officeEmail, address, universityId);

        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.add(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Faculty get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException {
        ServiceVerifier.verifyIds(id, universityId);
        ServiceVerifier.verifyStrings(name, officePhone, officeEmail, address);

        Faculty faculty = new Faculty(id, name, officePhone, officeEmail, address, universityId);

        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            facultyDao.update(faculty);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.delete(id);
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
    public List<Faculty> getByUniversity(Long universityId) throws ServiceException {
        ServiceVerifier.verifyId(universityId);

        FacultyDao facultyDao = FactoryProducer.getInstance().getPostgresDaoFactory().getFacultyDao();
        try {
            return facultyDao.getByUniversity(universityId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}
