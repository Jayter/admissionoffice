package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcDirectionDaoImpl;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by Jayton on 01.12.2016.
 */
public class DirectionServiceImpl implements DirectionService {

    @Override
    public Long add(String name, BigDecimal averageCoef, int countOfStudents, Long facultyId,
                    Map<Long, BigDecimal> subjects) throws ServiceException {
        verifyDirectionData(name, countOfStudents, facultyId);
        verifySubjectsData(averageCoef, subjects);

        Direction direction = new Direction(name, averageCoef, countOfStudents, facultyId, subjects);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException("Failed to save direction.", e);
        }
    }

    @Override
    public Direction get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to get direction.", e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete direction.", e);
        }
    }

    @Override
    public List<Direction> getAll() throws ServiceException {
        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException("Failed to get directions.", e);
        }
    }

    @Override
    public List<Direction> getByFaculty(Long facultyId) throws ServiceException {
        ServiceVerifier.verifyId(facultyId);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.getByFaculty(facultyId);
        } catch (DAOException e) {
            throw new ServiceException("Failed to get faculties.", e);
        }
    }

    @Override
    public void update(Long id, String name, BigDecimal averageCoef, int countOfStudents, Long facultyId,
                       Map<Long, BigDecimal> subjects) throws ServiceException {
        ServiceVerifier.verifyId(id);
        verifyDirectionData(name, countOfStudents, facultyId);
        verifySubjectsData(averageCoef, subjects);

        Direction direction = new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            jdbcDirectionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException("Failed to update direction.", e);
        }
    }

    @Override
    public void addEntranceSubject(Direction direction, Long subjectId, BigDecimal coef)
            throws ServiceException {
        Map<Long, BigDecimal> subjects = direction.getEntranceSubjects();
        subjects.put(subjectId, coef);
        verifySubjectsData(direction.getAverageCoefficient(), subjects);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            jdbcDirectionDao.addSubject(direction.getId(), subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete entrance subjects.", e);
        }

    }

    @Override
    public boolean deleteEntranceSubject(Long directionId, Long subjectId) throws ServiceException {
        ServiceVerifier.verifyId(directionId);
        ServiceVerifier.verifyId(subjectId);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.deleteSubject(directionId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException("Failed to delete entrance subjects.", e);
        }
    }

    private void verifySubjectsData(BigDecimal averageCoef, Map<Long, BigDecimal> subjects) throws ServiceException {
        ServiceVerifier.verifyCoef(averageCoef);
        if(subjects == null || subjects.isEmpty() || subjects.size() != 3) {
            throw new ServiceVerificationException("Count of subjects must be 3.");
        }
        for(Map.Entry<Long, BigDecimal> pair: subjects.entrySet()) {
            ServiceVerifier.verifyId(pair.getKey());
            ServiceVerifier.verifyCoef(pair.getValue());
        }

        BigDecimal sum = averageCoef;
        subjects.values().stream().reduce(sum, BigDecimal::add);
        if(sum.compareTo(BigDecimal.ONE) != 0) {
            throw new ServiceVerificationException("Sum of coefficients must be strictly equal to 1.");
        }
    }

    private void verifyDirectionData(String name, int countOfStudents, Long facultyId)
            throws ServiceException {
        ServiceVerifier.verifyString(name);
        if(!ServiceVerifier.isPositive(countOfStudents)) {
            throw new ServiceVerificationException("Count must be positive.");
        }
        ServiceVerifier.verifyId(facultyId);
    }
}
