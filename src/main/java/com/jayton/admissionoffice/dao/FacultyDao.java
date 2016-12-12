package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;

import java.util.List;

public interface FacultyDao extends Dao<Faculty> {
    List<Faculty> getByUniversity(long universityId) throws DAOException;
}