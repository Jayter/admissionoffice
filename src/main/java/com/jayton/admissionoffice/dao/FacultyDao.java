package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;

import java.util.List;

public interface FacultyDao extends Dao<Faculty> {
    List<Faculty> getByUniversity(long universityId, long offset, long count) throws DAOException;
    List<Faculty> getAll() throws DAOException;
    long getCount(long universityId) throws DAOException;
}