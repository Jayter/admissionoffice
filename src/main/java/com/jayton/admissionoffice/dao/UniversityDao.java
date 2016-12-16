package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.university.University;

import java.util.List;

public interface UniversityDao extends Dao<University> {
    List<University> getByCity(String city, long offset, long count) throws DAOException;
    List<University> getAll(long offset, long count) throws DAOException;
    long getTotalCount() throws DAOException;
}