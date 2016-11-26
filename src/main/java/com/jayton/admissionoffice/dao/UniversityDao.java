package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.University;

import java.util.List;

/**
 * Created by Jayton on 24.11.2016.
 */
public interface UniversityDao extends BaseDao<University> {
    List<University> getByCity(String city) throws DAOException;
    List<University> getAll() throws DAOException;
}
