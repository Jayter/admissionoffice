package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.exception.ServiceException;

public interface UniversityService extends Service<University> {
    PaginationDTO<University> getWithCount(long offset, long count) throws ServiceException;
    PaginationDTO<University> getWithCountByCity(String city, long offset, long count) throws ServiceException;
    long add(University university) throws ServiceException;
    boolean update(University university) throws ServiceException;
}