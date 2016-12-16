package com.jayton.admissionoffice.dao;

import com.jayton.admissionoffice.dao.exception.DAOException;
import com.jayton.admissionoffice.model.to.Application;
import com.jayton.admissionoffice.model.to.Status;

import java.util.List;

public interface ApplicationDao {
    Application get(long id) throws DAOException;
    Application add(Application application) throws DAOException;
    void update(long id, Status status) throws DAOException;
    void delete(long id) throws DAOException;
    void updateAll(List<Application> applications, Status status) throws DAOException;
    List<Application> getByUser(long userId) throws DAOException;
    List<Application> getByDirection(long directionId, long offset, long count) throws DAOException;
    List<Application> getAll() throws DAOException;
    long getCount(long directionId) throws DAOException;
}