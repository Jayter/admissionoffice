package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Direction;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface DirectionDao extends BaseDao<Direction> {
    List<Direction> getByFaculty(Long facultyId) throws DAOException;
}
