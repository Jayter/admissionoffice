package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;

import java.math.BigDecimal;
import java.util.List;

public interface DirectionDao extends BaseDao<Direction> {
    List<Direction> getByFaculty(Long facultyId) throws DAOException;
    void deleteSubject(Long directionId, Long subjectId) throws DAOException;
    void addSubject(Long directionId, Long subjectId, BigDecimal coef) throws DAOException;
}
