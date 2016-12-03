package com.jayton.admissionoffice.service;

import com.jayton.admissionoffice.model.university.University;
import com.jayton.admissionoffice.service.exception.ServiceException;

import java.util.List;

/**
 * Created by Jayton on 28.11.2016.
 */
public interface UniversityService extends Service<University> {
    List<University> getByCity(String city) throws ServiceException;
    Long add(String name, String city, String address) throws ServiceException;
    void update(Long id, String name, String city, String address) throws ServiceException;
}
