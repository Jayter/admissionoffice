package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;

public interface UniversityDao extends Dao<University> {
    PaginationDTO<University> getWithCountByCity(String city, long offset, long count) throws DAOException;
    PaginationDTO<University> getWithCount(long offset, long count) throws DAOException;
}