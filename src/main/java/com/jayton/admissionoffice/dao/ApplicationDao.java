package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.util.List;

/**
 * Created by Jayton on 30.11.2016.
 */
public interface ApplicationDao {
    Application get(Long id) throws DAOException;
    Long add(Application application) throws DAOException;
    void update(Long id, Status status) throws DAOException;
    boolean delete(Long id) throws DAOException;
    List<Application> getByUser(Long userId) throws DAOException;
    List<Application> getByDirection(Long directionId) throws DAOException;
    List<Application> getAll() throws DAOException;
}
