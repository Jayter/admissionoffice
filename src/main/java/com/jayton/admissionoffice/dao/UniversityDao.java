package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.PaginationDto;
import com.jayton.admissionoffice.model.university.University;

public interface UniversityDao extends Dao<University> {
    PaginationDto<University> getWithCountByCity(String city, long offset, long count) throws DAOException;
    PaginationDto<University> getWithCount(long offset, long count) throws DAOException;
}