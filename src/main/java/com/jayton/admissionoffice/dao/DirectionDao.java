package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface DirectionDao extends Dao<Direction> {
    List<Direction> getByFaculty(long facultyId, long offset, long count) throws DAOException;
    void deleteSubject(long directionId, long subjectId) throws DAOException;
    void addSubject(long directionId, long subjectId, BigDecimal coef) throws DAOException;
    Map<Long, BigDecimal> getEntranceSubjects(long directionId) throws DAOException;
    long getCount(long facultyId) throws DAOException;
    List<Direction> getAll() throws DAOException;
    Map<Long, String> getUserNames(long directionId) throws DAOException;
}