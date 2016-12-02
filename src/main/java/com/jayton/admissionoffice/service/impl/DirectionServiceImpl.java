package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.dao.jdbc.JdbcDirectionDaoImpl;
import com.jayton.admissionoffice.dao.jdbc.JdbcSubjectDaoImpl;
import com.jayton.admissionoffice.model.Subject;
import com.jayton.admissionoffice.model.university.Direction;
import com.jayton.admissionoffice.service.DirectionService;
import com.jayton.admissionoffice.service.exception.ServiceException;
import com.jayton.admissionoffice.service.exception.ServiceVerificationException;
import com.jayton.admissionoffice.service.util.ServiceVerifier;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import static com.jayton.admissionoffice.service.util.ServiceVerifier.NULLABLE;

/**
 * Created by Jayton on 01.12.2016.
 */
public class DirectionServiceImpl implements DirectionService {

    @Override
    public Long add(String name, BigDecimal averageCoef, int countOfStudents, Long facultyId,
                    Map<Long, BigDecimal> subjects) throws ServiceException {
        verifyDirectionData(name, countOfStudents);
        ServiceVerifier.verifyId(facultyId);
        verifySubjectsData(averageCoef, subjects);

        Direction direction = new Direction(name, averageCoef, countOfStudents, facultyId, subjects);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, BigDecimal averageCoef, int countOfStudents, Long facultyId,
                       Map<Long, BigDecimal> subjects) throws ServiceException {
        ServiceVerifier.verifyIds(id, facultyId);
        verifyDirectionData(name, countOfStudents);
        verifySubjectsData(averageCoef, subjects);

        Direction direction = new Direction(id, name, averageCoef, countOfStudents, facultyId, subjects);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            jdbcDirectionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.delete(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Direction> getAll() throws ServiceException {
        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.getAll();
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public List<Direction> getByFaculty(Long facultyId) throws ServiceException {
        ServiceVerifier.verifyId(facultyId);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.getByFaculty(facultyId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public synchronized void addEntranceSubject(Direction direction, Long subjectId, BigDecimal coef)
            throws ServiceException {
        ServiceVerifier.verifyIds(subjectId);
        ServiceVerifier.verifyCoef(coef);

        Map<Long, BigDecimal> subjects;

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            subjects = jdbcDirectionDao.getByDirection(direction.getId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        subjects.put(subjectId, coef);
        verifySubjectsData(direction.getAverageCoefficient(), subjects);

        try {
            jdbcDirectionDao.addSubject(direction.getId(), subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public boolean deleteEntranceSubject(Long directionId, Long subjectId) throws ServiceException {
        ServiceVerifier.verifyIds(directionId, subjectId);

        JdbcDirectionDaoImpl jdbcDirectionDao = JdbcDirectionDaoImpl.getInstance();
        try {
            return jdbcDirectionDao.deleteSubject(directionId, subjectId);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    private void verifySubjectsData(BigDecimal averageCoef, Map<Long, BigDecimal> subjects) throws ServiceException {
        ServiceVerifier.verifyCoef(averageCoef);
        if(Objects.isNull(subjects)) {
            throw new ServiceVerificationException(String.format(NULLABLE, "Subjects"));
        }
        if(subjects.size() != 3) {
            throw new ServiceVerificationException("Count of subjects must be 3.");
        }

        Subject compulsory;
        JdbcSubjectDaoImpl jdbcSubjectDao = JdbcSubjectDaoImpl.getInstance();
        try {
            compulsory = jdbcSubjectDao.getByName("Українська мова та література");
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        if(!subjects.containsKey(compulsory.getId())) {
            throw new ServiceVerificationException("Subjects does not contain compulsory.");
        }
        if(subjects.keySet().size() != subjects.values().size()) {
            throw new ServiceVerificationException("Subjects must be different.");
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

    private void verifyDirectionData(String name, int countOfStudents) throws ServiceException {
        ServiceVerifier.verifyString(name);

        if(countOfStudents <= 0) {
            throw new ServiceVerificationException("Count of students must be a positive number.");
        }
    }
}
