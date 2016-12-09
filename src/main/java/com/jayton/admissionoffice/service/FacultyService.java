package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

public interface FacultyService extends Service<Faculty> {
    Faculty add(Faculty faculty) throws ServiceException;
    Faculty update(Faculty faculty) throws ServiceException;
    List<Faculty> getByUniversity(Long universityId) throws ServiceException;
}