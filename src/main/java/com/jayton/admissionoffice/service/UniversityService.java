package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface UniversityService extends Service<University> {
    PaginationDTO<University> getWithCount(long offset, long count) throws ServiceException;
    List<University> getByCity(String city, long offset, long count) throws ServiceException;
    University add(University university) throws ServiceException;
    University update(University university) throws ServiceException;
}