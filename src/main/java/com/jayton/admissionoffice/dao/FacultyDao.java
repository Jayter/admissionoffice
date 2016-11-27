package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.Faculty;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface FacultyDao extends BaseDao<Faculty> {
    List<Faculty> getByUniversity(Long universityId) throws DAOException;
}
