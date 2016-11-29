package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.EntranceSubject;
import com.jayton.admissionoffice.model.university.Direction;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface DirectionDao extends BaseDao<Direction> {
    List<Direction> getByFaculty(Long facultyId) throws DAOException;
    void addSubjects(List<EntranceSubject> subject) throws DAOException;
    void deleteSubject(EntranceSubject subject) throws DAOException;
    List<EntranceSubject> getSubjects(Long directionId) throws DAOException;

}
