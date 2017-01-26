package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.util.di.Injected;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class DirectionServiceImpl implements DirectionService {

    @Injected
    private DirectionDao directionDao;

    public DirectionServiceImpl() {
    }

    @Override
    public Direction add(Direction direction) throws ServiceException {
        verifyEntranceSubjects(direction.getEntranceSubjects(), direction.getAverageCoefficient());
        try {
            return directionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction get(long id) throws ServiceException {
        try {
            return directionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction update(Direction direction) throws ServiceException {
        try {
            return directionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void delete(long id) throws ServiceException {
        try {
            directionDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public PaginationDTO<Direction> getWithCountByFaculty(long facultyId, long offset, long count) throws ServiceException {
        try {
            return directionDao.getWithCountByFaculty(facultyId, offset, count);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addEntranceSubject(long directionId, long subjectId, BigDecimal coef) throws ServiceException {
        try {
            Direction direction = directionDao.get(directionId);

            Map<Long, BigDecimal> entranceSubjects = direction.getEntranceSubjects();
            if(entranceSubjects.containsKey(subjectId)) {
                throw new ServiceVerificationException("Can not add duplicated subject.");
            }
            entranceSubjects.put(subjectId, coef);
            verifyEntranceSubjects(entranceSubjects, direction.getAverageCoefficient());

            directionDao.addSubject(directionId, subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void deleteEntranceSubject(long directionId, long subjectId) throws ServiceException {
        try {
            directionDao.deleteSubject(directionId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Map<Long, String> getUserNames(long directionId) throws ServiceException {
        try {
            return directionDao.getUserNames(directionId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    public void verifyEntranceSubjects(Map<Long, BigDecimal> subjects, BigDecimal coefficient) throws ServiceVerificationException {
        if(subjects.size() > 3) {
            throw new ServiceVerificationException("Cannot add more than 3 entrance subjects.");
        }

        BigDecimal resultingCoef = subjects.values().stream().reduce(coefficient, BigDecimal::add)
                .setScale(2, BigDecimal.ROUND_HALF_UP);
        if(resultingCoef.compareTo(BigDecimal.ONE) != 0) {
            throw new ServiceVerificationException("Total coefficient must be strictly equal to 1.");
        }
    }
}