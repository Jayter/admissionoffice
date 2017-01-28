package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.to.PaginationDTO;
import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.exception.ServiceException;

public interface FacultyService extends Service<Faculty> {
    long add(Faculty faculty) throws ServiceException;
    boolean update(Faculty faculty) throws ServiceException;
    PaginationDTO<Faculty> getWithCountByUniversity(long universityId, long offset, long count) throws ServiceException;
}