package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.util.List;

public interface ApplicationDao {
    Application get(Long id) throws DAOException;
    Application add(Application application) throws DAOException;
    Application update(Application application, Status status) throws DAOException;
    void delete(Long id) throws DAOException;
    List<Application> getByUser(Long userId) throws DAOException;
    List<Application> getByDirection(Long directionId) throws DAOException;
    List<Application> getAll() throws DAOException;
}
