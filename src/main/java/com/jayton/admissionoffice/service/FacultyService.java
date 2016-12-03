package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.university.Faculty;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public interface FacultyService extends Service<Faculty> {
    Long add(String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException;
    void update(Long id, String name, String officePhone, String officeEmail, String address, Long universityId)
            throws ServiceException;
    List<Faculty> getByUniversity(Long universityId) throws ServiceException;
}
