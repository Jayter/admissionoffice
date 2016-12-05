package com.jayton.admissionoffice.service.impl;

import com.jayton.admissionoffice.dao.DirectionDao;
import com.jayton.admissionoffice.dao.FactoryProducer;
import com.jayton.admissionoffice.dao.SubjectDao;
import com.jayton.admissionoffice.dao.exception.DAOException;
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

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.add(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public Direction get(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.get(id);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public void update(Long id, String name, BigDecimal averageCoef, int countOfStudents, Long facultyId) throws ServiceException {
        ServiceVerifier.verifyIds(id, facultyId);
        verifyDirectionData(name, countOfStudents);
        ServiceVerifier.verifyCoef(averageCoef);

        Direction direction = new Direction(id, name, averageCoef, countOfStudents, facultyId);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            directionDao.update(direction);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
    }

    @Override
    public boolean delete(Long id) throws ServiceException {
        ServiceVerifier.verifyId(id);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.delete(id);
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
    public List<Direction> getByFaculty(Long facultyId) throws ServiceException {
        ServiceVerifier.verifyId(facultyId);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.getByFaculty(facultyId);
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

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            subjects = directionDao.getByDirection(direction.getId());
        } catch (DAOException e) {
            throw new ServiceException(e);
        }
        subjects.put(subjectId, coef);
        verifySubjectsData(direction.getAverageCoefficient(), subjects);

        try {
            directionDao.addSubject(direction.getId(), subjectId, coef);
        } catch (DAOException e) {
            throw new ServiceException(e);
        }

    }

    @Override
    public boolean deleteEntranceSubject(Long directionId, Long subjectId) throws ServiceException {
        ServiceVerifier.verifyIds(directionId, subjectId);

        DirectionDao directionDao = FactoryProducer.getInstance().getPostgresDaoFactory().getDirectionDao();
        try {
            return directionDao.deleteSubject(directionId, subjectId);
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
        SubjectDao subjectDao = FactoryProducer.getInstance().getPostgresDaoFactory().getSubjectDao();
        try {
            compulsory = subjectDao.getByName("Українська мова та література");
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
        sum = subjects.values().stream().reduce(sum, BigDecimal::add);
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
