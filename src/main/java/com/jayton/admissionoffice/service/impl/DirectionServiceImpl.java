package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DirectionServiceImpl implements DirectionService {

    @Override
    public Direction add(Direction direction) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction get(long id) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction update(Direction direction) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            directionDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Direction> getAll() throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Direction> getByFaculty(long facultyId) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.getByFaculty(facultyId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addEntranceSubject(long directionId, long subjectId, BigDecimal coef) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();

        Map<Long, BigDecimal> entranceSubjects;
        try {
            entranceSubjects = directionDao.getEntranceSubjects(directionId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

        if(entranceSubjects.size() >= 5) {
            throw new ServiceVerificationException("Cannot add more than 5 exam results.");
        }
        if(entranceSubjects.containsKey(subjectId)) {
            throw new ServiceVerificationException("Entrance subject already exists.");
        }

        try {
            directionDao.addSubject(directionId, subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEntranceSubject(long directionId, long subjectId) throws ServiceException {
        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            directionDao.deleteSubject(directionId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }
}